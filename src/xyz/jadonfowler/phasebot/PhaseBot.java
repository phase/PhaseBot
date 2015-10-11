package xyz.jadonfowler.phasebot;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.reflections.*;
import org.spacehq.mc.auth.*;
import org.spacehq.mc.auth.exception.*;
import org.spacehq.mc.protocol.*;
import org.spacehq.mc.protocol.data.status.*;
import org.spacehq.mc.protocol.data.status.handler.*;
import org.spacehq.packetlib.*;
import org.spacehq.packetlib.tcp.*;
import lombok.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.gui.*;
import xyz.jadonfowler.phasebot.script.*;

public class PhaseBot {

    @Getter @Setter private static String prefix = ".";
    @Getter private static String[] owners = { "Phase", "Voltz" };
    private static String USERNAME = "username";
    private static String PASSWORD = "password";
    // Build Server
    public static String HOST = "mort.openredstone.org";
    private static int PORT = 25569;
    // Voltz Server
    // private static String HOST = "173.58.127.167";
    // private static int PORT = 25565;
    // Survival Server
    // public static String HOST = "nick.openredstone.org";
    // private static int PORT = 25569;
    // public static String HOST = "localhost";
    // private static int PORT = 25565;
    private static Proxy PROXY = Proxy.NO_PROXY;
    private static boolean VERIFY_USERS = true;
    public static Random random = new Random();
    public static ArrayList<Command> commands = new ArrayList<Command>();
    private static CommandManager manager = null;
    public static ArrayList<Script> scripts = new ArrayList<Script>();
    private static Bot bot;
    @Getter private static ConsoleGui console;
    @Getter private static File configFile;

    public static void main(String... args) {
        manager = new CommandManager();
        console = new ConsoleGui();
        loadConfig();
        registerCommands();
        loadScripts();
        bot = new Bot(USERNAME, PASSWORD, HOST, PORT, PROXY);
        status();
        login();
    }

