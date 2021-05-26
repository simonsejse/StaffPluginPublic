package dk.simonwither.staff.models;

import org.bukkit.Material;

import java.util.function.IntPredicate;

public enum InventoryDetails {
    MAIN_MENU("Staff", t -> t < 13 || t > 13 && t < 21 || t > 23 && t < 30 || t > 32,13, 7, 9*5, 34,28, Material.GREEN_STAINED_GLASS_PANE),
    HELP_MENU("Help", t -> t < 12 || t > 14,13, 1, 9*3, 14,12, Material.BLUE_STAINED_GLASS_PANE);

    private final String menuName;
    private final IntPredicate decoration;
    private final int offset, availableSlots, size, nextPageSlot, backPageSlot;
    private final Material decorationMaterial;

    InventoryDetails(String menuName, IntPredicate decoration, int offset, int availableSlots, int size, int nextPageSlot, int backPageSlot, Material decorationMaterial) {
        this.menuName = menuName;
        this.decoration = decoration;
        this.offset = offset;
        this.availableSlots = availableSlots;
        this.size = size;
        this.nextPageSlot = nextPageSlot;
        this.backPageSlot = backPageSlot;
        this.decorationMaterial = decorationMaterial;
    }

    public String getMenuName() {
        return menuName;
    }

    public IntPredicate getDecoration() {
        return decoration;
    }

    public int getOffset() {
        return offset;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public int getSize() {
        return size;
    }

    public int getNextPageSlot() {
        return nextPageSlot;
    }

    public int getBackPageSlot() {
        return backPageSlot;
    }

    public Material getDecorationItem() {
        return decorationMaterial;
    }
}
