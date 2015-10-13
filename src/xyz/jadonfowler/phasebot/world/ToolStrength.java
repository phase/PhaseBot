package xyz.jadonfowler.phasebot.world;

import java.util.*;
import lombok.*;
import xyz.jadonfowler.phasebot.world.material.*;

public enum ToolStrength {
    //@formatter:off
    WOOD(2f, Material.getMaterials("wood_sword", "wood_pickaxe", "wood_axe", "wood_spade", "wood_hoe")),
    STONE(4f, Material.getMaterials("stone_sword", "stone_pickaxe", "stone_axe", "stone_spade", "stone_hoe")),
    IRON(6f, Material.getMaterials("iron_sword", "iron_pickaxe", "iron_axe", "iron_spade", "iron_hoe")),
    DIAMOND(8f, Material.getMaterials("diamond_sword", "diamond_pickaxe", "diamond_axe", "diamond_spade", "diamond_hoe")),
    GOLD(12f, Material.getMaterials("gold_sword", "gold_pickaxe", "gold_axe", "gold_spade", "gold_hoe"));
    //@formatter:on

    @Getter private static final List<String> level0Stones = Arrays
            .asList(new String[] { "stone", "cobblestone", "double_stone_slab", "stone_slab", "mossy_cobblestone",
                    "stone_stairs", "monster_egg", "cobblestone_wall", "sandstone", "stone_pressure_plate", "glowstone",
                    "stonebrick", "stone_brick_stairs", "sandstone_stairs", "cobblestone_wall", "redstone_block",
                    "quartz_block", "red_sandstone", "red_sandstone_stairs", "double_stone_slab2", "stone_slab2" });
    @Getter private static final List<String> level1Stones = Arrays
            .asList(new String[] { "iron_ore", "iron_block", "lapis_ore", "lapis_block" });
    @Getter private static final List<String> level2Stones = Arrays.asList(new String[] { "diamond_ore",
            "diamond_block", "gold_ore", "gold_block", "redstone_ore", "lit_redstone_ore" });
    @Getter private static final List<String> level3Stones = Arrays.asList(new String[] { "obsidian" });
    @Getter private static final List<String> level0Picks = Arrays.asList(
            new String[] { "wood_pickaxe", "gold_pickaxe", "stone_pickaxe", "iron_pickaxe", "diamond_pickaxe" });
    @Getter private static final List<String> level1Picks = Arrays
            .asList(new String[] { "stone_pickaxe", "iron_pickaxe", "diamond_pickaxe" });
    @Getter private static final List<String> level2Picks = Arrays
            .asList(new String[] { "iron_pickaxe", "diamond_pickaxe" });
    @Getter private static final List<String> level3Picks = Arrays.asList(new String[] { "diamond_pickaxe" });
    @Getter float strength;
    @Getter List<Material> materials;

    ToolStrength(float strength, Material... materials) {
        this.strength = strength;
        this.materials = Arrays.asList(materials);
    }

    public static float getEffectiveness(Material tool) {
        if (WOOD.materials.contains(tool)) return WOOD.strength;
        else if (STONE.materials.contains(tool)) return STONE.strength;
        else if (IRON.materials.contains(tool)) return IRON.strength;
        else if (DIAMOND.materials.contains(tool)) return DIAMOND.strength;
        else if (GOLD.materials.contains(tool)) return GOLD.strength;
        return 0f;
    }

    public static double getWaitTime(ItemType tool, BlockType block, boolean underwater, boolean onGround) {
        double time = 0;
        while (time < 1)
            time += strengthAgainstBlock(tool, block, underwater, onGround);
        return time * 1000;
    }

    public static double strengthAgainstBlock(ItemType tool, BlockType block, boolean underwater, boolean onGround) {
        return block.getHardness() * 5;
        /*if (block.getHardness() < 0) return 0;
        if (!canHarvest(tool, block)) return (1 / block.getHardness()) / 100;
        double d = 1;
        if (isEffectiveAgainst(tool, block)) d *= getEffectiveness(tool);
        if (underwater) d /= 5;
        if (!onGround) d /= 5;
        return (d / block.getHardness()) / 30;*/
    }

    public static boolean canHarvest(ItemType tool, BlockType block) {
        if (!block.isSolid()) return true;
        else return isEffectiveAgainst(tool, block);
    }

    public static boolean isEffectiveAgainst(ItemType tool, BlockType block) {
        if (!tool.getName().contains("_")) return false;
        if (tool.getName().endsWith("_spade")) {
            if (block.getMaterial().equals("dirt")) return true;
            else return false;
        }
        else if (tool.getName().endsWith("_axe")) {
            if (block.getMaterial().equals("wood")) return true;
            else return false;
        }
        else if (tool.getName().endsWith("_pickaxe")) {
            if (!block.getMaterial().equals("rock")) return false;
            if (tool.getName().equals("diamond_pick")) return true;
            if (level0Stones.contains(block.getName())) return true;
            if (level1Stones.contains(block.getName()) && level1Picks.contains(tool.getName())) return true;
            if (level2Stones.contains(block.getName()) && level2Picks.contains(tool.getName())) return true;
            if (level3Stones.contains(block.getName()) && level3Picks.contains(tool.getName())) return true;
        }
        return false;
    }
}