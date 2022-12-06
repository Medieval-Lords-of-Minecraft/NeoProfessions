package me.Neoblade298.NeoProfessions.Minigames;

import java.util.ArrayList;

public class MinigameDroptable {
	ArrayList<MinigameDrops> droptable;
	public ArrayList<MinigameDrops> getDropTable() {
		return droptable;
	}
	
	public int calculateTotalWeight(MinigameParameters params) {
		int weight = 0;
		for (MinigameDrops drops : droptable) {
			weight += drops.getWeight() * params.getRarityWeightMultiplier(drops.getItem().getRarity());
		}
		return weight;
	}
	
	public MinigameDroptable(ArrayList<MinigameDrops> droptable) {
		this.droptable = droptable;
	}
	
}
