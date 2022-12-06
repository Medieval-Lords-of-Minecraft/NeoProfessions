package me.Neoblade298.NeoProfessions.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Managers.RecipeManager;

public class RecipeSelectInventory extends ProfessionInventory {
	Player p;

	public RecipeSelectInventory(Player p) {
		this.p = p;
		inv = Bukkit.createInventory(p, 9, "§1Crafting Recipes");

		ItemStack[] contents = inv.getContents();
		contents[0] = createItem(Material.COOKED_BEEF, "§9Food Recipes", "Food", "food");
		contents[1] = createItem(Material.IRON_SWORD, "§9Gear Recipes", "Gear", "gear");
		contents[2] = createItem(Material.ENDER_PEARL, "§9Augment Recipes", "Augments", "augments");
		contents[3] = createItem(Material.COMPOSTER, "§9Fertilizer Recipes", "Fertilizers", "fertilizer");
		contents[4] = createItem(Material.OAK_SAPLING, "§9Garden Upgrades", "Garden Upgrades", "garden-upgrades");
		contents[5] = createItem(Material.CHEST, "§9All Recipes", "All", "all");
		contents[8] = createItem(Material.HOPPER, "§9/inv", "All", "all");
		inv.setContents(contents);

		setupInventory(p, inv, this);
	}

	protected ItemStack createItem(Material mat, String name, String recipeDisplay, String recipeList) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("recipeDisplay", recipeDisplay);
		nbti.setString("recipeList", recipeList);
		return nbti.getItem();
	}

	// Check for clicks on items
	public void handleInventoryClick(final InventoryClickEvent e) {
		if (e.getInventory() != inv)
			return;

		e.setCancelled(true);

		final ItemStack clickedItem = e.getCurrentItem();

		// verify current item is not null
		if (clickedItem == null || clickedItem.getType().isAir()) {
			return;
		}
		
		if (e.getRawSlot() == 8) {
			new StorageSelectInventory(p);
			return;
		}

		NBTItem nbti = new NBTItem(clickedItem);
		String display = nbti.getString("recipeDisplay");
		String list = nbti.getString("recipeList");
		new RecipeView(p, display, RecipeManager.getRecipeList(list), this.getClass().getName());
	}

	// Cancel dragging in this inventory
	public void handleInventoryDrag(final InventoryDragEvent e) {
		if (e.getInventory().equals(inv)) {
			e.setCancelled(true);
		}
	}
	
	public void handleInventoryClose(final InventoryCloseEvent e) {
		
	}
}
