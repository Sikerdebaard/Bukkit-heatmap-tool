/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

package net.hondev.heatmap;

import java.util.HashMap;
import java.util.Map;

public class Universe {
	private Map<Long, World> worlds;
	
	private long activityBegin;
	private long lastTime;
	private long activity;
	private int load;
	
	private int spawns;
	
	public Universe(){
		worlds = new HashMap<Long, World>();
		activityBegin = Long.MIN_VALUE;
		activity = 0;
		load = 0;
		spawns = 0;
	}
	
	public void registerSpawn(Spawn spawn){
		World wrld;
		
		long time = spawn.getTime();
		
		if(activityBegin == Long.MIN_VALUE){
			activityBegin = time;
			lastTime = time;
		}
		
		if(time - lastTime > 600000){
			activity += (lastTime - activityBegin);
			activityBegin = time;
			load++;
		}
		
		lastTime = time;
		
		if((wrld = worlds.get(Long.valueOf(spawn.getWorld()))) == null){
			worlds.put(Long.valueOf(spawn.getWorld()), (wrld = new World(spawn.getWorld())));
		}
		wrld.registerSpawn(spawn.getChunkX(), spawn.getChunkZ(), spawn.getCreature(), spawn.getTime());
		
		spawns++;
	}
	
	public void finishSpawns(){
		activity += (lastTime - activityBegin);
		load++;
		
		for(Long l : worlds.keySet())
			worlds.get(l).finishSpawns();
		
		System.out.println("\n--Universe finished--");
		System.out.println("Worlds: " + worlds.keySet().size());
		System.out.println("Spawns: " + spawns);
		System.out.println("Activity: " + (activity / 1000 / 60) + " mins.");
		System.out.println("");
		
		
	}
}
