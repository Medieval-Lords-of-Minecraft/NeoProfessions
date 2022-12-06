package me.Neoblade298.NeoProfessions.Listeners;

import java.util.Iterator;
import java.util.TreeMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.sucy.skill.api.event.PlayerExperienceGainEvent;

import me.neoblade298.neomythicextension.events.ChestDropEvent;
import me.neoblade298.neomythicextension.events.MythicResearchPointsChanceEvent;

public class BoostListener implements Listener {
	public static TreeMap<Double, String> expMultipliers = new TreeMap<Double, String>();
	public static TreeMap<Double, String> melbaMultipliers = new TreeMap<Double, String>();
	public static TreeMap<Double, String> bossChestMultipliers = new TreeMap<Double, String>();
	public static TreeMap<Double, String> profExpMultipliers = new TreeMap<Double, String>();
	public static TreeMap<Double, String> researchMultipliers = new TreeMap<Double, String>();
	
	public BoostListener() {
		expMultipliers.put(1.25, "boosts.skillapiexp.25");
		expMultipliers.put(1.5, "boosts.skillapiexp.50");
		expMultipliers.put(2.0, "boosts.skillapiexp.100");
		
		melbaMultipliers.put(1.25, "boosts.melba.25");
		melbaMultipliers.put(1.5, "boosts.melba.50");
		melbaMultipliers.put(2.0, "boosts.melba.100");
		
		bossChestMultipliers.put(1.25, "boosts.bosschests.25");
		bossChestMultipliers.put(1.5, "boosts.bosschests.50");
		bossChestMultipliers.put(2.0, "boosts.bosschests.100");
		
		profExpMultipliers.put(1.25, "boosts.professionexp.25");
		profExpMultipliers.put(1.5, "boosts.professionexp.50");
		profExpMultipliers.put(2.0, "boosts.professionexp.100");
		
		researchMultipliers.put(1.25, "boosts.research.25");
		researchMultipliers.put(1.5, "boosts.research.50");
		researchMultipliers.put(2.0, "boosts.research.100");
	}
	
	@EventHandler
	public void onSkillAPIExperienceGain(PlayerExperienceGainEvent e) {
		Player p = e.getPlayerData().getPlayer();
		Iterator<Double> iter = expMultipliers.descendingKeySet().iterator();
		while (iter.hasNext()) {
			double mult = iter.next();
			if (p.hasPermission(expMultipliers.get(mult))) {
				e.setExp(e.getExp() * mult);
				return;
			}
		}
	}
	


	@EventHandler(priority = EventPriority.HIGH)
	public void onResearchPointGain(MythicResearchPointsChanceEvent e) {
		Player p = e.getPlayer();
		Iterator<Double> iter = researchMultipliers.descendingKeySet().iterator();
		while (iter.hasNext()) {
			double mult = iter.next();
			if (p.hasPermission(researchMultipliers.get(mult))) {
				e.setChance(e.getChance() * mult);
				return;
			}
		}
	}
	

	@EventHandler(priority = EventPriority.HIGH)
	public void onChestDrop(ChestDropEvent e) {
		if (e.getDropType() == 2) {
			return; // Boss chest token
		}
		
		Player p = e.getPlayer();
		Iterator<Double> iter = bossChestMultipliers.descendingKeySet().iterator();
		while (iter.hasNext()) {
			double mult = iter.next();
			if (p.hasPermission(bossChestMultipliers.get(mult))) {
				e.setChance(e.getChance() * mult);
				return;
			}
		}
	}
}
