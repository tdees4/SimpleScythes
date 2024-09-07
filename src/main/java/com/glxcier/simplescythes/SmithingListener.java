package com.glxcier.simplescythes;

import com.glxcier.simplescythes.tools.ScytheHandler;
import com.glxcier.simplescythes.tools.ScytheMaterial;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

public class SmithingListener implements Listener {

    private final ScytheHandler scytheHandler;

    public SmithingListener(ScytheHandler scytheHandler) {
        this.scytheHandler = scytheHandler;
    }

    @EventHandler
    public void onSmithingPrepare(PrepareSmithingEvent e) {

        SmithingInventory inventory = e.getInventory();

        ItemStack upgradeItemSlot = inventory.getItem(0);
        ItemStack baseItemSlot = inventory.getItem(1);
        ItemStack additionItemSlot = inventory.getItem(2);

        if (upgradeItemSlot == null || baseItemSlot == null || additionItemSlot == null) {
            return;
        }

        if (upgradeItemSlot.getType() != Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE) {
            return;
        }

        if (!scytheHandler.isScythe(baseItemSlot)) {
            return;
        }

        if (additionItemSlot.getType() != Material.NETHERITE_INGOT) {
            return;
        }

        e.setResult(scytheHandler.getScythe(ScytheMaterial.NETHERITE));
    }

}
