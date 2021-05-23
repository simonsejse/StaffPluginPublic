package dk.simonwither.staff.menus;

import dk.simonwither.staff.StaffPlugin;
import dk.simonwither.staff.models.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StaffListMenu extends AbstractPaginatedMenu {

    private final List<String> UILoreConfiguration;
    private String UINameConfiguration;

    public StaffListMenu(StaffPlugin staffPlugin){
        super(staffPlugin);
        this.UILoreConfiguration = staffPlugin.getConfiguration().UILoreConfiguration;
        this.UINameConfiguration = staffPlugin.getConfiguration().UINameConfiguration;
    }

    @Override
    public InventoryDetails inventoryDetails() {
        return InventoryDetails.MAIN_MENU;
    }

    @Override
    public ItemStack[] itemStacks() {
        return super.getStaffPlugin()
                .getStaffManager()
                .getStaffs()
                .entrySet()
                .stream()
                .sorted(((o1, o2) -> {
                    final int priority = o1.getValue().getRank().getPriority();
                    final int priority1 = o2.getValue().getRank().getPriority();
                    return priority-priority1;
                }))
                .map(entry -> {
                    final StaffData value = entry.getValue();
                            return new ItemBuilder()
                                    .setPlayerSkull(entry.getKey())
                                    .setItemName(this.UINameConfiguration.replace("{rank}", value.getRank().getName()).replace("{username}", value.getUsername()).replace("{desc}", value.getDescription()).replace("{age}", String.valueOf(value.getAge())))
                                    .setLore(executeUnaryOperator(value, this.UILoreConfiguration))
                                    .buildItem();
                        }
                ).toArray(ItemStack[]::new);
    }

    public String[] executeUnaryOperator(final StaffData staffData, List<String> listForUnaryOperator){
        final List<String> list = listForUnaryOperator;
        list.replaceAll(new UILoreReplacement(staffData));
        return list.toArray(new String[list.size()]);
    }

    @Override
    public void performOnClick(Player whoClicked, int slotClicked) {
        whoClicked.sendMessage("You clicked inventory!");
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
