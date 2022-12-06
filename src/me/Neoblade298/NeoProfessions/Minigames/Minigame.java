package me.Neoblade298.NeoProfessions.Minigames;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Events.ProfessionHarvestEvent;
import me.Neoblade298.NeoProfessions.Inventories.HarvestingMinigame;
import me.Neoblade298.NeoProfessions.Inventories.LoggingMinigame;
import me.Neoblade298.NeoProfessions.Inventories.StonecuttingMinigame;
import me.Neoblade298.NeoProfessions.Managers.MinigameManager;
import me.Neoblade298.NeoProfessions.Managers.ProfessionManager;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;
import me.Neoblade298.NeoProfessions.Storage.StoredItemInstance;

public class Minigame {
	private ProfessionType type;
	private MinigameDroptable droptable;
	private String display;
	int numDrops, difficulty, level, cooldown;
	
	public Minigame(String display, ProfessionType type, ArrayList<MinigameDrops> droptable, int numDrops, int difficulty, int level, int cooldown) {
		this.type = type;
		this.droptable = new MinigameDroptable(droptable);
		this.numDrops = numDrops;
		this.display = display;
		this.difficulty = difficulty;
		this.level = level;
		this.cooldown = cooldown;
	}
	
	private ArrayList<MinigameDrop> generateDrops(MinigameParameters params) {
		ArrayList<MinigameDrop> drops = new ArrayList<MinigameDrop>();
		int totalWeight = droptable.calculateTotalWeight(params);
		
		for (int i = 0; i < numDrops; i++) {
			int rand = Professions.gen.nextInt(totalWeight);
			MinigameDrops mdrops = null;
			int index = 0;
			int weight;
			while (rand >= 0) {
				mdrops = droptable.getDropTable().get(index);
				weight = mdrops.getWeight();
				weight *= params.getRarityWeightMultiplier(mdrops.getItem().getRarity());
				rand -= weight;
				index++;
			}
			int min = mdrops.getMinAmt(), max = mdrops.getMaxAmt();
			min *= params.getAmountMultiplier();
			max *= params.getAmountMultiplier();
			
			drops.add(new MinigameDrop(new StoredItemInstance(mdrops.getItem(),
					max == min ? min :Professions.gen.nextInt(max - min) + min), mdrops.getExp()));
		}
		return drops;
	}
	
	public boolean startMinigame(Player p) {
		return startMinigame(p, null);
	}
	
	public boolean startMinigame(Player p, MinigameParameters params) {
		if (ProfessionManager.getLevel(p, type) < level) {
			p.sendMessage("§cYour §6" + type.getDisplay() + " §clevel is too low! It must be at least level §e" + level);
			return false;
		}
		
		if (params == null) {
			params = new MinigameParameters();
		}
		
		ProfessionHarvestEvent e = new ProfessionHarvestEvent(p, params, type);
		Bukkit.getPluginManager().callEvent(e);
		
		if (type.equals(ProfessionType.STONECUTTER)) {
			new StonecuttingMinigame(MinigameManager.main, p, generateDrops(params), display, difficulty);
			return true;
		}
		else if (type.equals(ProfessionType.HARVESTER)) {
			new HarvestingMinigame(MinigameManager.main, p, generateDrops(params), display, difficulty);
			return true;
		}
		else if (type.equals(ProfessionType.LOGGER)) {
			new LoggingMinigame(MinigameManager.main, p, generateDrops(params), display, difficulty);
			return true;
		}
		return false;
	}
	
	public int getCooldown() {
		return cooldown;
	}
}
