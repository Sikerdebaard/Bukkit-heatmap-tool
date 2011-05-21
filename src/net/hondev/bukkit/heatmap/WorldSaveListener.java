/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

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
