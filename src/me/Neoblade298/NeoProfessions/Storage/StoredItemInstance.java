package me.Neoblade298.NeoProfessions.Storage;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;

public class StoredItemInstance {
	StoredItem item;
	int amt;
	private static ArrayList<String> voucherLore = new ArrayList<String>();
	
	static {
		voucherLore.add("§7§oRight click to claim!");
	}
	
	public StoredItemInstance(StoredItem item, int amt) {
		this.item = item;
		this.amt = amt;
	}

	public StoredItem getItem() {
		return item;
	}

	public int getAmount() {
		return amt;
	}
	
	public String toString() {
		return item.getDisplay() + " §fx" + amt;
	}
	
	public ItemStack getBaseView() {
		return item.getBaseView(amt);
	}
	
	public ItemStack getStorageView(boolean fullLore) {
		return item.getStorageView(amt, fullLore);
	}
	
	public ItemStack getSourceView(Player p, int compare) {
		return item.getSourceView(p, compare);
	}
	
	public ItemStack getCompareView(Player p, int compare) {
		return item.getCompareView(p, compare);
	}
	
	public boolean sell(Player p, int amount) {
		if (amount > this.amt) {
			p.sendMessage("§4[§c§lMLMC§4] §cNot enough items to sell!");
			return false;
		}
		else if (!StorageManager.playerHas(p, this.item.getId(), amount)) {
			p.sendMessage("§4[§c§lMLMC§4] §cNot enough items to sell!");
			Bukkit.getLogger().log(Level.WARNING,
					"[NeoProfessions] StorageView item instance doesn't match actual storage for id " + this.item.getId() +
					". Expected: " + amt + ", actual: " + StorageManager.getAmount(p, this.item.getId()));
			return false;
		}
		
		amt -= amount;
		StorageManager.takePlayer(p, this.item.getId(), amount);
		Professions.econ.depositPlayer(p, this.item.getValue() * amount);
		return true;
	}
	
	public boolean giveVoucher(Player p, int amount) {
		if (amount > this.amt) {
			p.sendMessage("§4[§c§lMLMC§4] §cNot enough items to create the voucher!");
			return false;
		}
		else if (!StorageManager.playerHas(p, this.item.getId(), amount)) {
			p.sendMessage("§4[§c§lMLMC§4] §cNot enough items to create the voucher!");
			Bukkit.getLogger().log(Level.WARNING,
					"[NeoProfessions] StorageView item instance doesn't match actual storage for id " + this.item.getId() +
					". Expected: " + amt + ", actual: " + StorageManager.getAmount(p, this.item.getId()));
			return false;
		}
		
		amt -= amount;
		StorageManager.takePlayer(p, this.item.getId(), amount);
		p.getInventory().addItem(getVoucher(p, amount));
		return true;
	}
	
	private ItemStack getVoucher(Player p, int amount) {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.item.getDisplay() + " §fx" + amount);
		meta.setLore(voucherLore);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setInteger("id", this.item.getId());
		nbti.setInteger("amount", amount);
		nbti.setDouble("value", this.item.getValue() * amount);
		return nbti.getItem();
	}
}
