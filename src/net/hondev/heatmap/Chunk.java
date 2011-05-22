/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

package net.hondev.heatmap;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.CreatureType;

public class Chunk {
	private static final int MAX_GAP = 600000;
	
	private long lastTime;
	private long activityBegin;
	protected long activity;
	protected int load;
	protected int spawns;
	
	protected Map<CreatureType, Integer> spawnCounter;
	
	protected float avgSpawnsPerHour;
	
	protected int x, z;
	
	public Chunk(int x, int z){
		activityBegin = Long.MIN_VALUE;
		activity = 0;
		load = 0;
		spawns = 0;
		
		this.spawnCounter = new HashMap<CreatureType, Integer>();
		
		this.x = x;
		this.z = z;
	}
	
	public void registerSpawn(CreatureType creature, long time){ // very... very... long time.
		if(activityBegin == Long.MIN_VALUE){
			activityBegin = time;
			lastTime = time;
		}
		
		if(time - lastTime > MAX_GAP){
			activity += (lastTime - activityBegin);
			activityBegin = time;
			load++;
		}
		
		Integer i;
		if((i = spawnCounter.get(creature)) != null)
			spawnCounter.put(creature, Integer.valueOf(i.intValue() + 1));
		else 
			spawnCounter.put(creature, Integer.valueOf(1));
		
		
		lastTime = time;
		
		spawns++;
	}
	
	public void finishSpawns(){
		activity += (lastTime - activityBegin);
		load++;
		
		if(activity == 0)
			activity = MAX_GAP - 1;
		
		
		avgSpawnsPerHour = spawns / ((activity + 0f) / 60000);
	}
}
