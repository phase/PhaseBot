package xyz.jadonfowler.phasebot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.spacehq.mc.auth.exception.AuthenticationException;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.ProtocolMode;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoHandler;
import org.spacehq.mc.protocol.data.status.handler.ServerPingTimeHandler;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import xyz.jadonfowler.phasebot.cmd.Command;
import xyz.jadonfowler.phasebot.cmd.CommandManager;

public class PhaseBot {

	private static String USERNAME = "username";
	private static String PASSWORD = "password";
	private static String HOST = "mort.openredstone.org";
	private static int PORT = 25569;
	private static Proxy PROXY = Proxy.NO_PROXY;
	private static boolean VERIFY_USERS = true;
	public static Random random = new Random();

	public static ArrayList<Command> commands = new ArrayList<Command>();

	private static Bot bot;
	static CommandManager manager;

	public static void main(String... args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("res/config.txt"));
			String line = br.readLine();
			while (line != null) {
				if (line.startsWith("Username: ")) { // Username:Notch
					USERNAME = line.split(": ")[1];
				} else if (line.startsWith("Password: ")) { // Password:Derp
					PASSWORD = line.split(": ")[1];
				} else if (line.startsWith("Server: ")) { // Server:minecraft.net:25565
					HOST = line.split(": ")[1];
					PORT = java.lang.Integer.parseInt(line.split(":")[2]);
				} else if (line.startsWith("Proxy")) { // Proxy:123.456.789:860
					PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(line.split(":")[1],
							java.lang.Integer.parseInt(line.split(":")[2])));
				}
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		bot = new Bot(USERNAME, PASSWORD, HOST, PORT, PROXY);

		// status();
		login();
	}

	@SuppressWarnings("unused")
	private static void status() {
		MinecraftProtocol protocol = new MinecraftProtocol(ProtocolMode.STATUS);
		Client client = new Client(HOST, PORT, protocol, new TcpSessionFactory(PROXY));
		client.getSession().setFlag(ProtocolConstants.SERVER_INFO_HANDLER_KEY, new ServerInfoHandler() {
			@Override
			public void handle(Session session, ServerStatusInfo info) {
				System.out.println("Version: " + info.getVersionInfo().getVersionName() + ", "
						+ info.getVersionInfo().getProtocolVersion());
				System.out.println("Player Count: " + info.getPlayerInfo().getOnlinePlayers() + " / "
						+ info.getPlayerInfo().getMaxPlayers());
				System.out.println("Players: " + Arrays.toString(info.getPlayerInfo().getPlayers()));
				System.out.println("Description: " + info.getDescription().getFullText());
				System.out.println("Icon: " + info.getIcon());
			}
		});

		client.getSession().setFlag(ProtocolConstants.SERVER_PING_TIME_HANDLER_KEY, new ServerPingTimeHandler() {
			@Override
			public void handle(Session session, long pingTime) {
				System.out.println("Server ping took " + pingTime + "ms");
			}
		});

		client.getSession().connect();
		while (client.getSession().isConnected()) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void login() {
		MinecraftProtocol protocol = null;
		if (VERIFY_USERS) {
			try {
				protocol = new MinecraftProtocol(USERNAME, PASSWORD, false);
				System.out.println("Successfully authenticated user.");
			} catch (AuthenticationException e) {
				e.printStackTrace();
				return;
			}
		} else {
			protocol = new MinecraftProtocol(USERNAME);
		}

		Client client = new Client(HOST, PORT, protocol, new TcpSessionFactory(PROXY));
		client.getSession().addListener(new SessionAdapter() {
			@Override
			public void packetReceived(PacketReceivedEvent event) {

				if (event.getPacket() instanceof ServerJoinGamePacket) {
					event.getSession().send(new ClientChatPacket("PhaseBot has joined the game."));
					bot.entityId = event.<ServerJoinGamePacket> getPacket().getEntityId();
				} else if (event.getPacket() instanceof ServerPlayerPositionRotationPacket) {
					bot.pos.x = event.<ServerPlayerPositionRotationPacket> getPacket().getX();
					bot.pos.y = event.<ServerPlayerPositionRotationPacket> getPacket().getY();
					bot.pos.z = event.<ServerPlayerPositionRotationPacket> getPacket().getZ();
					bot.pitch = event.<ServerPlayerPositionRotationPacket> getPacket().getPitch();
					bot.yaw = event.<ServerPlayerPositionRotationPacket> getPacket().getYaw();
					System.out.println("Err, My Position: " + bot.pos.x + "," + bot.pos.y + "," + bot.pos.z);
					event.getSession().send(
							new ClientPlayerPositionRotationPacket(false, bot.pos.x, bot.pos.y, bot.pos.z, bot.pitch,
									bot.yaw));
					// } else if (event.getPacket() instanceof
					// ServerMultiChunkDataPacket) {
					// for (Chunk c : event.<ServerChunkDataPacket>
					// getPacket().getChunks()) {
					// c.getBlocks();
					// }
				}

				else if (event.getPacket() instanceof ServerChatPacket) {

					Message message = event.<ServerChatPacket> getPacket().getMessage();
					System.out.println(message.getFullText());
					try {
						String c = message.getFullText().split(": ")[1];
						if (!c.startsWith("."))
							return;
						if (!(message.getFullText().contains("Phase") || message.getFullText().contains("Voltz")
								|| message.getFullText().contains("chibill") || message.getFullText().contains("tyler"))) {
							// event.getSession().send(new
							// ClientChatPacket("You're not my master! D:"));
							return;
						}
						manager.performCommand(c.split(" ")[0].split(".")[1], c.split(" "), event.getSession());
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void disconnected(DisconnectedEvent event) {
				System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
			}
		});

		client.getSession().connect();
	}

	// public static Vector3d bez(Vector3d p0, Vector3d p1, int t) {
	// return new Vector3d(p0.x + (p0.x - p1.x) / t, p0.y + (p0.y - p1.y) / t,
	// p0.z + (p0.z - p1.z) / t);
	// }
	//
	// public static void spline(PacketReceivedEvent event) {
	// move(event, bot.positions[0].x, bot.positions[0].y, bot.positions[0].z);
	// Vector3d tmp = new Vector3d(0, 0, 0);
	// for (int i = 1; i < 100; i++) {
	// tmp = bez(bez(bez(bot.positions[0], bot.positions[1], i),
	// bez(bot.positions[1], bot.positions[2], i), i),
	// bez(bez(bot.positions[1], bot.positions[2], i), bez(bot.positions[2],
	// bot.positions[3], i), i), i);
	// move(event, tmp.x, tmp.y, tmp.z);
	// }
	// }

	public static CommandManager getCommandManager() {
		return manager;
	}

	public static Bot getBot() {
		return bot;
	}
}
