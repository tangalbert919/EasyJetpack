package com.github.jselby.easyjetpack;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomArmor {
	public static boolean isValidItem(ItemStack item) {
		// Checks if we are using custom items
		if (!ItemDetection.isUsingCustomItems()) {
			return true;
		}
		// If we are, compare the itemstack to them
		List<String> displayName1 = item.getItemMeta().getLore();
		List<String> displayName2 = getJetpack().getItemMeta().getLore();
		List<String> displayName3 = getBoots().getItemMeta().getLore();
		if (displayName1 != null && (displayName1.equals(displayName2) || displayName1.equals(displayName3))) {
			return true;
		}
		return false;
	}

	public static ItemStack getJetpack() {
		if (!ItemDetection.isUsingCustomItems()) {
			return new ItemStack(EasyJetpack.getInstance().getConfig().getInt("jetpack.id"), 1);
		}
		// returns a instance of the jetpack
		ItemStack stack = new ItemStack(Material.GOLD_CHESTPLATE, 1);
		ItemMeta im = stack.getItemMeta();
		ArrayList<String> list = new ArrayList<String>();
		list.add("§RFly away in the Jetpack 3000!");
		list.add("§RLimited time only! Only $9.95");
		list.add("§Rat your local black market.");
		im.setDisplayName("§R§4Jetpack");
		im.setLore(list);
		stack.setItemMeta(im);
		return stack;
	}

	public static ShapedRecipe getJetpackRecipe() {
		// Builds a recipe for the jetpack
		ShapedRecipe recipe = new ShapedRecipe(getJetpack());
		recipe.shape(" I ", " G ", " F ");
		recipe.setIngredient('I', Material.STONE);
		recipe.setIngredient('G', Material.GOLD_CHESTPLATE);
		recipe.setIngredient('F', Material.FURNACE);
		return recipe;
	}
	
	public static ItemStack getBoots() {
		if (!ItemDetection.isUsingCustomItems()) {
			return new ItemStack(EasyJetpack.getInstance().getConfig().getInt("boots.id"), 1);
		}
		// returns a instance of the fall boots
		ItemStack stack = new ItemStack(Material.LEATHER_BOOTS, 1);
		ItemMeta im = stack.getItemMeta();
		ArrayList<String> list = new ArrayList<String>();
		list.add("§RDirect from Aperture Science!");
		list.add("§ROutlawed in 170 countries!");
		im.setDisplayName("§R§4Fall Boots");
		im.setLore(list);
		stack.setItemMeta(im);
		return stack;
	}

	public static ShapedRecipe getBootRecipe() {
		// Builds a recipe for the fall boots
		ShapedRecipe recipe = new ShapedRecipe(getBoots());
		recipe.shape("   ", "FGF", "III");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('G', Material.LEATHER_BOOTS);
		recipe.setIngredient('F', Material.FEATHER);
		return recipe;
	}

	public static void register() {
		// Registers the recipes with Bukkit
		if (!ItemDetection.isUsingCustomItems()) {
			return;
		}
		EasyJetpack.getInstance().getServer().addRecipe(getJetpackRecipe());
		EasyJetpack.getInstance().getServer().addRecipe(getBootRecipe());
	}

	public static void doItemCraft(PrepareItemCraftEvent event) {
		// Allows custom items to appear in the crafting table
		if (!ItemDetection.isUsingCustomItems()) {
			return;
		}
		if (event.getRecipe().getResult().getItemMeta().getLore() != null
				&& event.getRecipe()
						.getResult()
						.getItemMeta()
						.getLore()
						.equals(getJetpackRecipe().getResult().getItemMeta()
								.getLore())) {
			event.getInventory().setResult(getJetpack());
		} if (event.getRecipe().getResult().getItemMeta().getLore() != null
				&& event.getRecipe()
				.getResult()
				.getItemMeta()
				.getLore()
				.equals(getBootRecipe().getResult().getItemMeta()
						.getLore())) {
			event.getInventory().setResult(getBoots());
		}
	}
}
