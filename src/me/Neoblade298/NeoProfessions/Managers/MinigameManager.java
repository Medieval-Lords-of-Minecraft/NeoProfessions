package me.Neoblade298.NeoProfessions.Managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Inventories.HarvestingMinigame;
import me.Neoblade298.NeoProfessions.Inventories.LoggingMinigame;
import me.Neoblade298.NeoProfessions.Inventories.ProfessionInventory;
import me.Neoblade298.NeoProfessions.Inventories.StonecuttingMinigame;
import me.Neoblade298.NeoProfessions.Minigames.Minigame;
import me.Neoblade298.NeoProfessions.Minigames.MinigameDrops;
import me.Neoblade298.NeoProfessions.Minigames.MinigameParameters;
import me.Neoblade298.NeoProfessions.Objects.Manager;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;
import me.neoblade298.neocore.bukkit.NeoCore;
import me.neoblade298.neocore.shared.exceptions.NeoIOException;
import me.neoblade298.neocore.shared.io.FileLoader;

public class MinigameManager implements Listener, Manager {
	public static Professions main;
	private static HashMap<Location, Integer> gameLocations = new HashMap<Location, Integer>();
	private static HashMap<Integer, Minigame> games = new HashMap<Integer, Minigame>();
	private static HashMap<UUID, HashMap<Location, Long>> playerCooldowns = new HashMap<UUID, HashMap<Location, Long>>();
	private static FileLoader minigameLoader;
	
	static {
		minigameLoader = (yaml, file) -> {
			for (String key : yaml.getKeys(false)) {
				int id = Integer.parseInt(key);
				ConfigurationSection itemCfg = yaml.getConfigurationSection(key);
				String display = itemCfg.getString("display");
				ProfessionType type = ProfessionType.valueOf(itemCfg.getString("type").toUpperCase());
				int numDrops = itemCfg.getInt("num-drops");
				int difficulty = itemCfg.getInt("difficulty");
				int level = StorageManager.getItem(id).getLevel();
				int cooldown = itemCfg.getInt("cooldown");
				// Hardcode level 5 minimum
				if (level <= 5) {
					level = 1;
				}
				
				// Parse locations
				ArrayList<String> locs = (ArrayList<String>) itemCfg.getStringList("locations");
				for (String loc : locs) {
					String[] args = loc.split(" ");
					gameLocations.put(new Location(Bukkit.getWorld(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]),
							Integer.parseInt(args[3])), id);
				}
				
				// Parse drops
				ArrayList<String> drops = (ArrayList<String>) itemCfg.getStringList("drops");
				ArrayList<MinigameDrops> parsed = new ArrayList<MinigameDrops>();
				for (String line : drops) {
					String[] lineArgs = line.split(" ");
					// defaults
					int itemId = 0;
					int minAmt = 1;
					int maxAmt = 1;
					int weight = -1;
					int exp = -1;
					
					for (String lineArg : lineArgs) {
						String[] args = lineArg.split(":");
						switch (args[0]) {
						case "id":
							itemId = Integer.parseInt(args[1]);
							break;
						case "amount":
							String[] amts = args[1].split("-");
							minAmt = Integer.parseInt(amts[0]);
							maxAmt = amts.length == 2 ? Integer.parseInt(amts[1]) : minAmt;
							break;
						case "weight":
							weight = Integer.parseInt(args[1]);
							break;
						case "exp":
							exp = Integer.parseInt(args[1]);
							break;
						}
					}
					
					// Post-processing (auto-generate exp)
					StoredItem sitem = StorageManager.getItem(itemId);
					sitem.addSource(display, false);
					if (exp == -1) {
						exp = sitem.getDefaultExp();
					}
					if (weight == -1) {
						weight = Professions.getDefaultWeight(sitem.getRarity());
					}
					
					parsed.add(new MinigameDrops(sitem, minAmt, maxAmt, weight, exp));
				}
				games.put(id, new Minigame(display, type, parsed, numDrops, difficulty, level, cooldown));
			}
		};
	}
	
	public MinigameManager(Professions main) {
		MinigameManager.main = main;
		reload();
	}
	
	@Override
	public void reload() {
		Bukkit.getLogger().log(Level.INFO, "[NeoProfessions] Loading Minigame manager...");
		games.clear();
		gameLocations.clear();
		try {
			NeoCore.loadFiles(new File(main.getDataFolder(), "minigames"), minigameLoader);
		} catch (NeoIOException e) {
			e.printStackTrace();
		}
	}
	
	public static void startMinigame(Player p, int key) {
		games.get(key).startMinigame(p, null);
	}
	
	public static void startMinigame(Player p, int id, MinigameParameters params) {
		games.get(id).startMinigame(p, params);
	}
	
	// Cooldown in seconds
	public static void startMinigame(Player p, Location loc) {
		if (!playerCooldowns.containsKey(p.getUniqueId())) {
			playerCooldowns.put(p.getUniqueId(), new HashMap<Location, Long>());
		}
		
		HashMap<Location, Long> cooldowns = playerCooldowns.get(p.getUniqueId());
		long currCd = cooldowns.getOrDefault(loc, 0L);
		if (currCd > System.currentTimeMillis()) {
			int time = (int) ((currCd - System.currentTimeMillis()) / 1000); // Remaining time in seconds
			int minutes = time / 60;
			int seconds = time % 60;
			p.sendMessage("§cYou cannot harvest this node for another " + String.format("§c%d:%02d", minutes, seconds));
			return;
		}
		
		Minigame game = games.get(gameLocations.get(loc));
		if (game.startMinigame(p, null)) {
			cooldowns.put(loc, System.currentTimeMillis() + (game.getCooldown() * 1000));
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			ProfessionInventory inv = Professions.viewingInventory.get(p);
			if (inv != null) {
				if (inv instanceof StonecuttingMinigame ||
						inv instanceof LoggingMinigame ||
						inv instanceof HarvestingMinigame) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getHand() != null && !e.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		Location loc = e.getClickedBlock().getLocation();
		if (gameLocations.containsKey(loc)) {
			startMinigame(e.getPlayer(), loc);
		}
	}
}
