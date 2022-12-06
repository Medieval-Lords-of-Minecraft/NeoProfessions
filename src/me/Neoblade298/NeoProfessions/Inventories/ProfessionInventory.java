package me.Neoblade298.NeoProfessions.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Events.OpenProfessionInvEvent;

public abstract class ProfessionInventory {
	protected Inventory inv;
	public abstract void handleInventoryClick(InventoryClickEvent e);
	public abstract void handleInventoryDrag(InventoryDragEvent e);
	public abstract void handleInventoryClose(InventoryCloseEvent e);
	public Inventory getInventory() {
		return this.inv;
	}
	public void setupInventory(Player p, Inventory inv, ProfessionInventory pinv) {
		Professions.viewingInventory.put(p, this);
		p.openInventory(inv);
		Bukkit.getPluginManager().callEvent(new OpenProfessionInvEvent(p, pinv));
	}
}
