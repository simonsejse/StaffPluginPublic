package dk.simonwither.staff.menus;

import dk.simonwither.staff.StaffPlugin;
import dk.simonwither.staff.models.AbstractPaginatedMenu;
import dk.simonwither.staff.models.InventoryDetails;
import dk.simonwither.staff.models.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HelpMenu extends AbstractPaginatedMenu {

    private final static ItemStack helpItem = new ItemBuilder(Material.LANTERN).setItemName("&4✯&b&l §c§lHelp Command &4✯").setLore("", "&8[&c1:&4&l ♛&8] &7➤ &c/staff menu", "&8&m&l——————————————", "&eCurrent menu").buildItem();
    private final static ItemStack addItem = new ItemBuilder(Material.SUNFLOWER).setItemName("&6✯&b&l §e§lAdd Command &6✯").setLore("", "&8[&e1:&6&l ♛&8] &7➤ &e/staff add <user> <rank> <age> <desc>", "&8&m&l——————————————", "&7Parameters:", "&6&l - &6<user>§8 - §ename of user", "&6&l - &6<rank>§8 - §erank of user", "&6&l - &6<age>§8 - §eage of user", "&6&l - &6<desc>§8 - §esmall motto for user").buildItem();
    private final static ItemStack removeItem = new ItemBuilder(Material.DARK_OAK_DOOR).setItemName("&4✯&b&l §c§lRemove Command &4✯").setLore("", "&8[&c1:&4&l ♛&8] &7➤ &c/staff remove <user>", "&8&m&l——————————————", "&7Parameters:", "&4&l - &c<user>§8 - §cname of user you", "§cwant to remove from staff team!").buildItem();
    private final static ItemStack listItem = new ItemBuilder(Material.EGG).setItemName("&2✯&b&l §a§lList Menu &2✯").setLore("", "&8[&a1:&2&l ♛&8] &7➤ &a/staff", "&8&m&l——————————————", "&6&l - &eOpens a UI with all staff").buildItem();
    private final static ItemStack changelogItem = new ItemBuilder(Material.EMERALD).setItemName("&9✯&b&l Changelogs &9✯").setLore("&7Release version: 1.0", "&7Current version: 1.0", "", "&8[&91:&b&l ♛&8] &7➤ &9No changes...", "&8&m&l——————————————", "&7Future changes:", "&6&l - &eNone..").buildItem();


    public HelpMenu(StaffPlugin staffPlugin) {
        super(staffPlugin);
    }

    @Override
    public InventoryDetails inventoryDetails() {
        return InventoryDetails.HELP_MENU;
    }

    @Override
    public ItemStack[] itemStacks() {
        return new ItemStack[]{
                helpItem,
                addItem,
                removeItem,
                listItem,
                changelogItem
        };
    }

    @Override
    public void performOnClick(Player whoClicked, int slotClicked) {

    }

    @Override
    public Inventory getInventory() { return null; }
}
