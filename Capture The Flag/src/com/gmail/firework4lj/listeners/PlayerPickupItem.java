package com.gmail.firework4lj.listeners;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.gmail.firework4lj.main.Main;

public class PlayerPickupItem implements Listener{

	private Main main;
	public PlayerPickupItem(Main Main) {
		this.main = Main;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		// String Variables:
		Player p = (Player) event.getPlayer();
		String pn = p.getName();
		Item drop = event.getItem();
		// Locations setter for the Red flag:
		Location RedFlag = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.z"));
		// Locations setter for the Blue flag:
		Location BlueFlag = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.z"));

		// variables for the Red and Blue flag (materials what it is made of)
		ItemStack redflag = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData());;
		ItemStack blueflag = new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getData());
		
		// Red and Blue Flag for inventory
		ItemMeta rflag = redflag.getItemMeta();
		ItemMeta bflag = blueflag.getItemMeta();
		
		// The main of picking up items, which will determine when a flag is stolen, returned or turned in, which will reset the flag to base, and eventually end the game
		if(Main.ctfingame.containsKey(p.getName())){
			rflag.setDisplayName("Redflag");
			redflag.setItemMeta(rflag);
			bflag.setDisplayName("Blueflag");
			blueflag.setItemMeta(bflag);
			
			//If the redflag gets stolen, put in player inventory and show message
			if (drop.getItemStack().equals(redflag) && Main.teamblue.containsKey(pn)) {
				Main.redflag.put("redflag", pn);
				for(String pl : Main.ctfingame.keySet()){
					main.msg(Bukkit.getPlayerExact(pl), ChatColor.BLUE + pn + ChatColor.RED+ " Stole the redflag!");
				}
			}
			//If the blueflag gets stolen, put in player inventory and show message
			if (drop.getItemStack().equals(blueflag) && Main.teamred.containsKey(pn)) {
				Main.blueflag.put("blueflag", pn);
				for(String pl : Main.ctfingame.keySet()){
					main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED+pn+ChatColor.BLUE+" Stole the blueflag!");
				}
			}
			// When the blue flag that was stolen gets turned in at base
			else if (drop.getItemStack().equals(redflag) && p.getInventory().contains(blueflag)) {
				event.setCancelled(true);
				// A message is shown who turned in the flag
				for(String pl : Main.ctfingame.keySet()){
					main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED + pn + ChatColor.GOLD	+ " Has captured the " + ChatColor.BLUE + "blue teams "+ ChatColor.GOLD + "flag!");
				}
				// A point is added to the scoring team
				int before = Main.redscore.get("red");
				int after = before + 1;
				Main.redscore.put("red", after);
				// The game checks if the max score is reached, if reached the game will anounce the winning team
				if (Main.redscore.get("red") == main.getConfig().getInt("max_points")) {
					for(String pl : Main.ctfingame.keySet()){
						main.msg(Bukkit.getPlayerExact(pl), ChatColor.GOLD + "Good game! Team "+ ChatColor.RED + "red " + ChatColor.GOLD+ "wins! Restarting game in 15 seconds.");
					}
					// calls the Reloadgame function, which is to start a new game
					this.Reloadgame();
				}else{
				// If the max scored wasn't reached, the blue flag gets reset, to its base
				p.getInventory().remove(blueflag);
				Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")).dropItemNaturally(BlueFlag, blueflag).setVelocity(new Vector(0D, 0D, 0D));;
				Main.blueflag.clear();
				}
				if (drop.getItemStack().equals(redflag)) {
					event.setCancelled(true);
				}
			}
			// When the red flag that was stolen gets turned in at base
			else if (drop.getItemStack().equals(blueflag) && p.getInventory().contains(redflag)&& Main.ctfingame.containsKey(p.getName())) {
				event.setCancelled(true);
				// A message is shown who turned in the flag
				for(String pl : Main.ctfingame.keySet()){
					main.msg(Bukkit.getPlayerExact(pl), ChatColor.BLUE + pn + ChatColor.GOLD+ " Has captured the " + ChatColor.RED + "red teams "+ ChatColor.GOLD + "flag!");
				}
				// A point is added to the scoring team
				int before = (int) Main.bluescore.get("blue");
				int after = (int) before + 1;
				Main.bluescore.put("blue", after);
				// The game checks if the max score is reached, if reached the game will anounce the winning team
				if (Main.bluescore.get("blue") == main.getConfig().getInt("max_points")) {
					for(String pl : Main.ctfingame.keySet()){
						main.msg(Bukkit.getPlayerExact(pl), ChatColor.GOLD+"Good game! Team "+ChatColor.BLUE+"blue "+ChatColor.GOLD+"wins! Restarting game in 15 seconds.");
					}
					// calls the Reloadgame function, which is to start a new game
					this.Reloadgame();
				}else{
				// If the max scored wasn't reached, the red flag gets reset, to its base
				p.getInventory().remove(redflag);
				Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")).dropItemNaturally(RedFlag, redflag).setVelocity(new Vector(0D, 0D, 0D));
				Main.redflag.clear();
				}
				if (drop.getItemStack().equals(blueflag)) {
					event.setCancelled(true);
				}
			}
			// safety, that doesn't allow own team to pick up own flag
			if (drop.getItemStack().equals(blueflag) && Main.teamblue.containsKey(pn)) {
				event.setCancelled(true);
				return;
			}
			// safety, that doesn't allow own team to pick up own flag
			if (drop.getItemStack().equals(redflag)&& Main.teamred.containsKey(pn)) {
				event.setCancelled(true);
				return;
			}	
		}
		else{
			event.setCancelled(false);
		}
	}
	
	
	@SuppressWarnings("deprecation")
	// Reloadgame function, which is to start a new game
	public void Reloadgame() {
		// First it will anounce to vote for a new map, and give the options
		for(String pl : Main.ctfingame.keySet()){
			main.msg(Bukkit.getPlayerExact(pl), ChatColor.GREEN+"Please vote for the next map with /vote (arena)");
			main.msg(Bukkit.getPlayerExact(pl), ChatColor.GREEN+"Available maps are:");
			Bukkit.getPlayerExact(pl).sendMessage(ChatColor.BLACK+"["+ChatColor.GOLD+"Ctf"+ChatColor.BLACK+"] "+ChatColor.AQUA+main.getConfig().getConfigurationSection("arenas").getKeys(false));
		}
		// time given to load the new arena and reset all conditions, to allow a new fresh game
		int seconds = 15;
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				Main.currentarena.clear();
				// Finding out which arena has the highest amount of votes
				// if there are no votes it will pick an arena
				if(Main.votes.isEmpty()){
					String arena = main.getConfig().getConfigurationSection("arenas").getKeys(false).iterator().next();
					Main.currentarena.put("arena", "arenas."+arena);
					for(String pl : Main.ctfingame.keySet()){
						main.msg(Bukkit.getPlayerExact(pl), ChatColor.GREEN+"No votes! Loading map: "+ChatColor.GOLD+arena);
					}
				}
				// If players have voted it will pick the arena with most votes
				else{
					int maxValueInMap = (Collections.max(Main.votes.values()));
					for (Entry<String, Integer> entry : Main.votes.entrySet()) {
						if(entry.getValue()==maxValueInMap){
							String arena = entry.getKey();
							Main.currentarena.put("arena", "arenas."+arena);
							for(String pl : Main.ctfingame.keySet()){
								main.msg(Bukkit.getPlayerExact(pl), ChatColor.GREEN+"Loading map: "+ChatColor.GOLD+arena);
							}
						}
					}
				}
				// leaves the current arena
				for(String pl : Main.ctfingame.keySet()){
					Bukkit.getPlayerExact(pl).performCommand("ctf leave");
				}
				// Clean up the flags on restart, so they don't contain the old coordinates, which might not relate to the next arena
				List<Entity> entlist = Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")).getEntities();
				for (Entity current : entlist) {
					if (current instanceof Item) {
						try{
							// clear out all items
							if (((Item) current).getItemStack().getItemMeta().getDisplayName().equals("Redflag") || ((Item) current).getItemStack().getItemMeta().getDisplayName().equals("Blueflag")){
								current.remove();
							}
						} 
						// a catch, so the program won't crash if there is an exception
						catch (NullPointerException e){
							// One of the flags is gone.
						}
					}
				}
				// clear all, so the game is fully reset, which allows the players to start without any conditions from previous game
				Main.blueflag.clear();
				Main.redflag.clear();
				Main.bluescore.clear();
				Main.redscore.clear();
				Main.ctfclass.clear();
				Main.teamblue.clear();
				Main.teamred.clear();
				Main.ctfingame.clear();
				Main.redscore.put("red", 0);
				Main.bluescore.put("blue", 0);
				Main.armorenterinv.clear();
				Main.mainenterinv.clear();
				Main.xplevel.clear();
				Main.voted.clear();
				Main.votes.clear();
			}
		}, (seconds * 20));
	}
}
