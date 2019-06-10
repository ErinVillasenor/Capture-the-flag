package com.gmail.firework4lj.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.gmail.firework4lj.main.Main;

public class PlayerAttack implements Listener{

	private Main main;
	public PlayerAttack(Main Main) {
		this.main = Main;
	}
	
	@EventHandler
	public void onPvp(EntityDamageByEntityEvent event){ //where event is a player attacking another player, this function negates team damage
		if ((event.getEntity() instanceof Player) && (event.getDamager() instanceof Player)) {
			Player attacker = (Player) event.getDamager();
			Player player = (Player) event.getEntity();
			if (Main.teamred.containsKey(attacker.getName()) && (Main.teamred.containsKey(player.getName()))  && Main.ctfingame.containsKey(player.getName())) {
				event.setCancelled(true);
				main.msg(attacker, ChatColor.RED+"Hey, no attacking your own team, red!");
				//if red team member tries to attack fellow red team member
			} else if (Main.teamblue.containsKey(attacker.getName()) && (Main.teamblue.containsKey(player.getName()))  && Main.ctfingame.containsKey(player.getName())) {
				event.setCancelled(true);
				main.msg(attacker, ChatColor.BLUE+"Hey, no attacking your own team, blue!");
			}//if blue team member tries to attack fellow blue team member 
			else {
				event.setCancelled(false);
			}
		}
	}	
}
