package com.gmail.firework4lj.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.firework4lj.main.Main;
import com.gmail.firework4lj.util.IconMenu;

public class Classes implements CommandExecutor{

	private Main Plugin;
	private Main main;
	public Classes(Main Main) {
		this.main = Main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("class")){
			if(Main.ctfingame.containsKey(p.getName())){
				IconMenu Menu = new IconMenu("       Select a Class      ", 27, new IconMenu.OptionClickEventHandler(){
			        public void onOptionClick(IconMenu.OptionClickEvent event) {
			        	main.msg(p, ChatColor.GOLD+"You have chosen " + event.getName());
			          event.setWillClose(true);
			        }
			      }
			      , this.Plugin)
			        .setOption(0, new ItemStack(Material.DIAMOND_SWORD, 1), "§6§lSoldier", new String[] { ChatColor.GREEN+"Normal speed, basic weapons, fair health." })
			        .setOption(1, new ItemStack(Material.BOW, 1), "§6§lSniper", new String[] { ChatColor.GREEN+"Normal speed, good shot, lower health." })
			        .setOption(2, new ItemStack(Material.COMPASS, 1), "§6§lScout", new String[] { ChatColor.GREEN+"Fast, powerful, but low health." })
			      	.setOption(3, new ItemStack(Material.DIAMOND_SWORD, 1), "§6§lHeavy", new String[] { ChatColor.GREEN+"Slow, Lots of health, powerful." })
			      	.setOption(4, new ItemStack(Material.GOLD_SWORD, 1), "§6§lMedic", new String[] { ChatColor.GREEN+"Normal speed, bad weapons, lots of health, powerful healing" });
			      Menu.open(p);
			}else{
				main.msg(p, ChatColor.DARK_RED+"You must join the game first! Use /ctf join");
			}
		}
		return false;
	}
}
