package net.hondev.bukkit.heatmap;

import java.io.File;

public class Config {
	public static final String dataFolder = "HeteMap/";
	
	public static final void checkDirs(){
		File f = new File(dataFolder);
		if(!f.exists())
			f.mkdirs();
	}
}
