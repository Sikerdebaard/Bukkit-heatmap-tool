/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

package net.hondev.bukkit.heatmap;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.bukkit.Chunk;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;


public class CreatureListener extends EntityListener  {
	public static HeatMap plugin;
	
	private DelayQueue<Spawn> queue;
	
	public CreatureListener(HeatMap instance){
		super();
		queue = new DelayQueue<Spawn>();
		CreatureListener.plugin = instance;
	}
	
	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if(event.getCreatureType() == CreatureType.WOLF && event.getEntity() instanceof Wolf && ((Wolf) event.getEntity()).isTamed()) // ignore tamed wolfs due to respawn (bug?)
			return;
		
		queue.add(new Spawn(event));
		processQueue();
	}
	
	private void processQueue(){
		Spawn spawn;
		CreatureSpawnEvent event;
		Chunk chunk;
		while((spawn = queue.poll()) != null){
			event = spawn.event;
			
			if(!event.isCancelled()){
				chunk = event.getLocation().getBlock().getChunk();
				plugin.registerSpawn(chunk.getX(), chunk.getZ(), chunk.getWorld().getId(), event.getCreatureType().ordinal(), spawn.time);
			}
			else {
				plugin.getServer().getLogger().info("Spawn event canceled.");
			}
		}
	}
	
	private class Spawn implements Delayed {
		protected CreatureSpawnEvent event;
		protected long time;
		
		private long endOfDelay;
		
		public Spawn(CreatureSpawnEvent event){
			this.event = event;
			time = System.currentTimeMillis();
			endOfDelay = System.currentTimeMillis() + 10000;
		}
		@Override
		public int compareTo(Delayed delay) {
			if(!(delay instanceof Spawn))
				return -1;
			
			Spawn s = (Spawn) delay;
			
			if(this.endOfDelay < s.endOfDelay)
				return -1;
			else if(this.endOfDelay > s.endOfDelay)
				return 1;
			
			
			return 0;
		}
		@Override
		public long getDelay(TimeUnit timeUnit) {
			return timeUnit.convert(endOfDelay - System.currentTimeMillis(),
                    TimeUnit.MILLISECONDS);
		}
	}
}
