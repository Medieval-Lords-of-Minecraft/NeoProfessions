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
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neogear.Gear;
import me.neoblade298.neogear.objects.GearConfig;

public class ConfirmShardInventory extends ProfessionInventory {
	ItemStack item, shard;
	String gear;
	int level;
	ItemEditor editor;

	public ConfirmShardInventory(Professions main, Player p, ItemStack item, ItemStack shard) {
		this.shard = shard;
		this.item = item;
		this.editor = new ItemEditor(item);
		
		inv = Bukkit.createInventory(p, 9, "§cAre you sure?");

		inv.addItem(item);
		NBTItem nbti = new NBTItem(item);
		gear = nbti.getString("gear");
		level = nbti.getInteger("level");
		inv.addItem(shard);
		ItemStack[] contents = inv.getContents();
		for (int i = 2; i < 7; i++) {
			contents[i] = createGuiItem(Material.RED_STAINED_GLASS_PANE, "§cNo");
		}
		contents[8] = createGuiItem(Material.LIME_STAINED_GLASS_PANE, "§aYes");
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

		if (clickedItem.getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
			NBTItem nbts = new NBTItem(this.shard);
			String type = nbts.getString("shard");
			
			GearConfig conf = Gear.getGearConfig(gear, level);
			String result = null;
			if (type.equals("level")) {
				result = conf.increaseLevel(p, item, 5);
			}
			else {
				result = conf.increaseRarity(p, item);
			}
			if (result == null) {
				Util.sendMessage(p, "&7Successfully used shard!");
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
				p.getInventory().removeItem(shard);
				p.closeInventory();
			}
			else {
				Util.sendMessage(p, "Could not use shard, " + result);
			}
		}
		else if (clickedItem.getType().equals(Material.RED_STAINED_GLASS_PANE)) {
			p.closeInventory();
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
