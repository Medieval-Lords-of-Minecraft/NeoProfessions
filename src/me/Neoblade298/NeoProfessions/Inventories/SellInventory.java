package me.Neoblade298.NeoProfessions.Inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

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
import me.Neoblade298.NeoProfessions.Listeners.BoostListener;

public class SellInventory extends ProfessionInventory {
	private Professions main;
	private final int SELL_ICON = 8;
	private final int MENU_MODEL = 5000;
	private boolean sold = false;

	public SellInventory(Professions main, Player p) {
		this.main = main;
		inv = Bukkit.createInventory(p, 54, "§cPlace items here to sell");
		ItemStack[] contents = inv.getContents();
		contents[SELL_ICON] = createGuiItem(Material.LIME_CONCRETE, "§aConfirm Sell", "§7You can check the value of", "§7item with §c/value");
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
		if (!sold && e.getClickedInventory() == inv && e.getRawSlot() == SELL_ICON) {
			e.setCancelled(true);
			confirmSell((Player) e.getWhoClicked());
		}
		
		else if (sold) {
			if (isUntouchable(e.getCurrentItem())) {
				e.setCancelled(true);
			}
		}
	}
	
	public void handleInventoryDrag(final InventoryDragEvent e) {
		if (sold) {
			e.setCancelled(true);
		}
	}
	
	public void handleInventoryClose(final InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (e.getInventory() == inv) {
			for (ItemStack item : inv.getContents()) {
				if (item == null || isUntouchable(item)) {
					continue;
				}
				
				HashMap<Integer, ItemStack> failed = p.getInventory().addItem(item);
				for (Entry<Integer, ItemStack> entry : failed.entrySet()) {
					p.getWorld().dropItem(p.getLocation(), entry.getValue());
				}
			}
		}
	}
	
	private boolean isUntouchable(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
				item.getItemMeta().getCustomModelData() == MENU_MODEL;
	}
	
	private void confirmSell(Player p) {
		double totalSell = 0;
		int numSold = 0;
		ItemStack[] contents = inv.getContents();
		for (int i = 0; i < contents.length; i++) {
			ItemStack item = contents[i];
			if (i == SELL_ICON) continue;
			
			if (item != null) {
				NBTItem nbti = new NBTItem(item);
				
				if (!nbti.hasKey("value")) continue;
				double value = 0;
				if (!nbti.getString("value").isBlank()) {
					value = Double.parseDouble(nbti.getString("value"));
				}
				else {
					value = nbti.getDouble("value");
				}
				
				totalSell += value * item.getAmount();
				numSold += item.getAmount();
				
				ItemStack receipt = new ItemStack(Material.PAPER);
				ItemMeta receiptMeta = receipt.getItemMeta();
				
				String name = item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name();
				receiptMeta.setDisplayName(name);
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("§7Amount sold: §e" + item.getAmount() + "x");
				lore.add("§7Price: §e" + value + "g");
				lore.add("§7Total: §a" + value * item.getAmount() + "g");
				receiptMeta.setLore(lore);
				receiptMeta.setCustomModelData(MENU_MODEL);
				receipt.setItemMeta(receiptMeta);
				contents[i] = receipt;
			}
		}

		// Boosts
		Iterator<Double> iter = BoostListener.melbaMultipliers.descendingKeySet().iterator();
		double finalMult = -1;
		while (iter.hasNext()) {
			double mult = iter.next();
			if (p.hasPermission(BoostListener.melbaMultipliers.get(mult))) {
				totalSell *= mult;
				finalMult = mult;
				break;
			}
		}
		
		
		ItemStack totalReceipt = new ItemStack(Material.OAK_SIGN);
		ItemMeta receiptMeta = totalReceipt.getItemMeta();
		receiptMeta.setDisplayName("§aSuccessfully sold " + numSold + " items!");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Total gold gained: §a" + totalSell + "g");
		if (finalMult != -1) lore.add("§7Melba Multiplier active: §e" + finalMult + "x");
		lore.add("§7Unsold items will be returned to your");
		lore.add("§7inventory after you close this menu.");
		receiptMeta.setLore(lore);
		receiptMeta.setCustomModelData(MENU_MODEL);
		totalReceipt.setItemMeta(receiptMeta);
		contents[SELL_ICON] = totalReceipt;
		inv.setContents(contents);
		sold = true;
		main.getEconomy().depositPlayer(p, totalSell);
		p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
		Bukkit.getLogger().info("[NeoProfessions] Player " + p.getName() + " sold " + numSold + " items for " + totalSell);
		p.sendMessage("§4[§c§lMLMC§4] §7Successfully sold §e" + numSold + " §7items for §a" + totalSell + "g§7.");
	}
}
