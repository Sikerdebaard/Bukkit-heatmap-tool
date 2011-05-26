/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

package net.hondev.heatmap;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
long time = System.currentTimeMillis();
		
		File list = new File("data/");
		
		HeatmapReader reader;
		
		Universe universe = new Universe();
		
		System.out.println("Reading data...");
		
		int c = 0;
		File[] flist = list.listFiles();
		Arrays.sort(flist, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		for(File f :  flist){
			if(f.isFile() && f.length() > 0 && /*Long.parseLong(f.getName().substring(0, f.getName().length() - 4)) > 1306248398181L - 10800000L */ Long.parseLong(f.getName().substring(0, f.getName().length() - 4)) > System.currentTimeMillis() - 10800000L){
				reader = new HeatmapReader(f);
				for(Spawn s : reader){
					if(s.getTime() < System.currentTimeMillis() - 7200000L)
						continue;
					
					//if(s.getTime() > 1306248398181L - 7200000L && s.getTime() < 1306248398181L)
					
					c++;
					universe.registerSpawn(s);
				}
			}
			
		}
		
		universe.finishSpawns();
		
		System.out.println( "Reader done in " + (System.currentTimeMillis() - time) + "msec.");
		System.out.println("Rendering...");
		
		//universe.render();
		
		System.out.println("Done!");
	}

}
