package ru.serious07.MinecraftRestarter.ini;

import java.io.IOException;

import ru.serious07.MinecraftRestarter.main.Main;

public class CreateFirstIni {
	
	inifiles ini = new inifiles();
	
	public void CreateFirstIniFile(){
		String fileadress = "MineRestarter/MincraftRestarter.ini";
		
		//Создать папку если её нет
		if(ini.folderexist("MineRestarter/")){
	    	//Создать ini файл если его нет
	    	if(ini.fileexist(fileadress) == false){
	    		CrateFirstIniFileOptions(fileadress);
	    	}
		} else {
			ini.foldercreate("MineRestarter/");
			CrateFirstIniFileOptions(fileadress);
		}
		
		ReadDatafromIni(fileadress);
	}
	
	public void ReadDatafromIni(String fileadress) {
		try {
			Main.checkIntervalInSconds = ini.filegetI(fileadress, "Options", "checkIntervalInSconds");
			Main.serverStopTimeInSeconds = ini.filegetI(fileadress, "Options", "serverStopTimeInSeconds");
			Main.serverStartTimeInSeconds = ini.filegetI(fileadress, "Options", "serverStartTimeInSeconds");
			Main.serverLocalIp = ini.filegetS(fileadress, "Options", "serverLocalIp");
			Main.serverGlobalIp = ini.filegetS(fileadress, "Options", "serverGlobalIp");
			Main.serverGlobalPort = ini.filegetI(fileadress, "Options", "serverGlobalPort");
			Main.serverLocalPort = ini.filegetI(fileadress, "Options", "serverLocalPort");
			Main.screenTag = ini.filegetS(fileadress, "Options", "screenTag");
			Main.screenName = ini.filegetS(fileadress, "Options", "screenName");
			Main.serverStopServerCmd = ini.filegetS(fileadress, "Options", "serverStopServerCmd");
			Main.serverKillServerCmd = ini.filegetS(fileadress, "Options", "serverKillServerCmd");
			Main.serverRunCmd = ini.filegetS(fileadress, "Options", "serverRunCmd");
			
			Main.backupsFolder = ini.filegetS(fileadress, "Restore", "backupsFolder");
			Main.serverWorldFolder = ini.filegetS(fileadress, "Restore", "serverWorldFolder");
			Main.restartCommand = ini.filegetS(fileadress, "Restore", "restartCommand");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void CrateFirstIniFileOptions(String fileadress) {
		try {
			ini.filecreate(fileadress);
			
			ini.filechange(fileadress, "Options", "checkIntervalInSconds", 10);
			ini.filechange(fileadress, "Options", "serverStopTimeInSeconds", 15);
			ini.filechange(fileadress, "Options", "serverStartTimeInSeconds", 300);
			ini.filechange(fileadress, "Options", "serverLocalIp", "0.0.0.0");
			ini.filechange(fileadress, "Options", "serverGlobalIp", "mc1.gregtechrus.ru");
			ini.filechange(fileadress, "Options", "serverGlobalPort", 25565);
			ini.filechange(fileadress, "Options", "serverLocalPort", 20001);
			ini.filechange(fileadress, "Options", "screenTag", "{screenName}");
			ini.filechange(fileadress, "Options", "screenName", "mc-GTR_Remastered");
			ini.filechange(fileadress, "Options", "serverStopServerCmd", "screen -S {screenName} -X stuff sc_restart\\r");
			ini.filechange(fileadress, "Options", "serverKillServerCmd", "pkill -f {screenName}");
			ini.filechange(fileadress, "Options", "serverRunCmd", "screen -d -m -S {screenName} java -server -XX:UseSSE=3 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSIncrementalPacing -XX:ParallelGCThreads=8 -XX:+AggressiveOpts -Xnoclassgc -Xms6G -Xmx6G -jar Thermos-1.7.10-1614-57-server.jar nogui");
			
			ini.filechange(fileadress, "Restore", "backupsFolder", "backups");
			ini.filechange(fileadress, "Restore", "serverWorldFolder", "world");
			ini.filechange(fileadress, "Restore", "restartCommand", "stop");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
