package dk.simonwither.staff.models;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(Material material)
    {
        itemStack = new ItemStack(material);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack)
    {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder()
    {

    }

    public ItemBuilder setMaterial(Material newMaterial){
        this.itemStack = new ItemStack(newMaterial);
        this.itemMeta = itemStack.getItemMeta();
        return this;
    }

    public ItemBuilder setItemName(String itemName){
        itemMeta.setDisplayName(color(itemName));
        updateMeta();
        return this;
    }

    public ItemBuilder setLore(String... lores){
        for(int i = 0;i<lores.length;i++){
            lores[i] = ChatColor.translateAlternateColorCodes('&', color(lores[i]));
        }
        itemMeta.setLore(Arrays.asList(lores).stream().flatMap((s) -> Stream.of( s.split( "\\r?\\n" ) )).collect(Collectors.toList()));
        updateMeta();
        return this;
    }


    public ItemBuilder setPlayerSkull(UUID offlinePlayerUUID)
    {
        itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(offlinePlayerUUID));
        this.itemMeta = meta;
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setAmount(int amount){
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags){
        itemMeta.addItemFlags(flags);
        updateMeta();
        return this;
    }

    public String color(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private void updateMeta(){
        itemStack.setItemMeta(itemMeta);
    }

    public ItemStack buildItem(){
        return itemStack;
    }
}
