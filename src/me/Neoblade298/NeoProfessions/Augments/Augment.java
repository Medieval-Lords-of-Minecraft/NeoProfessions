package me.Neoblade298.NeoProfessions.Augments;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;

public abstract class Augment {
	protected String name;
	protected int level;
	protected List<EventType> etypes;
	protected static DecimalFormat df = new DecimalFormat("##.#");
	
	public Augment() {
		this.level = 5;
	}
	
	public Augment(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLine() {
		return "§7[" + name + " Lv " + level + "]";
	}
	
	public void setEventType(List<EventType> etypes) {
		this.etypes = etypes;
	}
	
	public List<EventType> getEventTypes() {
		return etypes;
	}
	
	public String formatPercentage(double bonus) {
		return df.format(bonus * 100);
	}
	
	public String formatDouble(double num) {
		return df.format(num);
	}
	
	public ItemStack getItem(Player user) {
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4[Lv " + level + "] §c" + name + " Augment");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Level " + level + " " + name + " Augment");
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setInteger("level", level);
		nbti.setString("augment", name);
		nbti.setDouble("value", (double) (250 * (level / 5)));
		return nbti.getItem();
	}
	
	public boolean isPermanent() {
		return false;
	}
	
	public abstract Augment createNew(int level);
	
	@Override
	public String toString() {
		return name;
	}
}
