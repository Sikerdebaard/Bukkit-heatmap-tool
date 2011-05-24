/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

package net.hondev.bukkit.heatmap;

import org.bukkit.Chunk;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;


public class CreatureListener extends EntityListener  {
	public static HeatMap plugin;
	
	public CreatureListener(HeatMap instance){
		super();
		CreatureListener.plugin = instance;
	}
	
	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if(event.isCancelled()) // ignore canceled spawns
			return;
		
		if(event.getCreatureType() == CreatureType.WOLF && event.getEntity() instanceof Wolf && ((Wolf) event.getEntity()).isTamed()) // ignore tamed wolfs due to respawn (bug?)
			return;
		
		Chunk chunk = event.getLocation().getBlock().getChunk();
		plugin.registerSpawn(chunk.getX(), chunk.getZ(), chunk.getWorld().getId(), event.getCreatureType().ordinal(), System.currentTimeMillis());
	}
}
