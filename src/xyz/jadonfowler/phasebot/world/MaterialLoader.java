package xyz.jadonfowler.phasebot.world;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import xyz.jadonfowler.phasebot.world.material.*;

public class MaterialLoader {

    public static final String BLOCKS_URL = "https://raw.githubusercontent.com/PrismarineJS/minecraft-data/1.8/enums/blocks.json";
    public static final String ITEMS_URL = "https://raw.githubusercontent.com/PrismarineJS/minecraft-data/1.8/enums/items.json";
    private static final JSONParser parser = new JSONParser();

    public static void loadMaterials() {
        loadBlocks();
        loadItems();
    }

    public static void loadItems() {
        try {
            JSONArray items = getItemsJSON();
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                ItemType.ItemTypeBuilder itemBuilder = ItemType.builder();
                itemBuilder.id((long) item.get("id"));
                itemBuilder.displayName(item.get("displayName").toString());
                itemBuilder.name(item.get("name").toString());
                itemBuilder.stackSize((long) item.get("stackSize"));
                /* @formatter:off
                 * TODO Item Attributes not stored:
                 * - variations
                 * @formatter:on
                 */
                itemBuilder.build();
                // System.out.println(Material.getItem(item.get("name").toString()).toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadBlocks() {
        try {
            JSONArray blocks = getBlocksJSON();
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
                /* @formatter:off
                 * TODO Block Attributes not stored:
                 * - harvestTools 
                 * - variations
                 * - drops
                 * @formatter:on
                 */
                blockBuilder.build();
                // System.out.println(Material.getBlock(block.get("name").toString()).toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONArray getBlocksJSON() throws ParseException {
        return (JSONArray) parser.parse(fetchBlocks());
    }

    private static JSONArray getItemsJSON() throws ParseException {
        return (JSONArray) parser.parse(fetchItems());
    }

    private static String fetchItems() {
        return fetchFromURL(ITEMS_URL);
    }

    private static String fetchBlocks() {
        return fetchFromURL(BLOCKS_URL);
    }

    private static String fetchFromURL(String url) {
        String all;
        try {
            InputStream is = new URL(url).openStream();
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