    public static void loadConfig() {
        try {
            String configLocation;
            // here, we assign the name of the OS, according to Java, to a
            // variable...
            String OS = (System.getProperty("os.name")).toUpperCase();
            // to determine what the workingDirectory is.
            // if it is some version of Windows
            if (OS.contains("WIN")) {
                // it is simply the location of the "AppData" folder
                configLocation = System.getenv("AppData");
            }
            // Otherwise, we assume Linux or Mac
            else {
                // in either case, we would start in the user's home directory
                configLocation = System.getProperty("user.home");
                // if we are on a Mac, we are not done, we look for "Application
                // Support"
                configLocation += "/Library/Application Support/";
            }
            File config = new File(configLocation, "PhaseBot/config.txt");
            configFile = config;
            if (!config.exists()) {
                File cl = new File(configLocation, "PhaseBot/");
                if (!cl.exists()) {
                    cl.mkdir();
                }
                if (config.createNewFile()) {
                    BufferedWriter w = new BufferedWriter(new FileWriter(config));
                    //@formatter:off
                    w.write("PhaseBot Configuration File :O\r\n"
                            + "https://github.com/phase/phasebot\r\n"
                            + "Username: Notch\r\n"
                            + "Password: Derp\r\n"
                            + "Server: github.orgs:25565\r\n"
                            + "Owners: Phase,Voltz\r\n");
                    //@formatter:on
                    w.close();
                }
            }
            else {
                BufferedReader br = new BufferedReader(new FileReader(config));
                String line = br.readLine();
                while (line != null) {
                    try {
                        if (line.startsWith("Username: ")) { // Username:Notch
                            USERNAME = line.split(": ")[1];
                        }
                        else if (line.startsWith("Password: ")) { // Password:Derp
                            PASSWORD = line.split(": ")[1];
                        }
                        else if (line.startsWith("Server: ")) { // Server:minecraft.net:25565
                            HOST = line.split(": ")[1].split(":")[0];
                            PORT = Integer.parseInt(line.split(": ")[1].split(":")[1]);
                        }
                        else if (line.startsWith("Proxy: ")) { // Proxy:123.456.789:860
                            PROXY = new Proxy(Proxy.Type.HTTP,
                                    new InetSocketAddress(line.split(":")[1], Integer.parseInt(line.split(":")[2])));
                        }
                        else if (line.startsWith("Owners: ")) {
                            String o = line.split(": ")[1];
                            owners = o.contains(",") ? o.split(",") : new String[] { o };
                        }
                        else if (line.startsWith("Prefix: ")) {
                            prefix = line.split(": ")[1];
                        }
                        else if (line.startsWith("Online: ")) {
                            VERIFY_USERS = Boolean.parseBoolean(line.split(": ")[1].toLowerCase());
                        }
                    }
                    catch (Exception e) {}
                    line = br.readLine();
                }
                br.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void reloadScripts() {
        scripts = new ArrayList<Script>();
        loadScripts();
    }

    public static ArrayList<File> getFiles(String directoryName) {
        File directory = new File(directoryName);
        ArrayList<File> files = new ArrayList<File>();
        if (!directory.isDirectory()) {
            console.println("No files found in " + directoryName);
            return files;
        }
        // Get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            }
            else if (file.isDirectory()) {
                files.addAll(getFiles(file.getAbsolutePath()));
            }
        }
        return files;
    }

    public static void loadScripts() {
        ArrayList<File> dirFiles = getFiles("res/scripts/");
        dirFiles.addAll(getFiles(configFile.getAbsolutePath().split("config.txt")[0] + "scripts/"));
        if (!dirFiles.isEmpty()) {
            for (final File script : dirFiles) {
                try {
                    @Cleanup BufferedReader sr = new BufferedReader(new FileReader(script));
                    String sline = sr.readLine();
                    final ArrayList<String> cmds = new ArrayList<String>();
                    while (sline != null) {
                        cmds.add(sline);
                        sline = sr.readLine();
                    }
                    String name = script.getName();
                    int pos = name.lastIndexOf(".");
                    if (pos > 0) name = name.substring(0, pos);
                    scripts.add(new Script(name, cmds.toArray(new String[cmds.size()])));
                    new Command() {

                        public void exec(String in, final String[] args, Session s) {
                            for (final Script t : PhaseBot.scripts) {
                                if (t.getName().equalsIgnoreCase(args[0])) {
                                    t.run(args);
                                    return;
                                }
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
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void status() {
        MinecraftProtocol protocol = new MinecraftProtocol(ProtocolMode.STATUS);
        Client client = new Client(HOST, PORT, protocol, new TcpSessionFactory(PROXY));
        client.getSession().setFlag(ProtocolConstants.SERVER_INFO_HANDLER_KEY, new ServerInfoHandler() {

            @Override public void handle(Session session, ServerStatusInfo info) {
                console.println("Version: " + info.getVersionInfo().getVersionName() + ", "
                        + info.getVersionInfo().getProtocolVersion());
                console.println("Player Count: " + info.getPlayerInfo().getOnlinePlayers() + " / "
                        + info.getPlayerInfo().getMaxPlayers());
                String players = "";
                for (GameProfile s : info.getPlayerInfo().getPlayers())
                    players += s.getName() + " ";
                console.println("Players: " + players.trim());
                console.println("Description: " + info.getDescription().getFullText());
                // console.println("Icon: " + info.getIcon());
            }
        });
        client.getSession().setFlag(ProtocolConstants.SERVER_PING_TIME_HANDLER_KEY, new ServerPingTimeHandler() {

            @Override public void handle(Session session, long pingTime) {
                console.println("Server ping took " + pingTime + "ms");
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
        console.println("PhaseBot starting...", false, Color.RED);
        MinecraftProtocol protocol = null;
        if (VERIFY_USERS) {
            try {
                protocol = new MinecraftProtocol(USERNAME, PASSWORD, false);
                console.println("Successfully authenticated user.");
            }
            catch (AuthenticationException e) {
                console.print("Error > Invalid Credentials at " + configFile.getAbsolutePath(), false, Color.RED);
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
}
