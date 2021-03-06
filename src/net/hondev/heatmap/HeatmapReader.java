/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

package net.hondev.heatmap;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;


public class HeatmapReader implements Iterable<Spawn>, Iterator<Spawn> {
	private DataInputStream dataIn;
	private boolean eof;
	private Spawn spawn;
	
	private File file;
	
	private long l;
	
	public HeatmapReader(File data){
		this.file = data;
		l = Long.MIN_VALUE;
		init();
	}
	
	private void init(){
		eof = false;
		
		try {
			dataIn = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(file)))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		readNext();
	}
	
	private void readNext(){
		if(eof){
			try {
				dataIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			spawn = null;
			return;
		}
		
		try {
			spawn = new Spawn(dataIn.readInt(), dataIn.readInt(), dataIn.readLong(), dataIn.readInt(), dataIn.readLong());
			
			if(spawn != null && l > spawn.getTime())
				System.out.println("NOES!");
			
			if(spawn != null)
				l = spawn.getTime();
		} catch(EOFException e){
			eof = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Iterator<Spawn> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		if(spawn != null)
			return true;
		
		return false;
	}

	@Override
	public Spawn next() {
		Spawn ret = spawn;
		readNext();
		
		return ret;
	}

	@Override
	public void remove() {
		
	}
}
