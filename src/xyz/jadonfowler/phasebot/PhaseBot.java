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
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoHandler;
import org.spacehq.mc.protocol.data.status.handler.ServerPingTimeHandler;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import xyz.jadonfowler.phasebot.cmd.Command;
import xyz.jadonfowler.phasebot.cmd.CommandManager;
import xyz.jadonfowler.phasebot.cmd.chat.BlockStand;
import xyz.jadonfowler.phasebot.cmd.chat.Entities;
import xyz.jadonfowler.phasebot.cmd.chat.JavaScript;
import xyz.jadonfowler.phasebot.cmd.chat.Ruby;
import xyz.jadonfowler.phasebot.cmd.chat.Say;
import xyz.jadonfowler.phasebot.cmd.chat.Slap;
import xyz.jadonfowler.phasebot.cmd.chat.Spam;
import xyz.jadonfowler.phasebot.cmd.chat.UUIDCommand;
import xyz.jadonfowler.phasebot.cmd.fun.Derp;
import xyz.jadonfowler.phasebot.cmd.fun.Swing;
import xyz.jadonfowler.phasebot.cmd.position.Dig;
import xyz.jadonfowler.phasebot.cmd.position.Fall;
import xyz.jadonfowler.phasebot.cmd.position.Look;
import xyz.jadonfowler.phasebot.cmd.position.Move;
import xyz.jadonfowler.phasebot.cmd.position.Patrol;
import xyz.jadonfowler.phasebot.cmd.position.Place;
import xyz.jadonfowler.phasebot.cmd.position.Set;
import xyz.jadonfowler.phasebot.cmd.position.Teleport;

public class PhaseBot {

	private static String USERNAME = "username";
	private static String PASSWORD = "password";
	private static String HOST = "mort.openredstone.org";
	private static int PORT = 25569;
	// private static String HOST = "nick.openredstone.org";
	// private static int PORT = 25569;
	private static Proxy PROXY = Proxy.NO_PROXY;
	private static boolean VERIFY_USERS = true;
	public static Random random = new Random();

	public static ArrayList<Command> commands = new ArrayList<Command>();

	private static Bot bot;
	private static CommandManager manager = null;

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
		manager = new CommandManager();

		new JavaScript();
		new Ruby();
		new Say();
		new Slap();
		new Entities();
		new UUIDCommand();
		new Derp();
		new Swing();
		new Look();
		new Move();
		new Patrol();
		new Set();
		new Teleport();
		new Dig();
		new BlockStand();
		new Fall();
		new Spam();
		new Place();
		
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
		System.out.println("PhaseBot starting...");
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

		bot.setClient(client);
		client.getSession().addListener(new PacketHandler());
		client.getSession().connect();
	}

	public static CommandManager getCommandManager() {
		if (manager == null)
			manager = new CommandManager();
		return manager;
	}

	public static Bot getBot() {
		return bot;
	}
}
