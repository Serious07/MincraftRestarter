package ru.serious07.MinecraftRestarter.main;

import java.util.Scanner;

import ru.serious07.MinecraftRestarter.ini.CreateFirstIni;
import ru.serious07.MinecraftRestarter.threads.CheckServerStatus;

public class Main {

	public static boolean serverIsRunning = true;
	
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
	
	public static void main(String[] args) {
		CreateFirstIni createFirstIni = new CreateFirstIni();
		createFirstIni.CreateFirstIniFile();
		
		CheckServerStatus checkServerStatus = new CheckServerStatus(); 
		
		CheckServerStatus.showMsg("Minecraft starter running!");
		
		while(serverIsRunning) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Scanner scanner = new Scanner(System.in);
			String cmd = scanner.nextLine();
			
			if(cmd.equalsIgnoreCase("exit")) {
				System.out.println("Minecraft Restarter colsed!");
				serverIsRunning = false;
			}
		}
	}
}