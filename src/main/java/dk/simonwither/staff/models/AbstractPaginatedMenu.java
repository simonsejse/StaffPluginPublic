package dk.simonwither.staff.models;

import dk.simonwither.staff.StaffPlugin;
import dk.simonwither.staff.menus.InventoryDecorate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.function.IntPredicate;

public abstract class AbstractPaginatedMenu implements InventoryHolder {

    private final StaffPlugin staffPlugin;

    protected int currentPage = 1;

    public AbstractPaginatedMenu(StaffPlugin staffPlugin){
        this.staffPlugin = staffPlugin;
    }

    public abstract InventoryDetails inventoryDetails();
    public abstract ItemStack[] itemStacks();
    public abstract void performOnClick(Player whoClicked, int slotClicked);

    public Inventory initializeInventory(int page){
        this.currentPage = page;
        final int offset = inventoryDetails().getOffset();
        final int availableSlots = inventoryDetails().getAvailableSlots();
        final int backPageSlot = inventoryDetails().getBackPageSlot();
        final int nextPageSlot = inventoryDetails().getNextPageSlot();
        final IntPredicate predicate = inventoryDetails().getDecoration().negate().and(t -> t != backPageSlot).and(t -> t != nextPageSlot);
        final int inventorySize = inventoryDetails().getSize();
        Inventory newInventory = Bukkit.createInventory(this, inventorySize, inventoryDetails().getMenuName());
        InventoryDecorate.decorate(newInventory, inventoryDetails().getDecoration(), inventoryDetails().getDecorationItem());
        for(int slot = 0 + offset, currentIndex = availableSlots * (page - 1); slot < inventorySize && currentIndex < itemStacks().length; slot++) {
            if (!predicate.test(slot)) continue;
            newInventory.setItem(slot, itemStacks()[currentIndex++]);
        }
        addPageIndicators(newInventory);
        return newInventory;
    }



    protected void addPageIndicators(Inventory inventory){
        boolean isMorePages = itemStacks().length > getCurrentPage() * inventoryDetails().getAvailableSlots();

        ItemStack nextPageItem, backPageItem;
        if (!isMorePages) nextPageItem = InventoryDecorate.NEXT_PAGE_ITEM.setMaterial(Material.BARRIER).setItemName("&cNo more pages...").setLore("&7No more pages for the menu...").buildItem();
        else nextPageItem = InventoryDecorate.NEXT_PAGE_ITEM.setMaterial(Material.DARK_OAK_BUTTON).setItemName("&aNext page&2&l»").setLore("&7Click here to go to the next page..").buildItem();
        if (getCurrentPage() == 1) backPageItem = InventoryDecorate.PREVIOUS_PAGE_ITEM.setMaterial(Material.BARRIER).setItemName("&4Du kan ikke gå længere tilbage").setLore("&cDu er allerede på den første side", "&cdu kan ikke komme længere tilbage!").buildItem();
        else backPageItem = InventoryDecorate.PREVIOUS_PAGE_ITEM.setMaterial(Material.DARK_OAK_BUTTON).setItemName("&4&l« &4Previous page").setLore("&cClick here to go to the previous page..").buildItem();

        inventory.setItem(this.inventoryDetails().getBackPageSlot(), backPageItem);
        inventory.setItem(this.inventoryDetails().getNextPageSlot(), nextPageItem);
    }

    public StaffPlugin getStaffPlugin() {
        return staffPlugin;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
