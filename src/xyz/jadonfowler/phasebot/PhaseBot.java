package xyz.jadonfowler.phasebot;

import java.io.*;
import java.net.*;
import java.util.*;
import org.reflections.*;
import org.spacehq.mc.auth.exception.*;
import org.spacehq.mc.protocol.*;
import org.spacehq.mc.protocol.data.status.*;
import org.spacehq.mc.protocol.data.status.handler.*;
import org.spacehq.packetlib.*;
import org.spacehq.packetlib.tcp.*;
import lombok.*;
import xyz.jadonfowler.phasebot.cmd.*;

public class PhaseBot {

    private static String USERNAME = "username";
    private static String PASSWORD = "password";
    // Build Server
    private static String HOST = "mort.openredstone.org";
    private static int PORT = 25569;
    // Survival Server
    // private static String HOST = "nick.openredstone.org";
    // private static int PORT = 25569;
    // private static String HOST = "localhost";
    // private static int PORT = 25565;
    private static Proxy PROXY = Proxy.NO_PROXY;
    private static boolean VERIFY_USERS = true;
    public static Random random = new Random();
    public static ArrayList<Command> commands = new ArrayList<Command>();
    private static Bot bot;
    private static CommandManager manager = null;

    public static void main(String... args) {
        manager = new CommandManager();
        try {
            BufferedReader br = new BufferedReader(new FileReader("res/config.txt"));
            String line = br.readLine();
            while (line != null) {
                if (line.startsWith("Username: ")) { // Username:Notch
                    USERNAME = line.split(": ")[1];
                }
                else if (line.startsWith("Password: ")) { // Password:Derp
                    PASSWORD = line.split(": ")[1];
                }
                else if (line.startsWith("Server: ")) { // Server:minecraft.net:25565
                    HOST = line.split(": ")[1];
                    PORT = java.lang.Integer.parseInt(line.split(":")[2]);
                }
                else if (line.startsWith("Proxy")) { // Proxy:123.456.789:860
                    PROXY = new Proxy(Proxy.Type.HTTP,
                            new InetSocketAddress(line.split(":")[1], java.lang.Integer.parseInt(line.split(":")[2])));
                }
                line = br.readLine();
            }
            br.close();
            File scriptDir = new File("res/scripts/");
            File[] dirFiles = scriptDir.listFiles();
            if (dirFiles != null) {
                for (final File script : dirFiles) {
                    @Cleanup BufferedReader sr = new BufferedReader(new FileReader(script));
                    String sline = sr.readLine();
                    final ArrayList<String> cmds = new ArrayList<String>();
                    while (sline != null) {
                        cmds.add(sline);
                        sline = sr.readLine();
                    }
                    new Command() {

                        public void exec(String in, String[] args, Session s) {
                            for (String c : cmds) {
                                manager.performCommand(c.replace(".", "").split(" ")[0], c.split(" "), null);
                            }
                        }

                        public String getCommand() {
                            String name = script.getName();
                            int pos = name.lastIndexOf(".");
                            if (pos > 0) name = name.substring(0, pos);
                            return name;
                        }

                        public String getDescription() {
                            return null;
                        }
                    };
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        registerCommands();
        bot = new Bot(USERNAME, PASSWORD, HOST, PORT, PROXY);
        status();
        login();
    }

    public static void registerCommands() {
        Reflections reflections = new Reflections("xyz.jadonfowler.phasebot.cmd");
        Set<Class<? extends Command>> subTypes = reflections.getSubTypesOf(Command.class);
        for (Class<? extends Command> c : subTypes) {
            try {
                c.getConstructor().newInstance();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void status() {
        MinecraftProtocol protocol = new MinecraftProtocol(ProtocolMode.STATUS);
        Client client = new Client(HOST, PORT, protocol, new TcpSessionFactory(PROXY));
        client.getSession().setFlag(ProtocolConstants.SERVER_INFO_HANDLER_KEY, new ServerInfoHandler() {

            @Override public void handle(Session session, ServerStatusInfo info) {
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

            @Override public void handle(Session session, long pingTime) {
                System.out.println("Server ping took " + pingTime + "ms");
            }
        });
        client.getSession().connect();
        while (client.getSession().isConnected()) {
            try {
                Thread.sleep(5);
            }
            catch (InterruptedException e) {
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
            }
            catch (AuthenticationException e) {
                e.printStackTrace();
                return;
            }
        }
        else {
            protocol = new MinecraftProtocol(USERNAME);
        }
        Client client = new Client(HOST, PORT, protocol, new TcpSessionFactory(PROXY));
        bot.setClient(client);
        client.getSession().addListener(new PacketHandler());
        client.getSession().connect();
    }

    public static CommandManager getCommandManager() {
        if (manager == null) manager = new CommandManager();
        return manager;
    }

    public static Bot getBot() {
        return bot;
    }

    public static String getPrefix() {
        return "owk, ";
    }
}
