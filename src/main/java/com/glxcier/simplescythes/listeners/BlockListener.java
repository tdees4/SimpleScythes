package com.glxcier.simplescythes.listeners;

import com.glxcier.simplescythes.tools.ScytheHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * Implements the main functionality of scythes: breaking crops using right click.
 */
public class BlockListener implements Listener {

    private final Plugin plugin;
    private final ScytheHandler scytheHandler;
    private final List<Location> brokenCrops;
    private final List<Material> tillableBlocks;

    public BlockListener(Plugin plugin, ScytheHandler scytheHandler) {
        this.plugin = plugin;
        this.scytheHandler = scytheHandler;
        brokenCrops = new ArrayList<>();
        tillableBlocks = Arrays.asList(Material.DIRT, Material.GRASS_BLOCK, Material.DIRT_PATH);
    }

    /**
     * Handles the replanting and drop functionality that comes with scythes.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();

        if (!brokenCrops.contains(block.getLocation()))
            return;

        brokenCrops.remove(block.getLocation());

        e.setDropItems(false);

        Collection<ItemStack> drops = block.getDrops();

        // Removes 1 seed from the drops
        drops.forEach(item -> {
            if (scytheHandler.getSeeds().contains(item.getType())) {
                if (item.getAmount() <= 1) {
                    drops.remove(item);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }
            }
        });

        World world = block.getWorld();

        // Manually drop the items at the location
        drops.forEach(item -> {
            world.dropItemNaturally(block.getLocation(), item);
        });

        // Replant the seed
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            System.out.println("Replanting");
        }, 2L);
    }

    /**
     * Allows the player to right-click with a scythe and till land or harvest crops.
     */
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null ||
                e.getItem() == null ||
                e.getAction() != Action.RIGHT_CLICK_BLOCK ||
                !scytheHandler.isScythe(e.getItem()))
            return;

        Block block = e.getClickedBlock();
        ItemStack scythe = e.getItem();

        // Harvest the crops

        if (scytheHandler.getCrops().contains(block.getType())) {
            harvestCrops(scythe, block.getLocation(), e.getPlayer(), e.getHand());
            return;
        }

        // Prevent tilling land

        if (tillableBlocks.contains(block.getType()) && !plugin.getConfig().getBoolean("scythe-can-till")) {
            e.setCancelled(true);
        }
    }

    /**
     * Harvests crops in a certain radius depending on the type of scythe used.
     * @param scythe The scythe being used.
     * @param clickedLocation The location of the clicked block.
     * @param player The player using the scythe.
     * @param hand The hand the scythe is in. (Main hand or Off hand)
     */
    private void harvestCrops(ItemStack scythe, Location clickedLocation, Player player, EquipmentSlot hand) {
        if (clickedLocation.getWorld() == null) {
            return;
        }

        Ageable age = (Ageable) clickedLocation.getBlock().getBlockData();
        if (age.getAge() != age.getMaximumAge()) {
            return;
        }

        if (hand == EquipmentSlot.HAND) {
            player.swingMainHand();
        } else {
            player.swingOffHand();
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            reduceDurability(scythe, 1);
        }

        int radius = scytheHandler.getRadius(scythe);

        double x = clickedLocation.getX(), y = clickedLocation.getY(), z = clickedLocation.getZ();
        World world = clickedLocation.getWorld();
        world.playSound(clickedLocation, Sound.BLOCK_CROP_BREAK, 1, 1);

        switch (radius) {
            case 4:
                harvestCropAt(player, new Location(world, x + 2, y, z), scythe);
                harvestCropAt(player, new Location(world, x - 2, y, z), scythe);
                harvestCropAt(player, new Location(world, x, y, z + 2), scythe);
                harvestCropAt(player, new Location(world, x, y, z - 2), scythe);
            case 3:
                harvestCropAt(player, new Location(world, x + 1, y, z + 1), scythe);
                harvestCropAt(player, new Location(world, x - 1, y, z + 1), scythe);
                harvestCropAt(player, new Location(world, x + 1, y, z - 1), scythe);
                harvestCropAt(player, new Location(world, x - 1, y, z - 1), scythe);
            case 2:
                harvestCropAt(player, new Location(world, x + 1, y, z), scythe);
                harvestCropAt(player, new Location(world, x - 1, y, z), scythe);
                harvestCropAt(player, new Location(world, x, y, z + 1), scythe);
                harvestCropAt(player, new Location(world, x, y, z - 1), scythe);
            case 1:
                harvestCropAt(player, clickedLocation, scythe);
        }
    }

    /**
     * Harvests crops at a specific block, accounting for the scythe's enchants and re-plant settings.
     * @param player The player using the scythe.
     * @param location The location of the crop (block) being harvested.
     * @param scythe The scythe being used.
     */
    private void harvestCropAt(Player player, Location location, ItemStack scythe) {
        Block block = location.getBlock();
        Material crop = block.getType();
        if (scytheHandler.getCrops().contains(block.getType())) {
            Ageable age = (Ageable) block.getBlockData();
            if (age.getAge() == age.getMaximumAge()) {

                Collection<ItemStack> drops = block.getDrops();

                // Removes 1 seed from the drops if auto replant is enabled
                if(plugin.getConfig().getBoolean("enable-auto-replant")) {
                    for (ItemStack item : drops) {
                        if (scytheHandler.getSeeds().contains(item.getType())) {
                            item.setAmount(item.getAmount() - 1);
                            break;
                        }
                    }
                }

                // Adds drops if the scythe has fortune
                if (scythe.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
                    for (ItemStack item : drops) {
                        if (scytheHandler.getCropItems().contains(item.getType())) {
                            int additional = 0;

                            Random random = new Random();
                            double roll = random.nextDouble();
                            double stack = 0;
                            double[] odds = scytheHandler.getFortuneOdds(scythe.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS));

                            for (int i = 0; i < odds.length; i++) {
                                stack += odds[i];
                                if (stack >= roll) {
                                    additional = i;
                                    break;
                                }
                            }

                            item.setAmount(item.getAmount() + additional);
                        }
                    }
                }

                World world = block.getWorld();

                Particle.DustOptions dust = new Particle.DustOptions(
                        Color.fromRGB(255, 255, 255), 1);
                world.spawnParticle(Particle.REDSTONE, location, 10, 0.5, 0.5, 0.5, 0, dust);

                // Manually drop the items at the location
                drops.forEach(item -> {
                    if (item.getType() != Material.AIR && item.getAmount() > 0) {
                        if (plugin.getConfig().getBoolean("drops-to-inventory")) {
                            player.getInventory().addItem(item);
                        } else {
                            world.dropItemNaturally(block.getLocation(), item);
                        }
                    }
                });

                if (plugin.getConfig().getBoolean("enable-auto-replant"))
                    location.getBlock().setType(crop);
                else
                    location.getBlock().setType(Material.AIR);
            }
        }
    }

    /**
     * Reduces the durability of the item by amount.
     * @param item The item whose durability will be reduced.
     * @param amount The amount the durability will be reduced by.
     */
    private void reduceDurability(ItemStack item, int amount) {
        if (item.getItemMeta() instanceof Damageable) {
            Damageable damageMeta = (Damageable) item.getItemMeta();

            if (!item.containsEnchantment(Enchantment.DURABILITY)) {
                damageMeta.setDamage(amount);
            } else {
                Random random = new Random();
                double roll = random.nextDouble();
                double odds = scytheHandler.getUnbreakingOdds(item.getEnchantmentLevel(Enchantment.DURABILITY));

                if (roll > odds) {
                    damageMeta.setDamage(amount);
                }
            }

            item.setItemMeta(damageMeta);
        }
    }

}
