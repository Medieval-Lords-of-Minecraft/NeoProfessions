package me.Neoblade298.NeoProfessions.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.Neoblade298.NeoProfessions.Recipes.Recipe;

public class ProfessionCraftSuccessEvent extends Event {
	Player p;
	Recipe recipe;
    private static final HandlerList handlers = new HandlerList();

	public ProfessionCraftSuccessEvent(Player p, Recipe recipe) {
		this.recipe = recipe;
		this.p = p;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public Recipe getRecipe() {
		return recipe;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
