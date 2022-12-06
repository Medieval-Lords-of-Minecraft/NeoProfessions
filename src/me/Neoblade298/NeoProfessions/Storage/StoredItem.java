package me.Neoblade298.NeoProfessions.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Gardens.Fertilizer;
import me.Neoblade298.NeoProfessions.Managers.GardenManager;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.Neoblade298.NeoProfessions.Objects.Rarity;
import me.Neoblade298.NeoProfessions.Objects.SkullCreator;
import me.Neoblade298.NeoProfessions.Objects.StoredItemSource;

public class StoredItem {
	private String mat;
	private String display;
	private String name;
	private int id;
	private int level;
	private int defaultExp;
	private double value;
	private Rarity rarity;
	private ArrayList<String> baseLore;
	private ArrayList<String> storageLore;
	private ArrayList<StoredItemSource> sources;
	private ArrayList<String> sourceLore;
	private TreeSet<String> relevantRecipes;
	
	private static final int SOURCES_POS = 2;

	public StoredItem(int id, String name, int level, String rarity, String mat, ArrayList<String> lore) {
		this.id = id;
		this.rarity = Rarity.valueOf(rarity.toUpperCase());
		this.baseLore = new ArrayList<String>();
		this.name = name;
		this.value = level * this.rarity.getPriceModifier();
		this.display = "§6[Lv " + level + "] " + this.rarity.getCode() + name;
		this.baseLore.add("§6Rarity§7: " + this.rarity.getDisplay());
		this.baseLore.add("§6Value§7: §e" + String.format("%.2f", this.value));
		this.baseLore.add("§7§m---");
		if (lore != null) {
			for (String line : lore) {
				this.baseLore.add("§7§o" + line);
			}
		}
		this.mat = mat;
		this.relevantRecipes = new TreeSet<String>();
		this.storageLore = new ArrayList<String>(baseLore);
		this.storageLore.add(SOURCES_POS, "§6Sources§7:");
		this.level = level;
		// Sources added as more things are loaded
		this.sources = new ArrayList<StoredItemSource>();
		this.sourceLore = new ArrayList<String>();
		this.sourceLore.add("§6Rarity§7: " + this.rarity.getDisplay());
		this.sourceLore.add("§6Sources§7:");
		
		this.defaultExp = (int) (level * Professions.getExpMultiplier(this.rarity));
	}
	
	public void addRelevantRecipe(String key) {
		this.relevantRecipes.add(key);
	}
	
	public TreeSet<String> getRelevantRecipes() {
		return relevantRecipes;
	}
	
	public double getValue() {
		return value;
	}
	
	public int getId() {
		return this.id;
	}

	public String getDisplay() {
		return display;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<String> getBaseLore() {
		return this.baseLore;
	}

	public Rarity getRarity() {
		return this.rarity;
	}

	public int getLevel() {
		return this.level;
	}
	
	public void addSource(String source, boolean isMob) {
		source = source.replaceAll("&", "§");
		StoredItemSource src = new StoredItemSource(source, isMob);
		if (sources.contains(src)) {
			return;
		}

		sources.add(src);
		if (isMob) {
			MythicMob mm = MythicBukkit.inst().getMobManager().getMythicMob(source).get();
			if (mm != null) {
				source = mm.getDisplayName().get();
			}
		}
		this.storageLore.add(SOURCES_POS + 1, "§7- " + source);
		this.sourceLore.add("§7- " + source);
	}
	
	public ArrayList<StoredItemSource> getSources() {
		return sources;
	}
	
	public ItemStack getItem() {
		ItemStack item;
		if (mat.length() > 40) {
			item = SkullCreator.itemFromBase64(mat);
		}
		else {
			item = new ItemStack(Material.getMaterial(mat.toUpperCase()));
		}
		
		return item;
	}
	
	public ItemStack getBaseView(int amount) {
		ItemStack item = getItem();
		ItemMeta meta = item.getItemMeta();
		meta.setLore(baseLore);
		if (amount <= 0) {
			return null;
		}
		meta.setDisplayName(display + " §fx" + amount);
		item.setItemMeta(meta);
		item.setAmount(amount > 64 ? 64 : amount);
		return item;
	}
	
	public int getDefaultExp() {
		return defaultExp;
	}
	
	public ItemStack getStorageView(int amount, boolean fullLore) {
		ItemStack item = getItem();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		Fertilizer fert = GardenManager.getFertilizer(this.id);
		if (fert != null) {
			for (String line : fert.getEffects()) {
				lore.add(line);
			}
		}
		for (String line : fullLore ? storageLore : sourceLore) {
			lore.add(line);
		}
		meta.setLore(lore);
		if (amount <= 0) {
			return null;
		}
		meta.setDisplayName(display + " §fx" + amount);
		item.setItemMeta(meta);
		item.setAmount(amount > 64 ? 64 : amount);
		return item;
	}
	
	public ItemStack getSourceView(Player p, int compare) {
		ItemStack item = getItem();
		ItemMeta meta = item.getItemMeta();
		int playerHas = StorageManager.getAmount(p, id);
		String display = playerHas >= compare ? "§a" : "§c";
		display += ChatColor.stripColor(this.display) + " " + playerHas + " / " + compare;
		meta.setDisplayName(display);
		meta.setLore(sourceLore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCompareView(Player p, int compare) {
		ItemStack item = getItem();
		ItemMeta meta = item.getItemMeta();
		meta.setLore(storageLore);
		int playerHas = StorageManager.getAmount(p, id);
		String display = playerHas >= compare ? "§a" : "§c";
		display += ChatColor.stripColor(this.display) + " " + playerHas + " / " + compare;
		meta.setDisplayName(display);
		item.setItemMeta(meta);
		return item;
	}
}
