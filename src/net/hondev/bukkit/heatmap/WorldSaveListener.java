package net.hondev.bukkit.heatmap;

import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldSaveEvent;


public class WorldSaveListener extends WorldListener {
	protected static HeatMap plugin;
	
	public WorldSaveListener(HeatMap plugin){
		super();
		WorldSaveListener.plugin = plugin;
	}
	
	@Override
	public void onWorldSave(WorldSaveEvent event){
		plugin.saveBuffer();
	}
}
