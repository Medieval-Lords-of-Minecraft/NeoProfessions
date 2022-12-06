package me.Neoblade298.NeoProfessions.PlayerProfessions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Listeners.BoostListener;

public class Profession {
	ProfessionType type;
	int exp, level;
	private static final int START_LEVEL = 5;
	private static final int MAX_LEVEL = 60;
	static HashMap<Integer, Integer> nextLv= new HashMap<Integer, Integer>();
	
	static {
		for (int lv = 1; lv <= 60; lv++) {
			nextLv.put(lv, (30 * lv^2) + (100 * lv));
		}
	}
	
	public Profession(ProfessionType prof) {
		this.type = prof;
		this.exp = 0;
		this.level = START_LEVEL;
	}

	public ProfessionType getType() {
		return type;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public static HashMap<Integer, Integer> getNextLv() {
		return nextLv;
	}

	public static void setNextLv(HashMap<Integer, Integer> nextLv) {
		Profession.nextLv = nextLv;
	}
	
	public void addExp(Player p, int exp) {
		if (this.level == MAX_LEVEL) {
			return;
		}
		if (exp < 0) {
			exp = 1;
		}
		
		exp *= Professions.getExpMultiplier(this.type);
		
		// Boosts
		Iterator<Double> iter = BoostListener.profExpMultipliers.descendingKeySet().iterator();
		while (iter.hasNext()) {
			double mult = iter.next();
			if (p.hasPermission(BoostListener.profExpMultipliers.get(mult))) {
				exp *= mult;
				break;
			}
		}
		
		int newExp = exp + this.exp;
		
		// If next level exists, check that the player can reach it
		p.sendMessage("§a+" + exp + " §7(§f" + newExp + " / " + nextLv.get(this.level) + "§7) §6" + type.getDisplay() + " §7exp");
		boolean levelup = false;
		while (nextLv.containsKey(this.level) && newExp >= nextLv.get(this.level) && this.level < MAX_LEVEL) {
			newExp -= nextLv.get(this.level);
			this.level++;
			levelup = true;
		}
		if (levelup) {
			p.sendMessage(Professions.lvlupMsg.replaceAll("%level%", "" + this.level).replaceAll("%type%", "" + this.type.getDisplay())
					.replaceAll("%profname%", type.getDisplay()));
			p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 1, 1);
		}
		
		if (levelup && this.level == MAX_LEVEL) {
			this.exp = 0;
		}
		else {
			this.exp = (int) newExp;
		}
	}
	
	public void convertExp(HashMap<Integer, Integer> currency, int divider) {
		int newExp = this.exp;
		for (Entry<Integer, Integer> entry : currency.entrySet()) {
			newExp += (entry.getKey() * entry.getValue()) / divider;
		}
		
		while (nextLv.containsKey(this.level) && newExp >= nextLv.get(this.level)) {
			newExp -= nextLv.get(this.level);
			this.level++;
		}
		if (level >= 60) {
			this.level = 60;
			this.exp = 0;
		}
		else {
			this.exp = newExp;
		}
	}
}
