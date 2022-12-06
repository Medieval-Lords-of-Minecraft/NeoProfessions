package me.Neoblade298.NeoProfessions.Inventories;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.ItemEditor;
import me.Neoblade298.NeoProfessions.Events.ProfessionSlotSuccessEvent;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class ConfirmAugmentInventory extends ProfessionInventory {
	ItemStack item;
	ItemStack augment;
	ItemEditor editor;

	public ConfirmAugmentInventory(Professions main, Player p, ItemStack item, ItemStack augment) {
		this.augment = augment;
		this.item = item;
		this.editor = new ItemEditor(item);
		
		inv = Bukkit.createInventory(p, 9, "§cReplace which slot?");

		inv.addItem(item);
		NBTItem nbti = new NBTItem(item);
		inv.addItem(augment);
		ItemStack[] contents = inv.getContents();
		for (int i = 2; i < 9 - nbti.getInteger("slotsCreated"); i++) {
			contents[i] = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ");
		}
		
		int j = 1;
		for (int i = 9 - nbti.getInteger("slotsCreated"); i < 9; i++) {
			Augment oldAug = editor.getAugment(j);
			if (oldAug == null) {
				contents[i] = createGuiItem(Material.LIME_STAINED_GLASS_PANE, "§aAdd to Slot " + j, "§7Empty slot");
			}
			else {
				contents[i] = createGuiItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "§9Replace Slot " + j, true,
						oldAug.getLine(), oldAug.getItem(p).getItemMeta().getLore(), oldAug);
			}
			j++;
		}
		inv.setContents(contents);

		setupInventory(p, inv, this);
	}

	protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return item;
	}

	protected ItemStack createGuiItem(final Material material, final String name, final boolean isReplace, final String line, List<String> list, Augment aug) {
		final ItemStack item = new ItemStack(material);
		final ItemMeta meta = item.getItemMeta();
		list.add(0, line);
		if (isReplace) {
			list.add(0, "§cCurrent augment will be destroyed!");
		}
		if (aug.isPermanent()) {
			meta.setDisplayName("§cThis augment cannot be swapped");
			item.setType(Material.RED_STAINED_GLASS_PANE);
		}
		else {
			meta.setDisplayName(name);
		}
		meta.setLore(list);
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

		if (clickedItem.getType().equals(Material.LIME_STAINED_GLASS_PANE) ||
				clickedItem.getType().equals(Material.LIGHT_BLUE_STAINED_GLASS_PANE)) {
			String clicked = clickedItem.getItemMeta().getDisplayName();
			int selected = Integer.parseInt(clicked.substring(clicked.length() - 1));
			NBTItem nbtaug = new NBTItem(this.augment);
			Augment aug = AugmentManager.getFromCache(nbtaug.getString("augment"), nbtaug.getInteger("level"));
			
			String result = editor.setAugment(p, aug, selected);
			if (result == null) {
				Util.sendMessage(p, "&7Successfully slotted item!");
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
				p.getInventory().removeItem(augment);
				Bukkit.getPluginManager().callEvent(new ProfessionSlotSuccessEvent(p, editor.getItem(), aug));
				p.closeInventory();
			}
			else {
				Util.sendMessage(p, "Could not set augment on slot " + selected + ", " + result);
			}
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
