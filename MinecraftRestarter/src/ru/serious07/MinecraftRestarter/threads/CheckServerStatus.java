package ru.serious07.MinecraftRestarter.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import javax.net.SocketFactory;

import org.json.JSONException;
import org.json.JSONObject;

import ru.serious07.MinecraftRestarter.fileWriter.RestartsLogger;
import ru.serious07.MinecraftRestarter.json.JsonReader;
import ru.serious07.MinecraftRestarter.main.Main;
import ru.serious07.MinecraftRestarter.serverStatusHelper.ServerStatusParcer;
import ru.serious07.MinecraftRestarter.serverStatusHelper.ServerStatusParcer.StatusResponse;
import ru.serious07.MinecraftRestarter.serverStatusHelper.ServerStatusParcer.Version;

public class CheckServerStatus extends Thread {
	public CheckServerStatus() {
		this.start();
	}
	
	public void run() {
		while(Main.serverIsRunning) {
			try {
				Thread.sleep(Main.checkIntervalInSconds * 1000);
				
				if(!serverPortAllowedLocaly()) {
					if(isServerOfflineLocaly()) {
						restartServerLocaly();
					}
				} else if (serverPortAllowedLocaly() && !serverPortAllowedGlobaly()) {
					if(isServerOfflineGlobaly()) {
						restartServerGlobaly();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isServerOfflineLocaly() throws InterruptedException {
		int trysAmount = 0;
		int trysCount = 10;
		long waitingTime = 7000;
		
		for(int i = 0; i < trysCount; i++) {
			if(!serverPortAllowedLocaly()) {
				trysAmount++;
				
				showMsg("Try to reach server localy! Try " + trysAmount + "/" + trysCount);
			} else {
				showMsg("False Restart detection! Restart is canceled!");
				
				return false;
			}
			Thread.sleep(waitingTime);
		}
		
		return trysAmount == trysCount;
	}
	
	public static boolean isServerOfflineGlobaly() throws InterruptedException {
		int trysAmount = 0;
		int trysCount = 10;
		long waitingTime = 7000;
		
		for(int i = 0; i < trysCount; i++) {
			if(!serverPortAllowedGlobaly()) {
				trysAmount++;
				
				showMsg("Try to reach server globaly! Try " + trysAmount + "/" + trysCount);
			} else {
				showMsg("False Restart detection! Restart is canceled!");
				
				return false;
			}
			Thread.sleep(waitingTime);
		}
		
		return trysAmount == trysCount;
	}
	
	public static void restartServerLocaly() throws InterruptedException {
		String restartAnswer = "Server offline or crash localy, kill and restart! " + Main.serverLocalIp + ":" + Main.serverLocalPort;
		
		RestartsLogger.CreateLog(restartAnswer);
		showMsg(restartAnswer);
		
		executeKillCommand();
		Thread.sleep(50);
		executRunCommand();
		showMsg("Waiting before server starts correctly!");
		Thread.sleep(Main.serverStartTimeInSeconds * 1000);
	}
	
	public static void restartServerGlobaly() throws InterruptedException {
		String restartAnswer = "Server offline for world, send restart command on server! " + Main.serverGlobalIp + ":" + Main.serverGlobalPort;
		
		RestartsLogger.CreateLog(restartAnswer);
		showMsg(restartAnswer);
		
		executeStopCommand();
		showMsg("Waiting before server stops correctly!");
		Thread.sleep(Main.serverStopTimeInSeconds * 1000);
		executRunCommand();
		showMsg("Waiting before server starts correctly!");
		Thread.sleep(Main.serverStartTimeInSeconds * 1000);
		showMsg("Waiting before API Reset cash!");
		Thread.sleep(15 * 60 * 1000);
	}
	
	public static void showMsg(String msg) {
		System.out.println(LocalDateTime.now() + " " + msg);
	}
	
	public static boolean serverPortAllowedLocaly() {
		try {
			return localServerListening(Main.serverLocalIp, Main.serverLocalPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean serverPortAllowedGlobaly() {
		try {
			return gloabalServerListening(Main.serverGlobalIp, Main.serverGlobalPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void executeCommand(String command) {
		ProcessBuilder processBuilder = new ProcessBuilder();
		
		String newCommand = command.replace(Main.screenTag, Main.screenName);
		
		processBuilder.command("bash", "-c", newCommand);
		try {

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Command excuted!");
				System.out.println(output);
			} else {
				//abnormal...
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void executeKillCommand() {
		executeCommand(Main.serverKillServerCmd);
	}
	
	public static void executeStopCommand(){
		executeCommand(Main.serverStopServerCmd);
	}
	
	public static void executRunCommand(){
		executeCommand(Main.serverRunCmd);
	}
	
	public static boolean localServerListening(String host, int port) throws UnknownHostException,
    IOException
	{
		boolean open = true;
	    Socket socket = SocketFactory.getDefault().createSocket();
	    try {
	        socket.setSoTimeout(5000);
	        socket.connect(new InetSocketAddress(host, port));
	        socket.close();
	    } catch (ConnectException e) {
	        open = false;
	    }

	    return open;
	}
	
	public static boolean gloabalServerListening(String host, int port) throws JSONException, IOException {
		boolean open = true;
		
		ServerStatusParcer serverStatusParcer = new ServerStatusParcer();
		serverStatusParcer.setAddress(new InetSocketAddress(Main.serverGlobalIp, Main.serverGlobalPort));
		StatusResponse serverResponce = serverStatusParcer.fetchData();
		Version ver = serverResponce.getVersion();
		System.out.println("Version: " + ver.getName());
		if (ver.getName().equals("")) open = false;
		
		return open;
	}
}