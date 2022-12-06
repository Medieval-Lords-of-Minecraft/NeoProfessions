package me.Neoblade298.NeoProfessions.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;

public class GardenSelectInventory extends ProfessionInventory {
	
	private static final int HARVESTER_SLOT = 2, LOGGER_SLOT = 4, STONECUTTER_SLOT = 6;

	public GardenSelectInventory(Player p) {
		inv = Bukkit.createInventory(p, 9, "§9Your Gardens");

		ItemStack[] contents = inv.getContents();
		contents[2] = createGarden(ProfessionType.HARVESTER);
		contents[4] = createGarden(ProfessionType.LOGGER);
		contents[6] = createGarden(ProfessionType.STONECUTTER);
		inv.setContents(contents);

		setupInventory(p, inv, this);
	}

	protected ItemStack createGarden(ProfessionType type) {
		ItemStack item;
		String name;
		if (type.equals(ProfessionType.HARVESTER)) {
			item = new ItemStack(Material.TALL_GRASS);
			name = "§9Harvester Garden";
		}
		else if (type.equals(ProfessionType.LOGGER)) {
			item = new ItemStack(Material.OAK_LOG);
			name = "§9Logger Arbor";
		}
		else {
			item = new ItemStack(Material.IRON_ORE);
			name = "§9Stonecutter Quarry";
		}
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
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

		final Player p = (Player) e.getWhoClicked();
		final int slot = e.getRawSlot();
		
		switch (slot) {
		case HARVESTER_SLOT: new GardenInventory(p, ProfessionType.HARVESTER);
			break;
		case LOGGER_SLOT: new GardenInventory(p, ProfessionType.LOGGER);
			break;
		case STONECUTTER_SLOT: new GardenInventory(p, ProfessionType.STONECUTTER);
			break;
		}
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
