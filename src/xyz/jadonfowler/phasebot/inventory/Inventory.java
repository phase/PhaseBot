package xyz.jadonfowler.phasebot.inventory;

public class Inventory {

    @Getter ItemStack[] items = new ItemStack[44];
    
    public Inventory(ItemStack[] i) {
        for(int j = 0; j < i.length; j++) {
            items[j] = i[j];
        }
    }

    public ItemStack getItem(int i){ return items[i]; }

}
