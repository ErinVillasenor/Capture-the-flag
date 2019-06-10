package com.gmail.firework4lj.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.gmail.firework4lj.main.Main;

	/* What this class does:
	* This class is called when The player dies, and when the player respawns.
	* When the player dies, they are checked for if they were holding the flag. If they were holding the flag, the flag is destroyed along with all of there drops, and then the flag is respawned into the game at the flag spawn point.
	*/

public class PlayerRespawn implements Listener{

	private Main main;
	public PlayerRespawn(Main Main) {
		this.main = Main;
	}

	public HashMap<String, Inventory> items = new HashMap<String, Inventory>();

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		// Variables
		String playerName = event.getPlayer().getName();
		//This variable is the relevant player's in-game name, used for printing out to the screen information
		//regarding that player e.g. <playerName> has dropped the flag
		Player player = event.getPlayer();
		//This variable references a relevant player
		
		// Locations
		Location LobbySpawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".mains.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.z"));
		Location redspawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".reds.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.z"));
		Location bluespawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".blues.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.z"));

		
		if (Main.teamred.containsKey(playerName)&&Main.ctfingame.containsKey(player.getName())) {
			if(Main.ctfclass.containsKey(playerName)){
				player.performCommand("classes "+Main.ctfclass.get(player.getName()));
			}
			event.setRespawnLocation(redspawn);
			player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 14));
		} else if (Main.teamblue.containsKey(playerName)&&Main.ctfingame.containsKey(player.getName())) {
			if(Main.ctfclass.containsKey(playerName)){
				player.performCommand("classes "+Main.ctfclass.get(player.getName()));
			}
			event.setRespawnLocation(bluespawn);
			player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 11));
		} else {
			if(Main.ctfingame.containsKey(player.getName())){
			event.setRespawnLocation(LobbySpawn);
			} 
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String playerName = event.getEntity().getName();
		
		ItemStack redflag = new ItemStack(Material.WOOL, 1,DyeColor.RED.getData());
		ItemStack blueflag = new ItemStack(Material.WOOL, 1,DyeColor.BLUE.getData());
		
		Location redf = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.z"));
		Location bluef = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.z"));

		ItemMeta rflag = redflag.getItemMeta();
		ItemMeta bflag = blueflag.getItemMeta();
		
		rflag.setDisplayName("Redflag");
		redflag.setItemMeta(rflag);
		bflag.setDisplayName("Blueflag");
		blueflag.setItemMeta(bflag);
		
		if (player.getInventory().contains(blueflag)&&Main.ctfingame.containsKey(player.getName())) {
			
			event.getDrops().clear();
			Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")).dropItemNaturally(bluef, blueflag).setVelocity(new Vector(0D, 0D, 0D));;
			Main.blueflag.clear();
			event.setDeathMessage(null);
			for(String pl : Main.ctfingame.keySet()){
				main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED + playerName + ChatColor.GOLD+ " Has dropped the " + ChatColor.BLUE + "blue "+ ChatColor.GOLD + "flag!");
			}
		} else if (player.getInventory().contains(redflag)&&Main.ctfingame.containsKey(player.getName())) {
			for(String pl : Main.ctfingame.keySet()){
				main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED + playerName + ChatColor.GOLD+ " Has dropped the " + ChatColor.RED + "red "+ ChatColor.GOLD + "flag!");
			}
			event.setDeathMessage(null);
			event.getDrops().clear();
			Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")).dropItemNaturally(redf, redflag).setVelocity(new Vector(0D, 0D, 0D));
			Main.redflag.clear();
		} else if(Main.ctfingame.containsKey(player.getName())){
			event.getDrops().clear();
		}
	}
	
}
