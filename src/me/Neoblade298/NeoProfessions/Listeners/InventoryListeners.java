package me.Neoblade298.NeoProfessions.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import me.Neoblade298.NeoProfessions.Professions;

public class InventoryListeners implements Listener {
	Professions main;
	public InventoryListeners(Professions main) {
		this.main = main;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (Professions.viewingInventory.containsKey(p)) {
			Professions.viewingInventory.get(p).handleInventoryClick(e);
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (Professions.viewingInventory.containsKey(p)) {
			Professions.viewingInventory.get(p).handleInventoryDrag(e);
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (Professions.viewingInventory.containsKey(p) && e.getInventory() == Professions.viewingInventory.get(p).getInventory()) {
			Professions.viewingInventory.get(p).handleInventoryClose(e);
			Professions.viewingInventory.remove(p);
		}
	}
}
