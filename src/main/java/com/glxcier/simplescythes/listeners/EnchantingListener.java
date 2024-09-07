package com.glxcier.simplescythes.listeners;

import com.glxcier.simplescythes.tools.ScytheHandler;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import java.util.Map;
import java.util.Random;

/**
 * Handles the enchanting of scythes.
 */
public class EnchantingListener implements Listener {

    private final ScytheHandler scytheHandler;

    public EnchantingListener(ScytheHandler scytheHandler) {
        this.scytheHandler = scytheHandler;
    }

    /**
     * Removes the efficiency enchantment from the possible list of enchantments for a scythe.
     */
    @EventHandler
    public void onPrepareEnchant(PrepareItemEnchantEvent e) {
        if (!scytheHandler.isScythe(e.getItem())) {
            return;
        }

        EnchantmentOffer[] offers = e.getOffers();

        for (int i = 0; i < offers.length; i++) {
            EnchantmentOffer offer = offers[i];
            if (offer.getEnchantment().equals(Enchantment.DIG_SPEED)) {
                offers[i] = rollNewOffer(offer.getEnchantmentLevel(), offer.getCost());
            }
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent e) {
        if (!scytheHandler.isScythe(e.getItem())) {
            return;
        }

        Map<Enchantment, Integer> enchantMap = e.getEnchantsToAdd();

        // Ensures that the hinted enchant is applied to the item
        if (!enchantMap.containsKey(e.getEnchantmentHint()) || enchantMap.get(e.getEnchantmentHint()) != e.getLevelHint()) {
            enchantMap.put(e.getEnchantmentHint(), e.getLevelHint());
        }

        // Removes or replaces efficiency enchantment
        if (enchantMap.containsKey(Enchantment.DIG_SPEED)) {
            int previousLevel = enchantMap.get(Enchantment.DIG_SPEED);
            enchantMap.remove(Enchantment.DIG_SPEED);

            Enchantment enchantment;

            if (enchantMap.containsKey(Enchantment.DURABILITY) && enchantMap.containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
                return;
            } else if (enchantMap.containsKey(Enchantment.DURABILITY) && !enchantMap.containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
                enchantment = Enchantment.LOOT_BONUS_BLOCKS;
            } else if (!enchantMap.containsKey(Enchantment.DURABILITY) && enchantMap.containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
                enchantment = Enchantment.DURABILITY;
            } else {
                Random random = new Random();
                double roll = random.nextDouble();
                if (roll < 0.75) {
                    enchantment = Enchantment.DURABILITY;
                } else {
                    enchantment = Enchantment.LOOT_BONUS_BLOCKS;
                }
            }

            enchantMap.put(enchantment, (int) Math.ceil(previousLevel / 2.0));
        }
    }

    /**
     * Determines a new enchantment offer for a scythe based on random chance.
     * @param previousLevel The level of the previous offer.
     * @param cost The enchantment cost
     * @return A new, valid scythe enchantment offer.
     */
    private EnchantmentOffer rollNewOffer(int previousLevel, int cost) {
        int newLevel = (int) Math.ceil(previousLevel / 2.0);

        Random random = new Random();

        double roll = random.nextDouble();

        Enchantment enchantment;
        if (roll < 0.75) {
            enchantment = Enchantment.DURABILITY;
        } else {
            enchantment = Enchantment.LOOT_BONUS_BLOCKS;
        }

        return new EnchantmentOffer(enchantment, newLevel, cost);
    }

}
