package xyz.jadonfowler.phasebot.inventory;

import lombok.Getter;
import lombok.Setter;
import org.spacehq.mc.protocol.data.game.ItemStack;

public class Inventory {

    @Getter ItemStack[] items;
    @Getter @Setter int heldSlot;

    public Inventory(ItemStack[] i) {
        items = i;
    }

    public ItemStack getItem(int i) {
        return items[i];
    }

    public Inventory setItem(int i, ItemStack s) {
        items[i] = s;
        return this;
    }

    public ItemStack getHeldItem() {
        return items[heldSlot + 36 /* Magic value is magic */];
    }
}
