package me.Neoblade298.NeoProfessions.Inventories;

import java.util.Arrays;
import java.util.HashMap;
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
import me.Neoblade298.NeoProfessions.Managers.CurrencyManager;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neogear.listeners.DurabilityListener;

public class RepairInventory extends ProfessionInventory {
	private final ItemStack item;
	private final Player p;
	private int level;
	private static final int REPAIR_ICON = 8;
	private static final int MENU_MODEL = 5000;
	private static final HashMap<Integer, Integer> goldCost = new HashMap<Integer, Integer>();
	private static final int essenceCost = 3;
	
	static {
		goldCost.put(0, 50);
		goldCost.put(5, 100);
		goldCost.put(10, 150);
		goldCost.put(15, 200);
		goldCost.put(20, 250);
		goldCost.put(25, 300);
		goldCost.put(30, 400);
		goldCost.put(35, 500);
		goldCost.put(40, 600);
		goldCost.put(45, 700);
		goldCost.put(50, 800);
		goldCost.put(55, 900);
		goldCost.put(60, 1000);
	}

	public RepairInventory(Professions main, Player p) {
		this.p = p;
		this.item = p.getInventory().getItemInMainHand();
		p.getInventory().removeItem(item);
		level = new NBTItem(item).getInteger("level");
		inv = Bukkit.createInventory(p, 9, "§cRepair this item?");
		
		ItemStack[] contents = inv.getContents();
		contents[0] = item;
		contents[1] = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ");
		for (int i = 2; i < REPAIR_ICON; i++) {
			contents[i] = createGuiItem(Material.RED_STAINED_GLASS_PANE, "§cNo",
					"§7Gold cost: §e" + goldCost.get(level) + "g", "§7Essence cost: §e" + essenceCost);
		}
		contents[REPAIR_ICON] = createGuiItem(Material.LIME_STAINED_GLASS_PANE, "§aYes",
				"§7Gold cost: §e" + goldCost.get(level) + "g", "§7Essence cost: §e" + essenceCost);
		inv.setContents(contents);

		setupInventory(p, inv, this);
	}

	protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		meta.setDisplayName(name);
		meta.setCustomModelData(MENU_MODEL);
		item.setItemMeta(meta);
		return item;
	}

	public void handleInventoryClick(final InventoryClickEvent e) {
		if (e.getRawSlot() >= 2 && e.getRawSlot() <= 7) {
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 1F);
			p.closeInventory();
		}
		else if (e.getRawSlot() == REPAIR_ICON) {
			if (!Professions.econ.has(p, goldCost.get(level))) {
				Util.sendMessage(p, "&cYou don't have enough money!");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 1F);
				p.closeInventory();
				return;
			}
			else if (!CurrencyManager.hasEnough(p, level, essenceCost)) {
				Util.sendMessage(p, "&cYou don't have enough essence!");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 1F);
				p.closeInventory();
				return;
			}
			
			DurabilityListener.fullRepairItem(p, item);
			Professions.econ.withdrawPlayer(p, goldCost.get(level));
			CurrencyManager.subtract(p, level, essenceCost);
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1F, 1F);
			p.closeInventory();
		}
		e.setCancelled(true);
	}
	
	public void handleInventoryDrag(final InventoryDragEvent e) {
		e.setCancelled(true);
	}
	
	public void handleInventoryClose(final InventoryCloseEvent e) {
		HashMap<Integer, ItemStack> failed = p.getInventory().addItem(item);
		for (Integer num : failed.keySet()) {
			p.getWorld().dropItem(p.getLocation(), failed.get(num));
		}
	}
}
