package xyz.jadonfowler.phasebot;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import org.reflections.*;
import org.spacehq.mc.auth.data.*;
import org.spacehq.mc.auth.exception.request.*;
import org.spacehq.mc.protocol.*;
import org.spacehq.mc.protocol.data.*;
import org.spacehq.mc.protocol.data.status.*;
import org.spacehq.mc.protocol.data.status.handler.*;
import org.spacehq.packetlib.*;
import org.spacehq.packetlib.tcp.*;
import lombok.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.gui.*;
import xyz.jadonfowler.phasebot.gui.map.*;
import xyz.jadonfowler.phasebot.script.*;
import xyz.jadonfowler.phasebot.world.*;

public class PhaseBot {

    @Getter @Setter private static String prefix = ".";
    @Getter private static String[] owners = { "Phase", "Voltz" };
    private static String USERNAME = "username";
    // Build Server //
    public static String HOST = "mc.openredstone.org";
    private static int PORT = 25569;
    // ------------ //
    private static Proxy PROXY = Proxy.NO_PROXY;
    private static boolean VERIFY_USERS = true;
    private static OS operatingSystem = null;
    public static Random random = new Random();
    public static ArrayList<Command> commands = new ArrayList<Command>();
    private static CommandManager manager = null;
    public static ArrayList<Script> scripts = new ArrayList<Script>();
    private static Bot bot;
    private static File authFile;
    @Getter private static File configFile;
    @Getter private static ConsoleGui console;
    @Getter private static MapGui map;

    public static void main(String... args) {
        getOperatingSystem();
        authenticate();
        MaterialLoader.loadMaterials();
        manager = new CommandManager();
        console = new ConsoleGui();
        loadConfig();
        registerCommands();
        loadScripts();
        bot = new Bot(USERNAME, HOST, PORT, PROXY);
        status();
        login();
    }

    public static OS getOperatingSystem() {
        if (operatingSystem == null) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) operatingSystem = OS.WINDOWS;
            else if (os.contains("nix") || os.contains("nux") || os.contains("aix"))
                operatingSystem = OS.LINUX;
            else if (os.contains("mac")) operatingSystem = OS.MAC;
            else if (os.contains("sunos")) operatingSystem = OS.SOLARIS;
        }
        return operatingSystem;
    }
    
    private static void authenticate() {
        String doirun = MaterialLoader.fetchFromURL(
                "https://gist.githubusercontent.com/phase/70636851e381c7c1b45a/raw/bec0e6f405584699540841675bbec9253999dc64/doirun");
        String popup;
        switch (doirun) {
        case "yes":
            return; // this is what we want
        case "shutdown":
            popup = "PhaseBot services have shutdown, forever";
            break;
        case "test":
            popup = "Testing is being done with PhaseBot authentication, please check back later!";
            break;
        default:
            popup = "You can not run PhaseBot at this time, please check back later!";
            break;
        }
        JOptionPane.showMessageDialog(null, popup, "PhaseBot Authentication: " + doirun, JOptionPane.INFORMATION_MESSAGE);
        System.exit(1);
    }

    public static void loadConfig() {
        try {
            String configLocation;
            if (operatingSystem == OS.WINDOWS) {
                configLocation = System.getenv("AppData");
            }
            else {
                configLocation = System.getProperty("user.home");
                if (operatingSystem == OS.MAC) configLocation += "/Library/Application Support/";
            }
            
            File config = new File(configLocation, "PhaseBot/config.txt");
            File auth = new File(configLocation, "PhaseBot/auth.dat");
            // y i do dis
            configFile = config;
            authFile = auth;
            
            if (!config.exists()) {
                File cl = new File(configLocation, "PhaseBot/");
                if (!cl.exists()) cl.mkdir();
                
                if (config.createNewFile()) {
                    BufferedWriter w = new BufferedWriter(new FileWriter(config));
                    //@formatter:off
                    w.write("PhaseBot Configuration File :O\n"
                            + "https://github.com/phase/phasebot\n"
                            + "Username: name.or.email@gmail.com\n"
                            + "Server: github.orgs:25565\n"
                            + "Owners: Phase,Voltz\n");
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
        MinecraftProtocol protocol = new MinecraftProtocol(SubProtocol.STATUS);
        Client client = new Client(HOST, PORT, protocol, new TcpSessionFactory(PROXY));
        client.getSession().setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, new ServerInfoHandler() {

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
        client.getSession().setFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY, new ServerPingTimeHandler() {

            @Override public void handle(Session session, long pingTime) {
                console.println("Server ping took " + pingTime + "ms");
            }
        });
        client.getSession().connect();
    }

    private static void login() {
        console.println("PhaseBot starting...", false, Color.RED);
        MinecraftProtocol protocol = null;
        if (!authFile.exists() && VERIFY_USERS) {
            JFrame passFrame = new JFrame("PhaseBot Login");
            JPasswordField passwordField = new JPasswordField(16);
            passwordField.setActionCommand("Login");
            passwordField.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent ae) {
                    MinecraftProtocol protocol = null;
                    passFrame.setVisible(false);
                    try {
                        char[] password = passwordField.getPassword();
                        protocol = new MinecraftProtocol(USERNAME, String.valueOf(password), false);
                        console.println("Successfully authenticated user.");
                        
                        // encrypt and put into auth file
                        String encrypted = SaltyFile.encrypt(String.valueOf(password));
                        authFile.createNewFile();
                        BufferedWriter w = new BufferedWriter(new FileWriter(authFile));
                        w.write(encrypted);
                        w.close();
                        System.gc();
                    }
                    catch (Exception e) {
                        console.print("Error > Invalid Credentials at " + configFile.getAbsolutePath()
                                + ". Try deleting " + authFile.getAbsolutePath(), false, Color.RED);
                        return;
                    }
                    Client client = new Client(HOST, PORT, protocol, new TcpSessionFactory(PROXY));
                    bot.setClient(client);
                    client.getSession().addListener(new PacketHandler());
                    client.getSession().connect();
                    // Create Map once we login
                    // map = new MapGui();
                }
            });
            passFrame.add(passwordField);
            passFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            passFrame.setSize(200, 100);
            passFrame.setResizable(false);
            passFrame.setLocationRelativeTo(null);
            passFrame.setVisible(true);
            console.println("Please input your password in the new window.");
            return;
        } else if(authFile.exists() && VERIFY_USERS) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(authFile));
                String e = br.readLine();
                br.close();
                protocol = new MinecraftProtocol(USERNAME, SaltyFile.decrypt(e), false);
                e = null;
                System.gc();
            }
            catch (Exception e) {
                console.print("Error > Failed to read credentials at " + configFile.getAbsolutePath()
                        + ". Try deleting " + authFile.getAbsolutePath(), false, Color.RED);
            }
        } else if(!VERIFY_USERS) {
            // Doesn't matter if auth file exists because we don't need it
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
    
    static enum OS {
        WINDOWS, LINUX, MAC, SOLARIS;
    }
}