package xyz.jadonfowler.phasebot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;

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
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientSwingArmPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

public class PhaseBot {

	private static String USERNAME = "username";
	private static String PASSWORD = "password";
	private static String HOST = "mort.openredstone.org";
	private static int PORT = 25569;
	private static Proxy PROXY = Proxy.NO_PROXY;
	private static boolean VERIFY_USERS = true;

	static double x;
	static double y;
	static double z;
	static float pitch;
	static float yaw;

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
		status();
		login();
	}

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
					event.getSession().send(
							new ClientChatPacket("I am in GameMode: "
									+ event.<ServerJoinGamePacket> getPacket().getGameMode()));
				} else if (event.getPacket() instanceof ServerPlayerPositionRotationPacket) {
					x = event.<ServerPlayerPositionRotationPacket> getPacket().getX();
					y = event.<ServerPlayerPositionRotationPacket> getPacket().getY();
					z = event.<ServerPlayerPositionRotationPacket> getPacket().getZ();
					pitch = event.<ServerPlayerPositionRotationPacket> getPacket().getPitch();
					yaw = event.<ServerPlayerPositionRotationPacket> getPacket().getYaw();
					System.out.println("My Position: " + x + "," + y + "," + z);
					event.getSession().send(new ClientPlayerPositionRotationPacket(false, x, y, z, pitch, yaw));
				} else if (event.getPacket() instanceof ServerChatPacket) {

					Message message = event.<ServerChatPacket> getPacket().getMessage();
					System.out.println(message.getFullText());
					try {
						String c = message.getFullText().split(": ")[1];
						if (c.startsWith(".swing")) {
							event.getSession().send(new ClientSwingArmPacket());
						} else if (c.startsWith(".crouch")) {
						} else if (c.startsWith(".say")) {
							event.getSession().send(new ClientChatPacket(c.split(".say")[1]));
						} else if (c.startsWith(".move ")) {
							double rx = Double.parseDouble(c.split(" ")[1]);
							double ry = Double.parseDouble(c.split(" ")[2]);
							double rz = Double.parseDouble(c.split(" ")[3]);
							move(event, rx, ry, rz);
						}
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

	public static void move(PacketReceivedEvent event, double rx, double ry, double rz) {
		System.out.println("Move: " + (x + rx) + " " + (y + ry) + " " + (z + rz));
		for (double dx = 0; dx != rx; dx += rx > 0 ? 0.5d : -0.5d) {
			for (double dy = 0; dy != ry; dy += ry > 0 ? 0.5d : -0.5d) {
				for (double dz = 0; dz != rz; dz += rz > 0 ? 0.5d : -0.5d) {
					event.getSession().send(
							new ClientPlayerPositionRotationPacket(false, x + dx, y + dy, z + dz, pitch, yaw));
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		x = rx + x;
		y = ry + y;
		z = rz + z;
	}
}
