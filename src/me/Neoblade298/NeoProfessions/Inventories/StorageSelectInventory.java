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
public class StorageSelectInventory extends ProfessionInventory {
	Player p;

	public StorageSelectInventory(Player p) {
		this.p = p;
		inv = Bukkit.createInventory(p, 9, "§1Inventories");

		ItemStack[] contents = inv.getContents();
		contents[0] = createItem(Material.TALL_GRASS, "§9Harvesting Items", 0, 999);
		contents[1] = createItem(Material.OAK_LOG, "§9Logging Items", 1000, 1999);
		contents[2] = createItem(Material.IRON_ORE, "§9Stonecutting Items", 2000, 2999);
		contents[3] = createItem(Material.ROTTEN_FLESH, "§9Mob Drops", 3000, 3999);
		contents[4] = createItem(Material.SKELETON_SKULL, "§9Dungeon Drops", 4100, 4199);
		contents[5] = createItem(Material.CHEST, "§9All Items", -1, 1);
		contents[8] = createItem(Material.CRAFTING_TABLE, "§9/craft", -1, 1);
		inv.setContents(contents);

		setupInventory(p, inv, this);
	}

	protected ItemStack createItem(Material mat, String name, int min, int max) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setInteger("min", min);
		nbti.setInteger("max", max);
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
			new RecipeSelectInventory(p);
			return;
		}

		NBTItem nbti = new NBTItem(clickedItem);
		int min = nbti.getInteger("min");
		int max = nbti.getInteger("max");
		new StorageView(p, min, max);
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
