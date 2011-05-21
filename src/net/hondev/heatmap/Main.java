package net.hondev.heatmap;

import java.io.File;

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
		
		long l = Long.MIN_VALUE;
		int c = 0;
		for(File f :  list.listFiles()){
			if(f.isFile()){
				reader = new HeatmapReader(f);
				for(Spawn s : reader){
					if(l == Long.MIN_VALUE){
						l = s.getTime();
					}
					
					if(s.getTime() - l >= Long.MAX_VALUE /*120000L*/){
						break;
						//System.out.println(c);
					}
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
