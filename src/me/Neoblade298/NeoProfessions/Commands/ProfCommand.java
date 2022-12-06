package me.Neoblade298.NeoProfessions.Commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Inventories.CreateSlotInventory;

import me.Neoblade298.NeoProfessions.Inventories.InspectAugmentsInventory;
import me.Neoblade298.NeoProfessions.Inventories.RecipeSelectInventory;
import me.Neoblade298.NeoProfessions.Inventories.RecipeView;
import me.Neoblade298.NeoProfessions.Inventories.RepairInventory;
import me.Neoblade298.NeoProfessions.Inventories.SellInventory;
import me.Neoblade298.NeoProfessions.Inventories.StorageView;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;
import me.Neoblade298.NeoProfessions.Managers.MinigameManager;
import me.Neoblade298.NeoProfessions.Managers.ProfessionManager;
import me.Neoblade298.NeoProfessions.Managers.RecipeManager;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.Neoblade298.NeoProfessions.Methods.ProfessionsMethods;
import me.Neoblade298.NeoProfessions.Objects.Manager;
import me.Neoblade298.NeoProfessions.PlayerProfessions.Profession;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neogear.listeners.DurabilityListener;

public class ProfCommand implements CommandExecutor {

	Professions main;
	private static ArrayList<String> voucherLore = new ArrayList<String>();
	
	static {
		voucherLore.add("§7§oRight click to claim!");
	}

	public ProfCommand(Professions main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}

