package me.Neoblade298.NeoProfessions.Storage;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Objects.SkullCreator;

public class Sorter {
	int priority;
	int sortType;
	boolean reverse;
	String settingString;
	
	public static final int NAME_SORT = 0;
	public static final int LEVEL_SORT = 1;
	public static final int RARITY_SORT = 2;
	public static final int AMOUNT_SORT = 3;
	
	public static final String ALPHABET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTUxN2I0ODI5YjgzMTkyYmQ3MjcxMTI3N2E4ZWZjNDE5NjcxMWU0MTgwYzIyYjNlMmI4MTY2YmVhMWE5ZGUxOSJ9fX0=";
	public static final String RARITY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjExNmI2OGQ2MmEzNWMwY2UwYzhjNTU3YWM2MzRmNzY3NzA0OGM2ZmVkMjk2YTBkZDFlZDFhOWM0NzZiMjZlNiJ9fX0=";
	public static final String LEVEL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDliMzAzMDNmOTRlN2M3ODVhMzFlNTcyN2E5MzgxNTM1ZGFmNDc1MzQ0OWVhNDFkYjc0NmUxMjM0ZTlkZDJiNSJ9fX0=";
	public static final String AMOUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQxNTY0YmZlYzNiZDNhZjAzNTU4ODIzYzYwZTBlMTllODlhMGJjNzdjNjRmNWI3YjI1ZjVjNDM1YTViMWY5YyJ9fX0=";
	
	public Sorter(int sortType, int startPriority, boolean reverse) {
		this.priority = startPriority;
		this.sortType = sortType;
		this.reverse = reverse;

		switch (sortType) {
		case NAME_SORT: settingString = "name";
		break;
		case LEVEL_SORT: settingString = "level";
		break;
		case RARITY_SORT: settingString = "rarity";
		break;
		default: settingString = "amount";
		break;
		}
	}
	
	public int compare(StoredItemInstance a, StoredItemInstance b) {
		int comp = 0;
		switch (sortType) {
		case NAME_SORT: comp = b.getItem().getName().compareTo(a.getItem().getName());
		break;
		case LEVEL_SORT: comp = b.getItem().getLevel() - a.getItem().getLevel();
		break;
		case RARITY_SORT: comp = b.getItem().getRarity().getPriority() - a.getItem().getRarity().getPriority();
		break;
		case AMOUNT_SORT: comp = b.getAmount() - a.getAmount();
		break;
		}
		if (reverse) {
			comp *= -1;
		}
		return comp;
	}
	
	public ItemStack createSortButton() {
		ItemStack item;
		switch (sortType) {
		case NAME_SORT: item = SkullCreator.itemFromBase64(ALPHABET_HEAD);
		break;
		case LEVEL_SORT: item = SkullCreator.itemFromBase64(LEVEL_HEAD);
		break;
		case RARITY_SORT: item = SkullCreator.itemFromBase64(RARITY_HEAD);
		break;
		default: item = SkullCreator.itemFromBase64(AMOUNT_HEAD);
		break;
		}
		ItemMeta meta = item.getItemMeta();
		switch (sortType) {
		case NAME_SORT: meta.setDisplayName("§9Sort by Name");
		break;
		case LEVEL_SORT: meta.setDisplayName("§9Sort by Level");
		break;
		case RARITY_SORT: meta.setDisplayName("§9Sort by Rarity");
		break;
		case AMOUNT_SORT: meta.setDisplayName("§9Sort by Amount");
		break;
		}
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Priority: §e" + priority);
		lore.add("§7Order: §e" + (reverse ? "Ascending" : "Descending"));
		lore.add("§7§oPress 1-4 to set sort priority");
		lore.add("§7§oLeft click to set ascending/descending");
		meta.setLore(lore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("type", "sort");
		nbti.setInteger("priority", priority);
		return nbti.getItem();
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public boolean flipOrder() {
		reverse = !reverse;
		return reverse;
	}
	
	public String getSettingString() {
		return settingString;
	}
}
