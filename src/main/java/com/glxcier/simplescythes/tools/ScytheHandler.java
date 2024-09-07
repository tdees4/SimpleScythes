package com.glxcier.simplescythes.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Handles the main calculations pertaining to scythes.
 */
public class ScytheHandler {

    private final List<Material> scytheMaterials = Arrays.asList(
            Material.WOODEN_HOE, Material.STONE_HOE, Material.GOLDEN_HOE, Material.IRON_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE
    );

    private final List<Material> crops = Arrays.asList(
            Material.WHEAT, Material.BEETROOTS, Material.CARROTS, Material.POTATOES
    );

    private final List<Material> seeds = Arrays.asList(
            Material.WHEAT_SEEDS, Material.BEETROOT_SEEDS, Material.CARROT, Material.POTATO
    );

    private final List<Material> cropItems = Arrays.asList(
            Material.WHEAT, Material.BEETROOT, Material.CARROT, Material.POTATO
    );

    private final Map<Integer, double[]> fortuneOdds; // <Level, Odds Array>
    private final Map<Integer, Double> unbreakingOdds; // <Level, Odds>

    public ScytheHandler() {
        initializeRecipes();

        fortuneOdds = new HashMap<>();
        fortuneOdds.put(1, new double[]{0.35, 0.6, 0.05, 0});
        fortuneOdds.put(2, new double[]{0.25, 0.55, 0.2, 0});
        fortuneOdds.put(3, new double[]{0.1, 0.5, 0.35, 0.05});

        unbreakingOdds = new HashMap<>();
        unbreakingOdds.put(1, 0.2);
        unbreakingOdds.put(2, 0.25);
        unbreakingOdds.put(3, 0.3);
    }

    /**
     * Given a certain material, returns a Scythe made from that
     * material.
     * @param material The material to make the scythe out of.
     * @return The scythe as an ItemStack.
     */
    public ItemStack getScythe(ScytheMaterial material) {
        Material hoe;

        switch (material) {
            case STONE:
                hoe = Material.STONE_HOE;
                break;
            case GOLDEN:
                hoe = Material.GOLDEN_HOE;
                break;
            case IRON:
                hoe = Material.IRON_HOE;
                break;
            case DIAMOND:
                hoe = Material.DIAMOND_HOE;
                break;
            case NETHERITE:
                hoe = Material.NETHERITE_HOE;
                break;
            default:
                hoe = Material.WOODEN_HOE;
        }

        ItemStack item = new ItemStack(hoe);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RESET + material.getName() + " Scythe");
        meta.setLore(Collections.singletonList(ChatColor.BLUE + "Harvesting Tool"));
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Adds all possible scythe recipes to the server which allows scythes to be crafted.
     */
    public void initializeRecipes() {
        System.out.println("Initializing recipes...");
        // Wooden Scythe
        ShapedRecipe woodenRecipe = setUpRecipe(ScytheMaterial.WOODEN);
        RecipeChoice rc = new RecipeChoice.MaterialChoice(Material.OAK_PLANKS, Material.SPRUCE_PLANKS,
                Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.CRIMSON_PLANKS, Material.ACACIA_PLANKS,
                Material.WARPED_PLANKS, Material.CHERRY_PLANKS, Material.MANGROVE_PLANKS, Material.BAMBOO_PLANKS);
        woodenRecipe.setIngredient('W', rc);
        Bukkit.getServer().addRecipe(woodenRecipe);

        // Stone Scythe
        ShapedRecipe stoneRecipe = setUpRecipe(ScytheMaterial.STONE);
        stoneRecipe.setIngredient('W', Material.COBBLESTONE);
        Bukkit.getServer().addRecipe(stoneRecipe);

        // Golden Scythe
        ShapedRecipe goldRecipe = setUpRecipe(ScytheMaterial.GOLDEN);
        goldRecipe.setIngredient('W', Material.GOLD_INGOT);
        Bukkit.getServer().addRecipe(goldRecipe);

        // Iron Scythe
        ShapedRecipe ironRecipe = setUpRecipe(ScytheMaterial.IRON);
        ironRecipe.setIngredient('W', Material.IRON_INGOT);
        Bukkit.getServer().addRecipe(ironRecipe);

        // Diamond Scythe
        ShapedRecipe diamondRecipe = setUpRecipe(ScytheMaterial.DIAMOND);
        diamondRecipe.setIngredient('W', Material.DIAMOND);
        Bukkit.getServer().addRecipe(diamondRecipe);

    }

