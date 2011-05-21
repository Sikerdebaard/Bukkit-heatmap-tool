package net.hondev.heatmap;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.hondev.math.BigSquareRoot;

public class World {
	private Map<Integer, Map<Integer, Chunk>> chunks;
	
	List<Chunk> hotChunks;
	
	private long activityBegin;
	private long lastTime;
	private long activity;
	private int load;
	
	private long id;
	private int chunkCounter;
	private int spawns;
	
	private int left, right, top, bottom;
	
	private BigDecimal total;
	private BigDecimal avg;
	private BigDecimal stdev;
	private MathContext mathContext;
	private MathContext smallContext;
	private double max, min;
	
	private int gradient[];
	
	public World(long id){
		chunks = new HashMap<Integer, Map<Integer, Chunk>>();
		hotChunks = new ArrayList<Chunk>();
		
		activityBegin = Long.MIN_VALUE;
		activity = 0;
		load = 0;
		chunkCounter = 0;
		spawns = 0;
		
		mathContext = new MathContext(15);
		smallContext = new MathContext(2);
		total = new BigDecimal(0, mathContext);
		avg = new BigDecimal(0, mathContext);
		stdev = new BigDecimal(0, mathContext);
		max = Integer.MIN_VALUE;
		min = Integer.MAX_VALUE;
		
		gradient = new int[100];
		
		left = right = top = bottom = 0;
		
		this.id = id;
	}
	
	public void registerSpawn(int x, int z, long time){
		Map<Integer, Chunk> m;
		Chunk c;
		
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
		
		if(x < left)
			left = x;
		
		if(x > right)
			right = x;
		
		if(z < top)
			top = z;
		
		if(z > bottom)
			bottom = z;
		
		if((m = chunks.get(Integer.valueOf(x))) == null){
			m = new HashMap<Integer, Chunk>();
			chunks.put(Integer.valueOf(x), m);
		}
		
		if((c = m.get(Integer.valueOf(z))) == null){
			c = new Chunk(x, z);
			m.put(Integer.valueOf(z), c);
		}
		
		c.registerSpawn(time);
		
		spawns++;
	}
	
	public void finishSpawns(){
		activity += (lastTime - activityBegin);
		load++;
		
		Chunk c;
		for(Integer i : chunks.keySet())
			for(Integer i2 : chunks.get(i).keySet()){
				if(chunks.get(i).get(i2) == null)
					continue;
				
				c = chunks.get(i).get(i2); 
				c.finishSpawns();
				
				if(c.activity > 900000 && c.avgSpawnsPerHour >= Config.MAX_SPAWNS_PER_CHUNK)
					hotChunks.add(chunks.get(i).get(i2));
				
				chunkCounter++;
			}
		
		process();
		
		saveJSON();
		
		System.out.println("\n--World " + id + " finished--");
		System.out.println("Boundaries: LEFT=" + left + " TOP=" + top + " RIGHT=" + right + " BOTTOM=" + bottom);
		System.out.println("Spawns: " + spawns);
		System.out.println("Estimated activity: " + (activity / 1000 / 60) + " mins.");
		System.out.println("Load: " + load);
		System.out.println("Avg spawns per hour in active chunk: " + avg.round(smallContext).toString() + " (Stdev: " + stdev.round(smallContext).toString() + ")");
		System.out.println("Min/max spawns per hour in active chunk: " + new BigDecimal(min, smallContext).toString() + "/" + new BigDecimal(max, smallContext).toString());
		System.out.println("Chunks in memory: " + chunkCounter + " (" + hotChunks.size() + " are hot!)");
		System.out.println("");
	}
	
	private void saveJSON(){
		if(!new File(Config.JSON_OUT).exists())
			new File(Config.JSON_OUT).mkdirs();
		
		if(hotChunks.size() < 1)
			return;
		
		File out = new File(Config.JSON_OUT + id + ".json");
		try {
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(out));
			
			bout.write("var data = { \"markers\":[ ".getBytes(Charset.forName("ASCII")));
			
			Chunk c;
			for(int i = 0; i < hotChunks.size() - 1; i++){
				c = hotChunks.get(i);
				System.out.println(c.spawns + " - " + c.activity + " - " + c.load + " - " + c.avgSpawnsPerHour + "   X=" + c.x + " Y=" + c.z);
				
				bout.write(("{\"x\":" + (c.z /* de zorgverzekeraar =) */ * 16 + 8) + ",\"y\":" + (c.x * 16) + ",\"w\":" + ((int) Math.round(c.avgSpawnsPerHour)) + "},").getBytes(Charset.forName("ASCII")));
			}
			
			c = hotChunks.get(hotChunks.size() - 1);
			bout.write(("{\"x\":" + (c.z /* de zorgverzekeraar =) */ * 16 + 8) + ",\"y\":" + (c.x * 16) + ",\"w\":" + ((int) Math.round(c.avgSpawnsPerHour)) + "}").getBytes(Charset.forName("ASCII")));
			
			bout.write("]}".getBytes(Charset.forName("ASCII")));
			
			bout.flush();
			bout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void process(){
		Chunk c;
		BigSquareRoot bigSqrt = new BigSquareRoot();
		
		total.setScale(15);
		stdev.setScale(15);
		
		int a = 0;
		
		for(Integer i : chunks.keySet())
			for(Integer i2 : chunks.get(i).keySet()){
				if((c = chunks.get(i).get(i2)) == null)
					continue;
				
				total = total.add(new BigDecimal(c.avgSpawnsPerHour));
				
				if(c.avgSpawnsPerHour < min)
					min = c.avgSpawnsPerHour;
				
				if(c.avgSpawnsPerHour > max)
					max = c.avgSpawnsPerHour;
			}
		
		avg = total.divide(new BigDecimal(chunkCounter), mathContext);
		
		for(Integer i : chunks.keySet())
			for(Integer i2 : chunks.get(i).keySet()){
				if((c = chunks.get(i).get(i2)) == null)
					continue;
				
				stdev = stdev.add((new BigDecimal(c.avgSpawnsPerHour).min(avg)).pow(2));
			}
		
		bigSqrt.setScale(mathContext.getPrecision());
		stdev = bigSqrt.get(stdev.divide(new BigDecimal(chunkCounter), mathContext));
	}
}
