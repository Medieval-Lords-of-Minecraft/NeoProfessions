package me.Neoblade298.NeoProfessions.Events;

import me.Neoblade298.NeoProfessions.Minigames.MinigameParameters;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ProfessionHarvestEvent extends Event {
	MinigameParameters params;
	Player p;
	ProfessionType type;
    private static final HandlerList handlers = new HandlerList();

	public ProfessionHarvestEvent(Player p, MinigameParameters params, ProfessionType type) {
		this.p = p;
		this.params = params;
		this.type = type;
	}
	
	public Player getPlayer() {
		return p;
	}

	public MinigameParameters getParams() {
		return params;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public ProfessionType getType() {
		return type;
	}
}
