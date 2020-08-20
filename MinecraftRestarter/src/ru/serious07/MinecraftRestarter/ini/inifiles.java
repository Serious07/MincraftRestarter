//By ALEXANDER LYTKIN(Serious07) IN 2014 YEAR

package ru.serious07.MinecraftRestarter.ini;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.io.IOException;

import org.ini4j.Wini;


public class inifiles {
	
	// Create empty ini file
	public void filecreate(String path) throws IOException{
		File f = new File(path);
		//(works for both Windows and Linux)
		//f.mkdirs();
		f.createNewFile();
	}
	
	// Create folder
	public void foldercreate(String path){
		File f = new File(path);
		f.mkdirs();
	}
	
	// Check file for existing
	public boolean fileexist(String path){
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}
	
	// Check folder for existing
	public boolean folderexist(String path){
		File f = new File(path);
		if(f.exists() && f.isDirectory()){
			return true;
		}else{
			return false;
		}
	}
	
	// ==========================================================
	
	// Get int value from ini file
	public int filegetI(String path, String part, String key) throws IOException{
		Wini ini = new Wini(new File(path));
        int x = ini.get(part, key, int.class);
		
		return x;
	}
	
	// Get double value from ini file
	public double filegetD(String path, String part, String key) throws IOException{
		Wini ini = new Wini(new File(path));
		double x = ini.get(part, key, double.class);
		
		return x;
	}
	
	// Get String value from ini file
	public String filegetS(String path, String part, String key) throws IOException{
		Wini ini = new Wini(new File(path));
		String x = ini.get(part, key, String.class);
		
		return x;
	}
	
	// Get float value from ini file
	public float filegetF(String path, String part, String key) throws IOException{
		Wini ini = new Wini(new File(path));
		float x = ini.get(part, key, float.class);
		
		return x;
	}
	
	// Get boolean value from ini file
	public boolean filegetB(String path, String part, String key) throws IOException{
		Wini ini = new Wini(new File(path));
		boolean x = ini.get(part, key, boolean.class);
		
		return x;
	}
	
	// ===========================================================
	
	// Change int value in ini file
	public void filechange(String path, String part, String key, int x) throws IOException{
		Wini ini = new Wini(new File(path));
		
        ini.put(part, key, x);
        ini.store();
	}
	
	// Change double value in ini file
	public void filechange(String path, String part, String key, double x) throws IOException{
		Wini ini = new Wini(new File(path));
		
        ini.put(part, key, x);
        ini.store();
	}
	
	// Change String value in ini file
	public void filechange(String path, String part, String key, String x) throws IOException{
		Wini ini = new Wini(new File(path));
		
        ini.put(part, key, x);
        ini.store();
	}
	
	// Change float value in ini file
	public void filechange(String path, String part, String key, float x) throws IOException{
		Wini ini = new Wini(new File(path));
		
        ini.put(part, key, x);
        ini.store();
	}
	
	// Change boolean value in ini file
	public void filechange(String path, String part, String key, boolean x) throws IOException{
		Wini ini = new Wini(new File(path));
		
        ini.put(part, key, x);
        ini.store();
	}
	
	//=============================================================
	
	
}
