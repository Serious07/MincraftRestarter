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

import ru.serious07.MinecraftRestarter.json.JsonReader;
import ru.serious07.MinecraftRestarter.main.Main;

public class CheckServerStatus extends Thread {
	public CheckServerStatus() {
		this.start();
	}
	
	public void run() {
		while(Main.serverIsRunning) {
			try {
				Thread.sleep(Main.checkIntervalInSconds * 1000);
				
				showMsg("localy: " + serverPortAllowedLocaly() + " globaly: " + serverPortAllowedGlobaly());
				
				if(!serverPortAllowedLocaly()) {
					showMsg("Server offline or crash, kill and restart");
					executeKillCommand();
					Thread.sleep(50);
					executRunCommand();
					showMsg("Waiting before server starts correctly!");
					Thread.sleep(Main.serverStartTimeInSeconds * 1000);
				} else if (serverPortAllowedLocaly() && !serverPortAllowedGlobaly()) {
					showMsg("Server offline for world, send restart command on server!");
					executeStopCommand();
					showMsg("Waiting before server stops correctly!");
					Thread.sleep(Main.serverStopTimeInSeconds * 1000);
					executRunCommand();
					showMsg("Waiting before server starts correctly!");
					Thread.sleep(Main.serverStartTimeInSeconds * 1000);
					showMsg("Waiting before API Reset cash!");
					Thread.sleep(15 * 60 * 1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
	        socket.setSoTimeout(500);
	        socket.connect(new InetSocketAddress(host, port));
	        socket.close();
	    } catch (ConnectException e) {
	        open = false;
	    }

	    return open;
	}
	
	public static boolean gloabalServerListening(String host, int port) throws JSONException, IOException {
		boolean open = true;
		
		JSONObject result = JsonReader.readJsonFromUrl("https://api.mcsrvstat.us/2/" + Main.serverGlobalIp + ":" + Main.serverGlobalPort);
		JSONObject debug = (JSONObject) result.get("debug");
		boolean ping = debug.getBoolean("ping");
		open = ping;
		
		return open;
	}
}