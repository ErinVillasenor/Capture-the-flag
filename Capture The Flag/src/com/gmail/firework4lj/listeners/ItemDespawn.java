package com.gmail.firework4lj.listeners;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;

import com.gmail.firework4lj.main.Main;

public class ItemDespawn implements Listener{

	private Main main;
	public ItemDespawn(Main Main) {
		this.main = Main;
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent e) {
		if(e.getEntity() instanceof Item) {
			if(e.getEntity().getItemStack().getItemMeta().getDisplayName() == "Redflag") {
				e.setCancelled(true);
			}else if(e.getEntity().getItemStack().getItemMeta().getDisplayName() == "Blueflag") {
				e.setCancelled(true);
			}else{
				e.setCancelled(false);
			}
		}
	}
}
