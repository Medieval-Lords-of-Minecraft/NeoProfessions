package me.Neoblade298.NeoProfessions.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.Neoblade298.NeoProfessions.Inventories.ProfessionInventory;

public class OpenProfessionInvEvent extends Event {
	Player p;
	ProfessionInventory inv;
    private static final HandlerList handlers = new HandlerList();

	public OpenProfessionInvEvent(Player p, ProfessionInventory inv) {
		this.inv = inv;
		this.p = p;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public ProfessionInventory getInventory() {
		return inv;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