    /**
     * Sets up the ShapedRecipe object to be added to the server.
     * @param sMaterial The material of the scythe whose crafting recipe will be set up.
     * @return The ShapedRecipe to be added to the server.
     */
    private ShapedRecipe setUpRecipe(ScytheMaterial sMaterial) {
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft(sMaterial.getName().toLowerCase() + "scythe"), getScythe(sMaterial));
        recipe.shape(
                "WW ",
                "S W",
                "S  "
        );
        recipe.setIngredient('S', Material.STICK);
        return recipe;
    }

    /**
     * Determines if the passed ItemStack is a valid scythe.
     * @param item The ItemStack to be evaluated.
     * @return True if the ItemStack is a valid scythe, false otherwise.
     */
    public boolean isScythe(@Nullable ItemStack item) {
        return item != null && item.getItemMeta() != null && item.getItemMeta().getLore() != null &&
                item.getItemMeta().getLore().contains(ChatColor.BLUE + "Harvesting Tool") &&
                scytheMaterials.contains(item.getType());
    }

    /**
     * @param scythe The scythe to be evaluated.
     * @return The radius that the scythe removes crops in.
     */
    public int getRadius(ItemStack scythe) {
        int radius = 0;

        switch (scythe.getType()) {
            case WOODEN_HOE:
                radius = 1;
                break;
            case STONE_HOE:
            case GOLDEN_HOE:
                radius = 2;
                break;
            case IRON_HOE:
                radius = 3;
                break;
            case DIAMOND_HOE:
            case NETHERITE_HOE:
                radius = 4;
                break;
        }

        return radius;
    }

    /**
     * @return The list of crops affected by scythes.
     */
    public List<Material> getCrops() {
        return crops;
    }

    /**
     * @return The list of seeds that scythes can re-plant.
     */
    public List<Material> getSeeds() {
        return seeds;
    }

    /**
     * @return The list of items that scythed crops can drop.
     */
    public List<Material> getCropItems() {
        return cropItems;
    }

    /**
     * @param fortuneLevel The level of fortune on a given scythe.
     * @return The odds that a harvest will yield multiple crops.
     */
    public double[] getFortuneOdds(int fortuneLevel) {
        return fortuneOdds.get(fortuneLevel);
    }

    /**
     * @param unbreakingLevel The level of unbreaking on a given scythe.
     * @return The odds that it will be used without reducing durability.
     */
    public double getUnbreakingOdds(int unbreakingLevel) {
        return unbreakingOdds.get(unbreakingLevel);
    }

    /**
     * @param scythe The scythe to be evaluated.
     * @return The material of the scythe as a ScytheMaterial.
     */
    public ScytheMaterial getScytheMaterial(ItemStack scythe) {
        switch (scythe.getType()) {
            case WOODEN_HOE:
                return ScytheMaterial.WOODEN;
            case STONE_HOE:
                return ScytheMaterial.STONE;
            case GOLDEN_HOE:
                return ScytheMaterial.GOLDEN;
            case IRON_HOE:
                return ScytheMaterial.IRON;
            case DIAMOND_HOE:
                return ScytheMaterial.DIAMOND;
            case NETHERITE_HOE:
                return ScytheMaterial.NETHERITE;
            default:
                return null;
        }
    }

}
