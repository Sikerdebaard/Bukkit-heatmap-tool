/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

package net.hondev.bukkit.heatmap;

import org.bukkit.Chunk;
import org.bukkit.craftbukkit.entity.*;
import org.bukkit.entity.CreatureType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;


public class CreatureListener extends EntityListener  {
	public static HeatMap plugin;
	
	public CreatureListener(HeatMap instance){
		super();
		CreatureListener.plugin = instance;
		
	}
	
	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event){
		/*
		if(event.isCancelled()) // ignore canceled spawns
			return;
		
		if(event.getCreatureType() == CreatureType.WOLF && event.getEntity() instanceof Wolf && ((Wolf) event.getEntity()).isTamed()) // ignore tamed wolfs due to respawn (bug?)
			return;
		
		Chunk chunk = event.getLocation().getBlock().getChunk();
		plugin.registerSpawn(chunk.getX(), chunk.getZ(), chunk.getWorld().getId(), event.getCreatureType().ordinal(), System.currentTimeMillis());
		*/
	}
	
	@Override
	public void onEntityDeath(EntityDeathEvent event){
		if(!(event.getEntity() instanceof CraftLivingEntity))
			return;
		
		CreatureType type = creatureTypeFromCraftCreature((CraftLivingEntity) event.getEntity());
		
		if(type == null)
			return;
		
		Chunk chunk = event.getEntity().getLocation().getBlock().getChunk();
		
		plugin.registerDeath(chunk.getX(), chunk.getZ(), chunk.getWorld().getId(), type.ordinal(), System.currentTimeMillis());
	}
	
	private CreatureType creatureTypeFromCraftCreature(CraftLivingEntity creature){
		if(creature instanceof CraftChicken)
			return CreatureType.CHICKEN;
		else if(creature instanceof CraftCow)
			return CreatureType.COW;
		else if(creature instanceof CraftCreeper)
			return CreatureType.CREEPER;
		else if(creature instanceof CraftGhast)
			return CreatureType.GHAST;
		else if(creature instanceof CraftGiant)
			return CreatureType.GIANT;
		else if(creature instanceof CraftPig)
			return CreatureType.PIG;
		else if(creature instanceof CraftPigZombie)
			return CreatureType.PIG_ZOMBIE;
		else if(creature instanceof CraftSheep)
			return CreatureType.SHEEP;
		else if(creature instanceof CraftSkeleton)
			return CreatureType.SKELETON;
		else if(creature instanceof CraftSlime)
			return CreatureType.SLIME;
		else if(creature instanceof CraftSpider)
			return CreatureType.SPIDER;
		else if(creature instanceof CraftSquid)
			return CreatureType.SQUID;
		else if(creature instanceof CraftWolf)
			return CreatureType.WOLF;
		else if(creature instanceof CraftZombie)
			return CreatureType.ZOMBIE;
		else if(creature instanceof CraftMonster)
			return CreatureType.MONSTER;
		
		return null;
	}
}
