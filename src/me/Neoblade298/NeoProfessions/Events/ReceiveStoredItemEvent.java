package me.Neoblade298.NeoProfessions.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReceiveStoredItemEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
	private Player p;
	private int id;
	public ReceiveStoredItemEvent(Player p, int id) {
		this.p = p;
		this.id = id;
	}
	public Player getPlayer() {
		return p;
	}
	public int getId() {
		return id;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
