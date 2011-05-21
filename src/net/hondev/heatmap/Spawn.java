/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

package net.hondev.heatmap;

import org.bukkit.entity.CreatureType;

public class Spawn {
	private int x, z;
	private long time;
	private int creature;
	private long world;
	
	protected Spawn(int x, int z, long world, int creature, long time){
		reset(x, z, time, creature, world);
	}
	
	protected void reset(int x, int z, long time, int creature, long world){
		this.time = time;
		this.x = x;
		this.z = z;
		this.creature = creature;
		this.world = world;
	}
	
	public int getChunkX(){
		return x;
	}
	public int getChunkZ(){
		return z;
	}
	public long getTime(){
		return time;
	}
	public CreatureType getCreature(){
		return CreatureType.values()[creature];
	}
	public long getWorld(){
		return world;
	}
}
