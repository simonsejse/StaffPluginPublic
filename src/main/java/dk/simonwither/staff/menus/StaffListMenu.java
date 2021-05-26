package dk.simonwither.staff.menus;

import dk.simonwither.staff.StaffPlugin;
import dk.simonwither.staff.models.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StaffListMenu extends AbstractPaginatedMenu {

    private String UINameConfiguration;
    private final List<String> UILoreConfiguration;

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
                                    .setLore(executeOperatorForLore(value, this.UILoreConfiguration))
                                    .buildItem();
                        }
                ).toArray(ItemStack[]::new);
    }

    public String[] executeOperatorForLore(StaffData staffData, List<String> list){
        final List<String> tempList = list;
        return tempList.stream().map(s -> s.replace("{username}", staffData.getUsername())
                .replace("{age}", String.valueOf(staffData.getAge()))
                .replace("{rank}", staffData.getRank().getName())
                .replace("{desc}", staffData.getDescription()))
                .toArray(String[]::new);
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
