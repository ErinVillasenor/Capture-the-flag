package com.gmail.firework4lj.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.gmail.firework4lj.main.Main;

	/* How this class works:
	 * Player enters base command '/ctf'
	 * Player receives message on other commands for help menu and joining.
	 * 
	 * Player enters '/ctf help'
	 * Player receives help information expanding on all commands.
	 * 
	 * Player enters '/ctf join'
	 * If the ctfingame hashmap is empty, game is started. Flags are deposited, player location is stored in a hashmap for leaving the game.
	 * Player inventory and xp is also stored in a hashmap for leaving the game.
	 * Red and blue team score hashmaps are set to 0, allowing for easier adding of scores later.
	 * player inventory is cleared, player gamemode is set to adventure mode, and finally is teleported to spawn and sent a message to join blue or red.
	 * --If the player is not the first on the ctfingame hashmap, all of the above happens except flags being deposited and teamscores being set.
	 * 
	 * Player enters '/ctf (team)'
	 * Player is checked for being in the game already, being on red team already, or being on blue team already. If only in game condition is met, 
	 * player is then added to the hashmap teamred or teamblue. Player is finally teleported to the applicable teams spawn.
	 * Players name is changed to either red or blue, and if the name is more than 12 characters long, it is shortened to allow for color change.
	 */

public class CommandsMain implements CommandExecutor{

