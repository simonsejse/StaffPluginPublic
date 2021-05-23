package dk.simonwither.staff.models;

import java.util.function.IntPredicate;

public enum InventoryDetails {
    MAIN_MENU("Staff", t -> t < 10 || t > 16 && t < 19 || t > 25 && t < 28 || t > 34 && t < 37 || t > 43 && t != 49,10, 26, 9*6, 43,37);

    private final String menuName;
    private final IntPredicate decoration;
    private final int offset, availableSlots, size, nextPageSlot, backPageSlot;

    InventoryDetails(String menuName, IntPredicate decoration, int offset, int availableSlots, int size, int nextPageSlot, int backPageSlot) {
        this.menuName = menuName;
        this.decoration = decoration;
        this.offset = offset;
        this.availableSlots = availableSlots;
        this.size = size;
        this.nextPageSlot = nextPageSlot;
        this.backPageSlot = backPageSlot;
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
}
