package com.glxcier.simplescythes.listeners;

import com.glxcier.simplescythes.tools.ScytheHandler;
import com.glxcier.simplescythes.tools.ScytheMaterial;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

/**
 * Implements the repairing of scythes.
 */
public class RepairListener implements Listener {

    private final ScytheHandler scytheHandler;

    public RepairListener(ScytheHandler scytheHandler) {

        this.scytheHandler = scytheHandler;

    }

    /**
     * Allows scythes to be repaired by putting two of the same material
     * scythes in a crafting grid.
     */
    @EventHandler
    public void onCraftRepair(PrepareItemCraftEvent e) {

        CraftingInventory inventory = e.getInventory();
        ItemStack[] matrix = inventory.getMatrix();

        int scytheCount = 0;
        int damage = 0;
        ScytheMaterial sMaterial = null;

        for (ItemStack item : matrix) {
            if (item == null)
                continue;
            if (scytheHandler.isScythe(item)) {
                if (sMaterial == null) {
                    sMaterial = scytheHandler.getScytheMaterial(item);
                    scytheCount++;
                    Damageable itemMeta = (Damageable) item.getItemMeta();
                    damage += item.getType().getMaxDurability() - itemMeta.getDamage();
                } else if (scytheHandler.getScytheMaterial(item) == sMaterial) {
                    scytheCount++;
                    Damageable itemMeta = (Damageable) item.getItemMeta();
                    damage += item.getType().getMaxDurability() - itemMeta.getDamage();
                } else {
                    inventory.setResult(new ItemStack(Material.AIR));
                    return;
                }
            } else {
                inventory.setResult(new ItemStack(Material.AIR));
                return;
            }
        }

        if (scytheCount > 1) {
            ItemStack repairedSycythe = scytheHandler.getScythe(sMaterial);
            Damageable itemMeta = (Damageable) repairedSycythe.getItemMeta();
            itemMeta.setDamage(repairedSycythe.getType().getMaxDurability() - damage);
            repairedSycythe.setItemMeta(itemMeta);
            inventory.setResult(repairedSycythe);
        }

    }

}