	private Main main;
	public CommandsMain(Main Main) {
		this.main = Main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
	// Main capture the flag command handler.
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("ctf")){
			// Check to see if an arena exists.
			try{
				if(main.getConfig().getString(Main.currentarena.get("arena")+".mains.w").isEmpty()){
					main.msg(p, ChatColor.DARK_RED+"There are no arenas currently setup! Ask an administrator to set one up with /ctfsetup");
				}else{
					onCtfCommand(sender, cmd, label, args);
				}
			}catch(Exception e){
				main.msg(p, ChatColor.DARK_RED+"There are no arenas currently setup! Ask an administrator to set one up with /ctfsetup");
			}
		}
		return false;
}
	
	public void onCtfCommand(CommandSender sender, Command cmd, String label,String[] args){
		// Variables:
		Player p = (Player) sender;
		ItemStack redflag = new ItemStack(Material.WOOL, 1,DyeColor.RED.getData());
		ItemStack blueflag = new ItemStack(Material.WOOL, 1,DyeColor.BLUE.getData());
		String redName = ChatColor.RED + p.getName() + ChatColor.WHITE;
		String blueName = ChatColor.BLUE + p.getName() + ChatColor.WHITE;
		String defaultName = ChatColor.WHITE + p.getName()+ ChatColor.WHITE;
		// Locations:
		Location LobbySpawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".mains.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.z"));
		Location RedFlag = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.z"));
		Location BlueFlag = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.z"));
		Location bluespawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".blues.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.z"));
		Location redspawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".reds.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.z"));
		
		ItemMeta rflag = redflag.getItemMeta();
		ItemMeta bflag = blueflag.getItemMeta();
		
		rflag.setDisplayName("Redflag");
		redflag.setItemMeta(rflag);
		bflag.setDisplayName("Blueflag");
		blueflag.setItemMeta(bflag);
		
		if(args.length == 0){
			p.sendMessage(ChatColor.DARK_RED+"---Capture the Flag---");
			p.sendMessage(ChatColor.GOLD+"Welcome to Capture the Flag version 3.0");
			p.sendMessage(ChatColor.AQUA+"To join the game, type /ctf join");
			p.sendMessage(ChatColor.AQUA+"To get a full list of commands, type /ctf help");
			p.sendMessage(ChatColor.DARK_RED+"---Capture the Flag---");
		}else if(args.length == 1){
			// Capture the flag joining handler
			
			if(args[0].equalsIgnoreCase("join")){
				onCtfJoin(p, LobbySpawn, BlueFlag, RedFlag, blueflag, redflag);
				
			}else if(args[0].equalsIgnoreCase("red")){
				if(Main.ctfingame.containsKey(p.getName())){
					Main.teamblue.remove(p.getName());
					p.setGameMode(GameMode.ADVENTURE);
					main.msg(p, ChatColor.GREEN + "You are now on the "+ ChatColor.RED + "red " + ChatColor.GREEN + "team");
					p.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 14));
					Main.teamred.put(p.getName(), "red");
					main.msg(p, ChatColor.GREEN+ "Choose a class with /classes!");
					p.teleport(redspawn);
					if (p.getName().length() <= 12) {
						p.setDisplayName(redName);
						p.setPlayerListName(redName);
					} else {
						p.setPlayerListName(redName.substring(0, 13));
						p.setDisplayName(redName.substring(0, 13));
					}
				}else{
					main.msg(p, ChatColor.RED+"You must join the game first! Use /ctf join");
				}
					
					
			}else if(args[0].equalsIgnoreCase("blue")){
				
				if(Main.ctfingame.containsKey(p.getName())){
				Main.teamred.remove(p.getName());
				p.setGameMode(GameMode.ADVENTURE);
				main.msg(p, ChatColor.GREEN+"You are now on the "+ ChatColor.BLUE + "blue " + ChatColor.GREEN + "team");
				p.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 11));
				Main.teamblue.put(p.getName(), "blue");
				main.msg(p, ChatColor.GREEN+"Choose a class with /classes!");
				p.teleport(bluespawn);
				if (p.getName().length() <= 12) {
					p.setDisplayName(blueName);
					p.setPlayerListName(blueName);
				} else {
					p.setPlayerListName(blueName.substring(0, 13));
					p.setDisplayName(blueName.substring(0, 13));
				}
			}else{
				main.msg(p, ChatColor.RED+"You must join the game first! Use /ctf join");
			}
				
				
			}else if(args[0].equalsIgnoreCase("leave")){
			
				onCtfLeave(p, BlueFlag, RedFlag, blueflag, redflag, defaultName);
				
			}else if(args[0].equalsIgnoreCase("help")){
				main.msg(p, ChatColor.DARK_RED+"---Capture the Flag---");
				main.msg(p, ChatColor.GOLD+"Player Commands:");
				main.msg(p, ChatColor.AQUA+"/ctf - Main command for the Capture the Flag plugin.");
				main.msg(p, ChatColor.AQUA+"/ctf join - Allows you to join the current game.");
				main.msg(p, ChatColor.AQUA+"/ctf red OR /ctf blue - After joining the game, allows you to choose a team.");
				main.msg(p, ChatColor.AQUA+"/ctf leave - Allows you to safely exit the current game.");
				main.msg(p, ChatColor.AQUA+"/ctf help - Displays this message.");
				main.msg(p, ChatColor.AQUA+"/classes - List available classes.");
				main.msg(p, ChatColor.AQUA+"/classes (class name) - Switches your current class to the class specified.");
				main.msg(p, ChatColor.AQUA+"/vote (arena name) - Allows voting for a new arena during the end of a game.");
				main.msg(p, ChatColor.GOLD+"Operator Commands:");
				main.msg(p, ChatColor.AQUA+"/ctfsetup new (arena name) - Allows the creation of a new arena.");
				main.msg(p, ChatColor.AQUA+"/arenasetup (arena name) (arena spawn point) - Allows you to edit the arena specified spawn points.");
				main.msg(p, ChatColor.AQUA+"/ctf classcreate (name) - Allows for a copy of your current inventory to be saved as a playable class.");
			
			}else if(args[0].equalsIgnoreCase("stats")){
				main.msg(p, ChatColor.DARK_RED+"---Capture the Flag---");
				if(Main.redflag.containsKey("redflag")){
					main.msg(p, ChatColor.AQUA+"Red flag: "+ChatColor.RED+Main.redflag.get("redflag"));
				}else{
					main.msg(p, ChatColor.AQUA+"Red flag: "+ChatColor.RED+"Nobody");
				}
				if(Main.blueflag.containsKey("blueflag")){
					main.msg(p, ChatColor.AQUA+"Blue flag: "+ChatColor.BLUE+Main.blueflag.get("blueflag"));
				}else{
					main.msg(p, ChatColor.AQUA+"Blue flag: "+ChatColor.BLUE+"Nobody");
				}
				main.msg(p, ChatColor.AQUA+"Red Score: "+ChatColor.RED+Main.redscore.get("red"));
				main.msg(p, ChatColor.AQUA+"Blue Score: "+ChatColor.BLUE+Main.bluescore.get("blue"));
				
			}
			}else if(args.length == 2){
				if(args[0].equalsIgnoreCase("classcreate")){
					if(p.isOp()){
						PlayerInventory inv = p.getInventory();
							YamlConfiguration c = new YamlConfiguration();
							c.set("Classes."+args[1]+".items", inv.getContents());
							c.set("Classes."+args[1]+".armor", inv.getArmorContents());
							try {
								c.save(new File("plugins/Capture-the-Flag/classes/"+args[1]+".yml"));
								main.getConfig().set("Classes."+args[1], args[1]);
								main.saveConfig();
								main.msg(p, ChatColor.AQUA+"Class created sucessfully");
							} catch (IOException e) {
								e.printStackTrace();
							}
					}else{
						main.msg(p, ChatColor.DARK_RED+"You must be opped to use this command!");
					}
			}else if(args[0].equalsIgnoreCase("classdelete")){
				if(p.isOp()){
					File file = new File("plugins/Capture-the-Flag/classes/"+args[1]+".yml");
					file.delete();
					main.getConfig().set("Classes."+args[1], null);
					main.saveConfig();
					main.msg(p, ChatColor.AQUA+"Class deleted sucessfully");
				}else{
					main.msg(p, ChatColor.DARK_RED+"You must be opped to use this command!");
				}
			}else{
				p.sendMessage(ChatColor.DARK_RED+"---Capture the Flag---");
				p.sendMessage(ChatColor.GOLD+"Welcome to Capture the Flag version 3.0");
				p.sendMessage(ChatColor.AQUA+"To join the game, type /ctf join");
				p.sendMessage(ChatColor.AQUA+"To get a full list of commands, type /ctf help");
				p.sendMessage(ChatColor.DARK_RED+"---Capture the Flag---");
			}
		}else{
			p.sendMessage(ChatColor.DARK_RED+"---Capture the Flag---");
			p.sendMessage(ChatColor.GOLD+"Welcome to Capture the Flag version 3.0");
			p.sendMessage(ChatColor.AQUA+"To join the game, type /ctf join");
			p.sendMessage(ChatColor.AQUA+"To get a full list of commands, type /ctf help");
			p.sendMessage(ChatColor.DARK_RED+"---Capture the Flag---");
		}
	}
	
	public void onCtfJoin(Player p, Location LobbySpawn, Location BlueFlag, Location RedFlag, ItemStack blueflag, ItemStack redflag){
		
		if(Main.ctfingame.containsKey(p.getName())){
			main.msg(p, ChatColor.DARK_RED+"You are already in the game!");
		}else{
			// Startup sequence ON FIRST PLAYER JOIN
			p.getActivePotionEffects().clear();
			Main.mainenterinv.put(p.getName(), p.getInventory().getContents());
			Main.armorenterinv.put(p.getName(), p.getInventory().getArmorContents());
			Main.xplevel.put(p.getName(), p.getExp());
			Main.hungerlevel.put(p.getName(), p.getFoodLevel());
			Main.healthlevel.put(p.getName(), p.getHealth());
			Main.joinx.put(p.getName(), p.getLocation().getBlockX());
			Main.joiny.put(p.getName(), p.getLocation().getBlockY());
			Main.joinz.put(p.getName(), p.getLocation().getBlockZ());
			Main.joinw.put(p.getName(), p.getLocation().getWorld().getName());
			p.setGameMode(GameMode.ADVENTURE);
			p.teleport(LobbySpawn);
			p.getInventory().clear();
			p.getInventory().setHelmet(new ItemStack(Material.AIR));
			p.getInventory().setChestplate(new ItemStack(Material.AIR));
			p.getInventory().setLeggings(new ItemStack(Material.AIR));
			p.getInventory().setBoots(new ItemStack(Material.AIR));
			p.setHealth(20);
			p.setFoodLevel(20);
			if(Main.ctfingame.isEmpty()){
				Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")).dropItemNaturally(RedFlag, redflag).setVelocity(new Vector(0D, 0D, 0D));
				Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")).dropItemNaturally(BlueFlag, blueflag).setVelocity(new Vector(0D, 0D, 0D));
				Main.redscore.put("red", 0);
				Main.bluescore.put("blue", 0);
			}
			Main.ctfingame.put(p.getName(), true);
			main.msg(p, ChatColor.GREEN+ "Ctf game has started! Choose a team with /ctf "+ ChatColor.RED + "red " + ChatColor.GREEN + "or /ctf "+ ChatColor.BLUE + "blue");
			// END Game startup
		}
	}
	
	public void onCtfLeave(Player p, Location BlueFlag, Location RedFlag, ItemStack blueflag, ItemStack redflag, String defaultName){
		
		Location JoinLocation = new Location(Bukkit.getWorld(Main.joinw.get(p.getName())), ((Integer) Main.joinx.get(p.getName())), ((Integer) Main.joiny.get(p.getName())), ((Integer) Main.joinz.get(p.getName())));

		if(Main.ctfingame.containsKey(p.getName())){
			// TODO: Add a hashmap to not fully heal and replenish hunger for the player
			p.setHealth(Main.healthlevel.get(p.getName()));
			p.setFoodLevel(Main.hungerlevel.get(p.getName()));
			Main.hungerlevel.remove(p.getName());
			Main.healthlevel.remove(p.getName());
			Main.ctfingame.remove(p.getName());
			Main.teamred.remove(p.getName());
			Main.teamblue.remove(p.getName());
			Main.ctfclass.remove(p.getName());

			if (p.getInventory().contains(redflag)) {
				for(String pl : Main.ctfingame.keySet()){
					main.msg(Bukkit.getPlayerExact(pl), ChatColor.BLUE+ p.getName() + ChatColor.GOLD+ " has dropped the " + ChatColor.RED + "red "+ ChatColor.GOLD + "flag!");
				}
				Main.redflag.clear();
				Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")).dropItemNaturally(RedFlag, redflag).setVelocity(new Vector(0D, 0D, 0D));
				} else if (p.getInventory().contains(blueflag)) {
				for(String pl : Main.ctfingame.keySet()){
					main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED+ p.getName() + ChatColor.GOLD+ " has dropped the " + ChatColor.BLUE+ "blue " + ChatColor.GOLD + "flag!");
				}
				Main.blueflag.clear();
				Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")).dropItemNaturally(BlueFlag, blueflag).setVelocity(new Vector(0D, 0D, 0D));
			}
			List<Entity> entlist = Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")).getEntities();
			for (Entity current : entlist) {
				if (current instanceof Item) {
					try{
					if (((Item) current).getItemStack().getItemMeta().getDisplayName().equals("Redflag") || ((Item) current).getItemStack().getItemMeta().getDisplayName().equals("Blueflag")){
							current.remove();
					}
					} catch (NullPointerException e){
						// One of the flags is gone.
					}
				}
			}
			p.getInventory().setHelmet(new ItemStack(Material.AIR));
			p.getInventory().setBoots(new ItemStack(Material.AIR));
			p.getInventory().setChestplate(new ItemStack(Material.AIR));
			p.getInventory().setLeggings(new ItemStack(Material.AIR));
			p.getActivePotionEffects().clear();
			p.getInventory().clear();
			p.teleport(JoinLocation);
			Main.joinx.remove(p.getName());
			Main.joiny.remove(p.getName());
			Main.joinz.remove(p.getName());
			Main.joinw.remove(p.getName());
			p.setGameMode(GameMode.SURVIVAL);
			main.msg(p, ChatColor.GREEN+ "You have left the game");
			p.getInventory().setArmorContents(Main.armorenterinv.get(p.getName()));
			p.getInventory().setContents(Main.mainenterinv.get(p.getName()));
			p.setExp(Main.xplevel.get(p.getName()));
			p.updateInventory();
			if (p.getName().length() <= 12) {
				p.setPlayerListName(defaultName);
				p.setDisplayName(defaultName);
			} else {
				p.setPlayerListName(defaultName.substring(0, 14));
				p.setDisplayName(defaultName.substring(0, 13));
			}
			}else{
				main.msg(p, ChatColor.AQUA+"You are not in a game!");
			}
	}
}

