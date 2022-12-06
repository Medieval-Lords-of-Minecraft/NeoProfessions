package me.Neoblade298.NeoProfessions.Gardens;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.Neoblade298.NeoProfessions.Minigames.MinigameParameters;
import me.Neoblade298.NeoProfessions.Objects.Rarity;

public class Fertilizer {
	int id;
	MinigameParameters params;
	double timeMultiplier;
	ArrayList<String> effects;
	static final DecimalFormat df = new DecimalFormat("#.##");
	public Fertilizer(int id, MinigameParameters params, double timeMultiplier) {
		this.id = id;
		this.params = params;
		this.timeMultiplier = timeMultiplier;
		
		effects = new ArrayList<String>();
		effects.add("§6Fertilizer Effects:");
		if (timeMultiplier != 1) {
			effects.add("§7- Harvest Time §e" + df.format(timeMultiplier) + "x");
		}
		if (params.getAmountMultiplier() != 1) {
			effects.add("§7- Harvest Amount §e" + df.format(params.getAmountMultiplier()) + "x");
		}
		for (Rarity rarity : params.getModdedRarities()) {
			effects.add("§7- Droprate of " + rarity.getDisplay() + " §e" + df.format(params.getRarityWeightMultiplier(rarity)) + "x");
		}
	}
	public int getId() {
		return id;
	}
	public MinigameParameters getParams() {
		return params;
	}
	public double getTimeMultiplier() {
		return timeMultiplier;
	}
	public ArrayList<String> getEffects() {
		return effects;
	}
}
