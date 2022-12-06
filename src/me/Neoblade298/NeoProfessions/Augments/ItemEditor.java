package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;
import me.neoblade298.neogear.Gear;

public class ItemEditor {
	private static HashMap<String, String> typeConverter = new HashMap<String, String>();
	private static HashMap<String, Integer> maxSlotConverter = new HashMap<String, Integer>();
	ItemStack item;
	NBTItem nbti;
	
	static {
		typeConverter.put("Reinforced Helmet", "rhelmet");
		typeConverter.put("Reinforced Chestplate", "rchestplate");
		typeConverter.put("Reinforced Leggings", "rleggings");
		typeConverter.put("Reinforced Boots", "rboots");
		typeConverter.put("Infused Helmet", "ruhelmet");
		typeConverter.put("Infused Chestplate", "ruchestplate");
		typeConverter.put("Infused Leggings", "ruleggings");
		typeConverter.put("Infused Boots", "ruboots");
		
		maxSlotConverter.put("common", 0);
		maxSlotConverter.put("uncommon", 0);
		maxSlotConverter.put("rare", 1);
		maxSlotConverter.put("epic", 2);
		maxSlotConverter.put("legendary", 3);
		maxSlotConverter.put("artifact", 4);
	}
	
	public ItemEditor(ItemStack item) {
		this.item = item;
		this.nbti = new NBTItem(item);
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public Augment getAugment(int i) {
		String augmentName = nbti.getString("slot" + i + "Augment");
		if (AugmentManager.hasAugment(augmentName)) {
			int level = nbti.getInteger("slot" + i + "Level");
			return AugmentManager.getFromCache(augmentName, level);
		}
		return null;
	}
	
	public String unslotAugment(Player p, int i) {
		if (nbti.getInteger("version") == 0) {
			return "item version is old!";
		}
		if (i < 1) {
			return "<1 slot number!";
		}
		if (i > nbti.getInteger("slotsCreated")) {
			return "slot does not yet exist!";
		}
		if (!nbti.hasKey("slot" + i + "Augment")) {
			return "slot " + i + " is unused!";
		}

		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		lore.set(nbti.getInteger("slot" + i + "Line"), "§8[Empty Slot]");
		meta.setLore(lore);
		item.setItemMeta(meta);
		nbti = new NBTItem(item);
		nbti.removeKey("slot" + i + "Augment");
		nbti.removeKey("slot" + i + "Level");
		nbti.applyNBT(item);
		Gear.getGearConfig(nbti.getString("gear"), nbti.getInteger("level")).updateStats(p, item, false);
		return null;
	}
	
	public String setAugment(Player p, Augment aug, int i) {
		if (nbti.getInteger("version") == 0) {
			return "item version is old!";
		}
		if (i < 1) {
			return "<1 slot number!";
		}
		if (i > nbti.getInteger("slotsCreated")) {
			return "slot does not yet exist!";
		}
		
		for (int j = 1; j <= nbti.getInteger("slotsCreated"); j++) {
			if (j == i) {
				continue;
			}
			if (nbti.getString("slot" + j + "Augment").equals(aug.getName())) {
				return "same augment is already slotted!";
			}
		}
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		lore.set(nbti.getInteger("slot" + i + "Line"), aug.getLine());
		meta.setLore(lore);
		item.setItemMeta(meta);
		nbti = new NBTItem(item);
		nbti.setString("slot" + i + "Augment", aug.getName());
		nbti.setInteger("slot" + i + "Level", aug.getLevel());
		nbti.applyNBT(item);
		Gear.getGearConfig(nbti.getString("gear"), nbti.getInteger("level")).updateStats(p, item, false);
		return null;
	}
}
