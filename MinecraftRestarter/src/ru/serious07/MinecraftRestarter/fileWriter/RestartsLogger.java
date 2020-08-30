package ru.serious07.MinecraftRestarter.fileWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import ru.serious07.MinecraftRestarter.ini.inifiles;

public class RestartsLogger {
	public static String logFolder = "MineRestarter/Logs/";
	
	public static void CreateLog(String logText) {
		inifiles ini = new inifiles();
		
		// Создать папку если её нет
		if(ini.folderexist(logFolder) == false){
			ini.foldercreate(logFolder);
		}
		
		// Создать лог
		List<String> lines = Arrays.asList(logText);
		
		try {
			Files.write(Paths.get(logFolder + GetCurrentTime() + ".log"), lines, 
			        StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String GetCurrentTime() {
		return LocalDateTime.now().toString()
				.replace(":", "-").toString()
				.replace(".", "-");
	}
}