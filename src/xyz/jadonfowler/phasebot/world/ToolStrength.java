package xyz.jadonfowler.phasebot.world;

import static xyz.jadonfowler.phasebot.world.Materials.*;
import java.util.*;
import lombok.*;

public enum ToolStrength {/*
    //@formatter:off
    WOOD(2f, WOOD_SWORD, WOOD_PICKAXE, WOOD_AXE, WOOD_SPADE, WOOD_HOE),
    STONE(4f, STONE_SWORD, STONE_PICKAXE, STONE_AXE, STONE_SPADE, STONE_HOE),
    IRON(6f, IRON_SWORD, IRON_PICKAXE, IRON_AXE, IRON_SPADE, IRON_HOE),
    DIAMOND(8f, DIAMOND_SWORD, DIAMOND_PICKAXE, DIAMOND_AXE, DIAMOND_SPADE, DIAMOND_HOE),
    GOLD(12f, GOLD_SWORD, GOLD_PICKAXE, GOLD_AXE, GOLD_SPADE, GOLD_HOE);
    //@formatter:on

    @Getter float strength;
    @Getter List<Materials> materials;

    ToolStrength(float strength, Materials... materials) {
        this.strength = strength;
        this.materials = Arrays.asList(materials);
    }

    public static float getEffectiveness(Materials tool) {
        if (WOOD.materials.contains(tool)) return WOOD.strength;
        else if (STONE.materials.contains(tool)) return STONE.strength;
        else if (IRON.materials.contains(tool)) return IRON.strength;
        else if (DIAMOND.materials.contains(tool)) return DIAMOND.strength;
        else if (GOLD.materials.contains(tool)) return GOLD.strength;
        return 0f;
    }

    public static double getWaitTime(Materials tool, Materials block, boolean underwater, boolean onGround) {
        double time = 0;
        while (time < 1)
            time += strengthAgainstBlock(tool, block, underwater, onGround);
        return time;
    }

    public static double strengthAgainstBlock(Materials tool, Materials block, boolean underwater, boolean onGround) {
        if (block.getHardness() < 0) return 0;
        if (!canHarvest(tool, block)) return 1 / block.getHardness() / 100;
        double d = 1;
        if (isEffectiveAgainst(tool, block)) d *= getEffectiveness(tool);
        if (underwater) d /= 5;
        if (!onGround) d /= 5;
        return d / block.getHardness() / 30;
    }

    public static boolean canHarvest(Materials tool, Materials block) {
        if (!block.isSolid() && block.isBlock()) return true;
        else return isEffectiveAgainst(tool, block);
    }

    public static boolean isEffectiveAgainst(Materials tool, Materials block) {
        switch (tool) {
        case WOOD_SPADE:
        case STONE_SPADE:
        case IRON_SPADE:
        case DIAMOND_SPADE:
        case GOLD_SPADE:
            switch (block) {
            case GRASS:
            case DIRT:
            case SAND:
            case GRAVEL:
            case SNOW:
            case SNOW_BLOCK:
            case CLAY:
                return true;
            default:
                return false;
            }
        case WOOD_AXE:
        case STONE_AXE:
        case IRON_AXE:
        case DIAMOND_AXE:
        case GOLD_AXE:
            switch (block) {
            case WOOD:
            case BOOKSHELF:
            case LOG:
            case LOG_2:
                return true;
            default:
                return false;
            }
            // Everything below this line can be made better
            // Level 0
        case WOOD_PICKAXE:
        case GOLD_PICKAXE:
            // Level 1
        case STONE_PICKAXE:
            // Level 2
        case IRON_PICKAXE:
            // Level 3
        case DIAMOND_PICKAXE:
            switch (block) {
            // Level 0
            case STONE:
            case COBBLESTONE:
            case COBBLESTONE_STAIRS:
            case COBBLE_WALL:
            case SANDSTONE:
            case SANDSTONE_STAIRS:
            case MOSSY_COBBLESTONE:
            case COAL_BLOCK:
            case REDSTONE_BLOCK:
            case QUARTZ_BLOCK:
                return true;
            // Level 1
            case IRON_ORE:
            case IRON_BLOCK:
            case LAPIS_BLOCK:
            case LAPIS_ORE:
                switch (tool) {
                case STONE_PICKAXE:
                case IRON_PICKAXE:
                case DIAMOND_PICKAXE:
                    return true;
                default:
                    return false;
                }
                // Level 2
            case DIAMOND_ORE:
            case DIAMOND_BLOCK:
            case GOLD_ORE:
            case GOLD_BLOCK:
            case REDSTONE_ORE:
                switch (tool) {
                case IRON_PICKAXE:
                case DIAMOND_PICKAXE:
                    return true;
                default:
                    return false;
                }
                // Level 3
            case OBSIDIAN:
                switch (tool) {
                case DIAMOND_PICKAXE:
                    return true;
                default:
                    return false;
                }
            default:
            }
        default:
            return false;
        }
    }*/
}