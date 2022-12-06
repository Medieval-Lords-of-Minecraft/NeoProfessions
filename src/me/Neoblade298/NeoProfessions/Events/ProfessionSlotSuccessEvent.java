package me.Neoblade298.NeoProfessions.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Augments.Augment;

public class ProfessionSlotSuccessEvent extends Event {
	ItemStack slotted;
	Augment augment;
	Player p;
    private static final HandlerList handlers = new HandlerList();

	public ProfessionSlotSuccessEvent(Player p, ItemStack slotted, Augment augment) {
		this.slotted = slotted;
		this.augment = augment;
		this.p = p;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public ItemStack getSlotted() {
		return slotted;
	}
	
	public Augment getAugment() {
		return augment;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
