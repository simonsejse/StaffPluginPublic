package dk.simonwither.staff.menus;

import dk.simonwither.staff.models.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class InventoryDecorate {

    public static final ItemBuilder NEXT_PAGE_ITEM = new ItemBuilder();
    public static final ItemBuilder PREVIOUS_PAGE_ITEM = new ItemBuilder();

    public static void decorate(Inventory inventory, IntPredicate intPredicate, Material material){
        IntStream.range(0, inventory.getSize())
                .filter(intPredicate)
                .forEach(slot -> inventory.setItem(slot, new ItemStack(material)));
    }
}
