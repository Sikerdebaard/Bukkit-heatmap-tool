/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

package net.hondev.bukkit.heatmap;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.java.JavaPlugin;

public class HeatMap extends JavaPlugin {
	private static final Logger log = Logger.getLogger("Minecraft");
	private CreatureListener creatureListener;
	private WorldListener worldSaveListener;
	private SpawnBuffer buffer;
	
	private long lastSave;
	
	public HeatMap(){
		super();
		buffer = new SpawnBuffer(log);
		lastSave = 0;
	}
	
	@Override
	public void onDisable() {
		saveBuffer();
		log.info("Heatmap plugin stopped.");
	}
	
	@Override
	public void onEnable() {
		creatureListener = new CreatureListener(this);
		worldSaveListener = new WorldSaveListener(this);
		getServer().getPluginManager().registerEvent(Event.Type.CREATURE_SPAWN, creatureListener, Event.Priority.Lowest, this);
		getServer().getPluginManager().registerEvent(Event.Type.WORLD_SAVE, worldSaveListener, Event.Priority.Lowest, this);
		log.info("Heatmap plugin started.");
	}
	
	protected void saveBuffer(){
		if(System.currentTimeMillis() - lastSave < 900000L)
			return;
		
		buffer.saveBuffer();
		getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "[Heatmap] buffers flushed.");
		lastSave = System.currentTimeMillis();
	}
	
	public synchronized void registerSpawn(int x, int z, long world, int creature){
		buffer.registerSpawn(x, z, world, creature);
	}
}
