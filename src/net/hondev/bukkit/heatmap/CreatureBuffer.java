/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

package net.hondev.bukkit.heatmap;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

public class CreatureBuffer {
	private static final int BUFFER_SIZE = 10485760; // buffer 10MiB. Make sure there is enough space for at least 60 records when changing size.
	private static final int RECORD_SIZE = 28;
	private static final int MAX_RECORDS_IN_BUFFER = (BUFFER_SIZE / RECORD_SIZE) - 50;
	
	private Logger log;
	
	private ByteArrayOutputStream buffer;
	private DataOutputStream dataOut;
	
	private int records;
	
	public CreatureBuffer(Logger log){
		buffer = new ByteArrayOutputStream(BUFFER_SIZE); 
		dataOut = new DataOutputStream(buffer);
		records = 0;
		
		this.log = log;
	}
	
	/**
	 * Register spawn event in buffer.
	 * @param x Chunk X of spawn
	 * @param z Chunk Y of spawn
	 * @param world World No.
	 */
	public void registerDeath(int x, int z, long world, int creature, long time){
		synchronized(buffer){
			try {
				dataOut.writeInt(x);
				dataOut.writeInt(z);
				dataOut.writeLong(world);
				dataOut.writeInt(creature);
				dataOut.writeLong(time);
			} catch (IOException e) {
				e.printStackTrace(); // needs propper handling
				// TODO: propper handling; data file gets corrupt when this error is triggered
			}
			
			records++;
		}
		
		if(records == MAX_RECORDS_IN_BUFFER){
			saveBuffer();
			records = 0;
		}
	}
	
	/**
	 * Saves current buffer to GZip-compressed file in net.hondev.bukkit.heatmap.Config.dataFoler.
	 */
	public void saveBuffer(){
		if(buffer.size() < RECORD_SIZE * 60)
			return;
		
		long time = System.currentTimeMillis();
		
		log.info("Heatmap: saving buffers (" + records + " records in buffer)");
		
		File fout = new File(Config.dataFolder + time + ".dat");
		
		int c = 1;
		while(fout.exists()){
			fout = new File(Config.dataFolder + time + "_" + c + ".dat");
			c++;
		}
		
		try {
			fout.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		synchronized(buffer){
			try {
				GZIPOutputStream out = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(fout)));
				buffer.writeTo(out);
				out.close();
				buffer.reset();
			} catch (FileNotFoundException e) {
				//log.info("Heatmap: SpawnBuffer.java; File not found. " + Config.dataFolder + time + ".dat");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		log.info("Heatmap: buffers flushed.");
	}
}
