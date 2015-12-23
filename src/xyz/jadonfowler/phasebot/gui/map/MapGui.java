package xyz.jadonfowler.phasebot.gui.map;

import java.awt.*;
import java.awt.image.*;
import java.util.concurrent.*;
import javax.swing.*;
import lombok.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.world.*;
import xyz.jadonfowler.phasebot.world.material.*;

public class MapGui extends JFrame {
    private static final long serialVersionUID = 42l;
    public static final int MAP_SIZE = 400;
    @Getter @Setter private static BufferedImage map;

    public MapGui() {
        setTitle("PhaseBot Map");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(MAP_SIZE, MAP_SIZE);
        setResizable(false);
        setVisible(true);
        createBlankMap();
    }

    public void createBlankMap() {
        map = new BufferedImage(MAP_SIZE, MAP_SIZE, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                map.setRGB(x, y, Color.white.getRGB());
            }
        }
    }

    public void setBlock(Material m, int rx, int ry) {
        int rgb = (int) m.getId(); // TODO: anything but this
        map.setRGB(MAP_SIZE / 2 + rx, MAP_SIZE / 2 + ry, rgb);
    }

    public void updateMap() {
        Bot bot = PhaseBot.getBot();
        for (int y = -5; y < 5; y++) {
            for (int x = -5; x < 5; x++) {
                setBlock(Block.getBlock(bot.pos.x, bot.pos.z).getMaterial(), x, y);
            }
        }
    }

    @Override public void update(Graphics g) {
        paint(g);
    }

    @Override public void paint(Graphics g) {
        updateMap();
        g.drawImage(map, 0, 0, null);
        g.setColor(Color.red);
        g.drawOval(MAP_SIZE / 2, MAP_SIZE / 2, 1, 1);
        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.schedule(new Runnable() {
            @Override public void run() {
                repaint();
            }
        }, 10, TimeUnit.SECONDS);
    }
}
