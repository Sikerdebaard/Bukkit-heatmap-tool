/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

package net.hondev.heatmap;

public class Chunk {
	private static final int MAX_GAP = 600000;
	
	private long lastTime;
	private long activityBegin;
	protected long activity;
	protected int load;
	protected int spawns;
	
	protected float avgSpawnsPerHour;
	
	protected int x, z;
	
	public Chunk(int x, int z){
		activityBegin = Long.MIN_VALUE;
		activity = 0;
		load = 0;
		spawns = 0;
		
		this.x = x;
		this.z = z;
	}
	
	public void registerSpawn(long time){ // very... very... long time.
		if(activityBegin == Long.MIN_VALUE){
			activityBegin = time;
			lastTime = time;
		}
		
		if(time - lastTime > MAX_GAP){
			activity += (lastTime - activityBegin);
			activityBegin = time;
			load++;
		}
		
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