		if (args.length == 0) {
			// /prof storage [min] [max]
			// /prof recipes [menu name] [recipe list]
			sender.sendMessage("§7- §c/prof inspect §7- Checks your mainhand item's augments");
			sender.sendMessage("§7- §c/prof update §7- Updates lore of old augments");
		}
		else if (args[0].equalsIgnoreCase("storage")) {
			new StorageView(p, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			return true;
		}
		else if (args[0].equalsIgnoreCase("recipes")) {
			new RecipeView(p, args[1].replaceAll("_", " "), RecipeManager.getRecipeList(args[2]), RecipeSelectInventory.class.getName());
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("inspect")) {
			ItemStack item = p.getInventory().getItemInMainHand();
			if (item != null && !item.getType().isAir() && item.hasItemMeta()) {
				if(new NBTItem(item).hasKey("version")) {
					new InspectAugmentsInventory(main, p, item);
					return true;
				}
			}
			Util.sendMessage(p, "&cCould not inspect this item!");
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("update")) {
			ItemStack item = p.getInventory().getItemInMainHand();
			if (item != null && !item.getType().isAir() && item.hasItemMeta()) {
				NBTItem nbti = new NBTItem(item);
				if(nbti.hasKey("augment")) {
					ItemMeta meta = item.getItemMeta();
					Augment aug = AugmentManager.getFromCache(nbti.getString("augment"), nbti.getInteger("level"));
					meta.setLore(aug.getItem(p).getItemMeta().getLore());
					item.setItemMeta(meta);
					return true;
				}
			}
			Util.sendMessage(p, "&cCould not update this, are you sure it's an augment?");
			return true;
		}

		if (sender.hasPermission("neoprofessions.admin") || sender.isOp()) {
			if (args.length == 0) {
				sender.sendMessage("§7- §4/prof reload");
				sender.sendMessage("§7- §4/prof give [repair/augment] <aug> [level] [amount]");
				sender.sendMessage("§7- §4/prof give item [id] [amount]");
				sender.sendMessage("§7- §4/prof addlevel/exp [player] [type] [level/exp]");
				sender.sendMessage("§7- §4/prof setlevel/exp [player] [type] [level/exp]");
				sender.sendMessage("§7- §4/prof vouch knowledge [key] [displayname]");
				sender.sendMessage("§7- §4/prof debug");
				// prof sell
				// sender.sendMessage("§7- §4/prof start [minigame] [playername]");
				// sender.sendMessage("§7- §4/prof repair [playername]");
				// sender.sendMessage("§7- §4/prof checkaugments [playername]");
				sender.sendMessage("§7- §4/prof createslot [playername]");
				sender.sendMessage("§7- §4/prof artifact <playername>");
				// sender.sendMessage("§7- §4/prof givepaint [playername] R G B");
				return true;
			}
			else {
				if (args[0].equalsIgnoreCase("sell")) {
					if (args.length == 1) {
						new SellInventory(main, p);
					}
					else {
						new SellInventory(main, Bukkit.getPlayer(args[1]));
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("repair")) {
					if (args.length == 1) {
						DurabilityListener.fullRepairItem(p, p.getInventory().getItemInMainHand());
					}
					else {
						p = Bukkit.getPlayer(args[1]);
						ItemStack item = p.getInventory().getItemInMainHand();
						if (item == null || item.getType().isAir()) {
							Util.sendMessage(p, "§cYou're not holding anything in your hand!");
							return true;
						}
						NBTItem nbti = new NBTItem(item);
						if (!nbti.hasKey("gear") || !nbti.hasKey("level")) {
							Util.sendMessage(p, "§cThis item cannot be repaired! It may be outdated.");
							return true;
						}
						new RepairInventory(main, p);
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("reload")) {
					if (args.length == 1) {
						AugmentManager.getMain().loadConfig();
						for (Manager mngr : Professions.managers) {
							mngr.reload();
						}
						sender.sendMessage("§4[§c§lMLMC§4] §7Successful reload.");
					}
					return true;
				}
				// prof addlevel/exp [player] [type] [level]
				else if (args[0].equalsIgnoreCase("addlevel") || args[0].equalsIgnoreCase("addexp")) {
					Player target = null;
					int offset = 0;
					if (args.length == 4) {
						target = Bukkit.getPlayer(args[1]);
						offset++;
					}
					else if (args.length == 3) {
						target = (Player) sender;
					}
					int val = Integer.parseInt(args[2 + offset]);
					
					if (target != null) {
						Profession prof = ProfessionManager.getAccount(target.getUniqueId()).get(ProfessionType.valueOf(args[1 + offset].toUpperCase()));
						if (args[0].equalsIgnoreCase("addlevel")) {
							prof.setLevel(Math.min(prof.getLevel() + val, 60));
							sender.sendMessage("§4[§c§lMLMC§4] §7Successfully added player level.");
						}
						else {
							prof.addExp(p, val);
							sender.sendMessage("§4[§c§lMLMC§4] §7Successfully added player exp.");
						}
					}
					return true;
				}
				// prof setlevel/exp [player] [type] [level]
				else if (args[0].equalsIgnoreCase("setlevel") || args[0].equalsIgnoreCase("setexp")) {
					Player target = null;
					int offset = 0;
					if (args.length == 4) {
						target = Bukkit.getPlayer(args[1]);
						offset++;
					}
					else if (args.length == 3) {
						target = (Player) sender;
					}
					int val = Integer.parseInt(args[2 + offset]);
					
					if (target != null) {
						Profession prof = ProfessionManager.getAccount(target.getUniqueId()).get(ProfessionType.valueOf(args[1 + offset].toUpperCase()));
						if (args[0].equalsIgnoreCase("setlevel")) {
							prof.setLevel(val);
							sender.sendMessage("§4[§c§lMLMC§4] §7Successfully set player level.");
						}
						else {
							prof.setLevel(val);
							sender.sendMessage("§4[§c§lMLMC§4] §7Successfully set player exp.");
						}
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("createslot")) {
					if (args.length == 1) {
						ProfessionsMethods.createSlot(p, p.getInventory().getItemInMainHand());
						p.updateInventory();
					}
					else {
						p = Bukkit.getPlayer(args[1]);
						ItemStack item = p.getInventory().getItemInMainHand();
						if (item == null || item.getType().isAir()) {
							Util.sendMessage(p, "§cYou're not holding anything in your hand!");
							return true;
						}
						NBTItem nbti = new NBTItem(item);
						if (!nbti.hasKey("gear") || !nbti.hasKey("level")) {
							Util.sendMessage(p, "§cThis item cannot be repaired! It may be outdated.");
							return true;
						}
						new CreateSlotInventory(main, p);
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("checkaugments")) {
					if (args.length == 1) {
						sender.sendMessage(AugmentManager.getPlayerAugments((Player) sender).toString());
					}
					else {
						sender.sendMessage(AugmentManager.getPlayerAugments(Bukkit.getPlayer(args[1])).toString());
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("start")) {
					MinigameManager.startMinigame(Bukkit.getPlayer(args[2]), Integer.parseInt(args[1]));
					return true;
				}
				else if (args[0].equalsIgnoreCase("debug")) {
					main.debug = !main.debug;
					p.sendMessage("Debug set to " + main.debug);
					return true;
				}
				else if (args.length == 2 && args[0].equalsIgnoreCase("debug")) {
					sender.sendMessage(Professions.viewingInventory.toString());
				}
				// /prof givepaint [player] R G B
				else if (args[0].equalsIgnoreCase("1paint")) {
					ProfessionsMethods.givePaint(Bukkit.getPlayer(args[1]), args[2], args[3], args[4]);
					return true;
				}
				// /prof artifact <playername>
				else if (args[0].equalsIgnoreCase("artifact")) {
					if (args.length == 2) {
						ProfessionsMethods.artifactItem(Bukkit.getPlayer(args[1]));
						return true;
					}
					else {
						ProfessionsMethods.artifactItem(p);
					}
				}
				// /prof give/get [essence/oretype/repair/augment] <aug> [level] [amount]
				else if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
					int offset = 0;
					String aug = "";
					int amt = 1;
					if (Bukkit.getPlayer(args[1]) != null) {
						p = Bukkit.getPlayer(args[1]);
						offset++;
					}
					String type = args[1 + offset];
					// Augment-specific parsing
					if (type.equalsIgnoreCase("augment")) {
						if (!StringUtils.isNumeric(args[2 + offset])) {
							aug = args[2 + offset].replaceAll("_", " ");
							offset++;
						}
					}

					int lv = Integer.parseInt(args[2 + offset]);
					int id = lv;
					lv -= lv % 5; // Round to nearest 5
					if (args.length > 3 + offset) {
						amt = Integer.parseInt(args[3 + offset]);
					}

					if (type.equalsIgnoreCase("augment")) {
						Augment augment = null;
						if (AugmentManager.isDroptable(aug)) {
							augment = AugmentManager.getViaDroptable(aug, lv);
						}
						else if (AugmentManager.hasAugment(aug.toLowerCase())) {
							augment = AugmentManager.getFromCache(aug.toLowerCase(), lv);
						}
						else {
							augment = AugmentManager.getViaDroptable("default", lv);
						}
						ItemStack item = augment.getItem(p);
						item.setAmount(amt);
						p.getInventory().addItem(item);
						Util.sendMessage(sender, "&7Successfully gave " + augment.getLine() + " §7to " + p.getName() + "!");
					}
					else if (type.equalsIgnoreCase("repair")) {
						ItemStack item = getRepairItem(lv);
						item.setAmount(amt);
						p.getInventory().addItem(item);
						Util.sendMessage(sender, "&7Successfully gave " + amt + " Lv " + lv + " repairs to " + p.getName() + "!");
					}
					else if (type.equalsIgnoreCase("item")) {
						StorageManager.givePlayer(p, id, amt);
						Util.sendMessage(sender, "&7Successfully gave " + amt + " " + StorageManager.getItem(id).getDisplay() +
								" to " + p.getName() + "!");
					}
					return true;
				}
				// /prof vouch knowledge [key] [display]
				else if (args[0].equalsIgnoreCase("vouch") && args[1].equalsIgnoreCase("knowledge") && args.length == 4) {
					String key = args[2];
					String display = args[3].replaceAll("_", " ").replaceAll("&", "§");
					ItemStack item = new ItemStack(Material.PAPER);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName("§7Recipe: " + display);
					meta.setLore(voucherLore);
					item.setItemMeta(meta);
					NBTItem nbti = new NBTItem(item);
					nbti.setString("knowledge", key);
					nbti.setString("knowledge-display", display);
					p.getInventory().addItem(nbti.getItem());
					return true;
				}
			}
		}
		return true;
	}
	
	// Repair
	public static ItemStack getRepairItem(int level) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4[Lv " + level + "] §cRepair Kit");
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();

		lore.add("§6Drag onto item to use§0");
		lore.add("§cOnly works on quest items");
		lore.add("§7Compatibility: Up to §4Lv " + level);
		lore.add("§7Effect: Restores durability of an item");
		lore.add("§7Potency: §e40%");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		NBTItem nbti = new NBTItem(item);
		nbti.setInteger("level", level);
		nbti.setDouble("potency", 40D);
		
		return nbti.getItem();
	}
}