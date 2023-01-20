package me.Neoblade298.NeoProfessions.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;
import me.Neoblade298.NeoProfessions.Storage.StoredItemInstance;
import me.Neoblade298.NeoProfessions.Utilities.SkullCreator;

public class GardenChooseSeedView extends ProfessionInventory {
	
	private Player p;
	private ProfessionType type;
	private int min, max, slot;
	private static final int HARVESTER_BASE = 0, LOGGER_BASE = 1000, STONECUTTER_BASE = 2000,
			BACK_BUTTON = 45;

	public GardenChooseSeedView(Player p, ProfessionType type, int min, int max, int slot) {
		this.type = type;
		this.min = min;
		this.max = max;
		this.slot = slot;
		inv = Bukkit.createInventory(p, 54, "§9Choose a Seed");
		this.p = p;

		ItemStack[] contents = inv.getContents();
		contents[BACK_BUTTON] = createBackButton();
		inv.setContents(setupItems(contents, min, max));

		setupInventory(p, inv, this);
	}
	
	private ItemStack[] setupItems(ItemStack[] contents, int min, int max) {
		int base = 0;
		switch (type) {
		case HARVESTER: base = HARVESTER_BASE;
		break;
		case LOGGER: base = LOGGER_BASE;
		break;
		case STONECUTTER: base = STONECUTTER_BASE;
		break;
		default: break;
		}
		
		int count = 0;
		// First add the base seeds
		for (int i = base; i < base + 12; i++) {
			StoredItem item = StorageManager.getItem(i);
			int amount = StorageManager.getAmount(p, i);
			if (amount > 0) {
				NBTItem nbti = new NBTItem(new StoredItemInstance(item, amount).getStorageView(true));
				nbti.setInteger("id", i);
				contents[count++] = nbti.getItem();
			}
		}
		
		// Add any extra seeds I may have added
		if (min != -1) {
			for (int i = min; i < max; i++) {
				StoredItem item = StorageManager.getItem(i);
				int amount = StorageManager.getAmount(p, i);
				if (amount > 0) {
					NBTItem nbti = new NBTItem(new StoredItemInstance(item, amount).getStorageView(true));
					nbti.setInteger("id", i);
					contents[count++] = nbti.getItem();
				}
			}
		}
		return contents;
	}

	protected ItemStack createBackButton() {
		ItemStack item = SkullCreator.itemFromBase64(StorageView.PREV_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Return");
		item.setItemMeta(meta);
		return item;
	}

	// Check for clicks on items
	public void handleInventoryClick(final InventoryClickEvent e) {
		if (e.getInventory() != inv)
			return;

		e.setCancelled(true);
		final ItemStack item = e.getCurrentItem();

		// verify current item is not null
		if (item == null || item.getType().isAir()) {
			return;
		}
		final Player p = (Player) e.getWhoClicked();
		final int slot = e.getSlot();
		
		if (slot == BACK_BUTTON) {
			new GardenInventory(p, type);
			return;
		}
		
		NBTItem nbti = new NBTItem(inv.getContents()[slot]);
		new GardenChooseFertilizerView(p, nbti.getInteger("id"), min, max, this.slot, type);
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
