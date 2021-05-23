package dk.simonwither.staff.service;

import dk.simonwither.staff.models.AbstractPaginatedMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class ClickEventHandling implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e){
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getHolder() == null) return;
        final InventoryHolder holder = e.getClickedInventory().getHolder();
        if (holder instanceof AbstractPaginatedMenu){
            e.setCancelled(true);
            AbstractPaginatedMenu menu = (AbstractPaginatedMenu) holder;
            final int slot = e.getSlot();
            final Player whoClicked = (Player) e.getWhoClicked();
            if (slot == menu.inventoryDetails().getBackPageSlot()){
                if (menu.getCurrentPage() > 1){
                    whoClicked.openInventory(menu.initializeInventory(menu.getCurrentPage() - 1));
                } else whoClicked.sendMessage("§cNo previous pages.");
            }else if (slot == menu.inventoryDetails().getNextPageSlot()){
                if (menu.itemStacks().length > menu.getCurrentPage() * menu.inventoryDetails().getAvailableSlots()) {
                    whoClicked.openInventory(menu.initializeInventory(menu.getCurrentPage() + 1));
                } else whoClicked.sendMessage("§cNo more pages.");
            }else menu.performOnClick(whoClicked, slot);

        }
    }

}
