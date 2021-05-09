package ru.serious07.MinecraftRestarter.main;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

import ru.serious07.MinecraftRestarter.ini.CreateFirstIni;
import ru.serious07.MinecraftRestarter.restore.RestoreRegion;
import ru.serious07.MinecraftRestarter.threads.CheckServerStatus;

public class Main {

	public static boolean serverIsRunning = true;
	
	// Restart vars
	public static int checkIntervalInSconds = 10;
	public static int serverStopTimeInSeconds = 15;
	public static int serverStartTimeInSeconds = 300;
	public static String serverLocalIp = "localhost";
	public static String serverGlobalIp = "mc1.gregtechrus.ru";
	public static int serverGlobalPort = 25565;
	public static int serverLocalPort = 20001;
	public static String screenTag = "{screenName}";
	public static String screenName = "mc-GTR_Remastered";
	public static String serverStopServerCmd = "screen -S {screenName} -X stuff sc_restart\\r";
	public static String serverKillServerCmd = "pkill -f {screenName}";
	public static String serverRunCmd = "screen -d -m -S {screenName} java -server -XX:UseSSE=3 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSIncrementalPacing -XX:ParallelGCThreads=8 -XX:+AggressiveOpts -Xnoclassgc -Xms6G -Xmx6G -jar Thermos-1.7.10-1614-57-server.jar nogui";
	
	public static boolean falseRestartProtection = true;
	
	// Restore vars
	public static String backupsFolder = "/backups";
	public static String serverWorldFolder = "/server/world";
	public static String restartCommand = "restart";
	public static boolean restoreInProgress = false;
	
	public static CheckServerStatus checkServerStatus;
	
	public static void main(String[] args) {
		CreateFirstIni createFirstIni = new CreateFirstIni();
		createFirstIni.CreateFirstIniFile();
		
		checkServerStatus = new CheckServerStatus(); 
		
		CheckServerStatus.showMsg("Minecraft starter running!");
		
		while(serverIsRunning) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Scanner scanner = new Scanner(System.in);
			String cmd = scanner.nextLine();
			
			String cmdargs[] = cmd.split(" ");
			
			if(cmdargs[0].equalsIgnoreCase("exit")) {
				System.out.println("Minecraft Restarter colsed!");
				serverIsRunning = false;
			} else if (cmdargs[0].equalsIgnoreCase("restoreregion")) {
				if(cmdargs.length == 5) {
					int x = Integer.parseInt(cmdargs[1]);
					int y = Integer.parseInt(cmdargs[2]);
					int z = Integer.parseInt(cmdargs[3]);
					String backupFolder = cmdargs[4];
					
					RestoreRegion restoreRegion = new RestoreRegion(x, y, z, backupFolder);
				} else {
					System.out.println("Example: restoreregion [x] [y] [z] [backupFolderName]");
				}
			} else if (cmdargs[0].equalsIgnoreCase("restoreplayer")) {
				
			} else if (cmdargs[0].equalsIgnoreCase("backups")) {
				//Creating a File object for directory
			      File directoryPath = new File(backupsFolder);
			      //List of all files and directories
			      String contents[] = directoryPath.list();
			      System.out.println("Backups folders:");
			      for(int i=0; i<contents.length; i++) {
			    	 File tmpDirectory = new File(backupsFolder + "/" + contents[i]);
			         if(tmpDirectory.isDirectory()) {
			        	System.out.println(new Date(tmpDirectory.lastModified()).toString() + ": " + contents[i]);
			         }
			      }
			}
		}
	}
}