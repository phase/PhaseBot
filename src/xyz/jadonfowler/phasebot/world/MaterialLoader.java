package xyz.jadonfowler.phasebot.world;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import xyz.jadonfowler.phasebot.world.material.*;

public class MaterialLoader {

    public static final String BLOCKS_URL = "https://raw.githubusercontent.com/PrismarineJS/minecraft-data/1.8/enums/blocks.json";

    public static void loadMaterials() {
        try {
            final String blocksJSON = fetchBlocks();
            JSONParser parser = new JSONParser();
            JSONArray blocks = (JSONArray) parser.parse(blocksJSON);
            for (int i = 0; i < blocks.size(); i++) {
                JSONObject block = (JSONObject) blocks.get(i);
                BlockType.BlockTypeBuilder blockBuilder = BlockType.builder();
                blockBuilder.id((long) block.get("id"));
                blockBuilder.displayName(block.get("displayName").toString());
                blockBuilder.name(block.get("name").toString());
                if (block.get("hardness") != null) {
                    try {
                        blockBuilder.hardness((double) block.get("hardness"));
                    }
                    catch (ClassCastException e) {
                        blockBuilder.hardness((double) ((long) block.get("hardness")));
                    }
                }
                else {
                    // Should be null, but nulls are weird in Java
                    blockBuilder.hardness(999d);
                }
                blockBuilder.stackSize((long) block.get("stackSize"));
                blockBuilder.diggable((boolean) block.get("diggable"));
                blockBuilder.boundingBox(block.get("boundingBox").toString());
                try {
                    blockBuilder.material(block.get("material").toString());
                }
                catch (Exception e) {
                    // Silent catch as some blocks don't have
                }
                blockBuilder.build();
                System.out.println(Material.getBlock(block.get("name").toString()).toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fetchBlocks() {
        String all;
        try {
            InputStream is = new URL(BLOCKS_URL).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            all = readAll(rd);
            rd.close();
            is.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return all;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
