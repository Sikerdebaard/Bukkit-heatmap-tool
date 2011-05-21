package net.hondev.bukkit.heatmap;

import org.bukkit.Chunk;
import org.bukkit.entity.CreatureType;
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
		Chunk chunk = event.getLocation().getBlock().getChunk();
		plugin.registerSpawn(chunk.getX(), chunk.getZ(), chunk.getWorld().getId(), event.getCreatureType().ordinal());
	}
}
