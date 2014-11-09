package com.gmail.firework4lj.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.firework4lj.main.Main;

public class PlayerRespawn {

	private Main main;

	public PlayerRespawn(Main Main) {
		this.main = main;
	}

	public HashMap<String, Inventory> items = new HashMap<String, Inventory>();

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		// Variables
		String pn = event.getPlayer().getName();
		Player p = event.getPlayer();
		// Locations
		Location redspawn = new Location(p.getWorld(), main.getConfig().getDouble("location.Xredspawn"), main.getConfig().getDouble("location.Yredspawn"), main.getConfig().getDouble("location.Zredspawn"));
		Location bluespawn = new Location(p.getWorld(), main.getConfig().getDouble("location.Xbluespawn"), main.getConfig().getDouble("location.Ybluespawn"), main.getConfig().getDouble("location.Zbluespawn"));
		Location playerspawn = new Location(p.getWorld(), main.getConfig().getDouble("location.Xspawn"), main.getConfig().getDouble("location.Yspawn"), main.getConfig().getDouble("location.Zspawn"));

		if (Main.teamred.containsKey(pn)&&Main.ctfingame.containsKey(p.getName())) {
			event.setRespawnLocation(redspawn);
			p.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 14));
			if(Main.ctfclass.containsKey(pn)){
			p.performCommand(Main.ctfclass.get(p.getName()));
			}else{
				
			}
		} else if (Main.teamblue.containsKey(pn)&&Main.ctfingame.containsKey(p.getName())) {
			event.setRespawnLocation(bluespawn);
			p.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 11));
			if(Main.ctfclass.containsKey(pn)){
			p.performCommand(Main.ctfclass.get(p.getName()));
			}else{	
			}
		} else {
			if(Main.ctfingame.containsKey(p.getName())){
			event.setRespawnLocation(playerspawn);
			}else {
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		String pn = e.getEntity().getName();
		
		Location bluef = new Location(p.getWorld(), main.getConfig().getDouble("location.Xblueflagspawn"), main.getConfig().getDouble("location.Yblueflagspawn"), main.getConfig().getDouble("location.Zblueflagspawn"));
		Location redf = new Location(p.getWorld(), main.getConfig().getDouble("location.Xredflagspawn"), main.getConfig().getDouble("location.Yredflagspawn"), main.getConfig().getDouble("location.Zredflagspawn"));
		
		if (p.getInventory().contains(new ItemStack(Material.WOOL, 1, (short) 11))&&Main.ctfingame.containsKey(p.getName())) {
			for(String pl : Main.ctfingame.keySet()){
				main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED + pn + ChatColor.GOLD+ " Has dropped the " + ChatColor.BLUE + "blue "+ ChatColor.GOLD + "flag!");
			}
			e.getDrops().clear();
			p.getWorld().dropItemNaturally(bluef,new ItemStack(Material.WOOL, 1, (byte) 11)).setVelocity(new Vector(0, 0, 0));
			Main.blueflag.clear();

		} else if (p.getInventory().contains(new ItemStack(Material.WOOL, 1, (short) 14))&&Main.ctfingame.containsKey(p.getName())) {
			for(String pl : Main.ctfingame.keySet()){
				main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED + pn + ChatColor.GOLD+ " Has dropped the " + ChatColor.RED + "red "+ ChatColor.GOLD + "flag!");
			}
			e.getDrops().clear();
			p.getWorld().dropItemNaturally(redf,new ItemStack(Material.WOOL, 1, (byte) 14)).setVelocity(new Vector(0, 0, 0));
			Main.redflag.clear();
		} else if(Main.ctfingame.containsKey(p.getName())){
			e.getDrops().clear();
		}else{
			
		}
	}
	
}
