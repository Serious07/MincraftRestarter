package ru.serious07.MinecraftRestarter.restore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ru.serious07.MinecraftRestarter.main.Main;
import ru.serious07.MinecraftRestarter.threads.CheckServerStatus;

public class RestoreRegion extends Thread{
	private int x;
	private int y;
	private int z;
	private String backupName;
	
	public RestoreRegion(int x, int y, int z, String backupName) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.backupName = backupName;
		
		this.start();
	}
	
	public void run(){
		try {
			// Server online
			if(CheckServerStatus.isServerOfflineLocaly() == false && CheckServerStatus.isServerOfflineGlobaly() == false){
				Main.restoreInProgress = true;
				System.out.println("Restarter thread is paused!");
				CheckServerStatus.executeCommand("screen -S {screenName} -X stuff say Automated restore started on server, restart soon!!!\\\r");
				// Unzip backup
				try {
					if(unzipBackup(Main.backupsFolder + "/" + backupName) == UnzipingState.DONE) {
						String regionName = getRegionName(x, z);
						
						CheckServerStatus.executeCommand("screen -S {screenName} -X stuff say Automated world restore started restart now!!!\\\r");
						Thread.sleep(5000);
						CheckServerStatus.executeStopCommand();
						
						// Wait until server stop
						while(CheckServerStatus.isServerOfflineLocaly() == false) {
							Thread.sleep(1000);
						}
						
						System.out.println("Making current world region backup");
						
						File oldServerRegionsFolder = new File("MineRestarter/OldServerRegionFiles");
						if(oldServerRegionsFolder.exists() == false || oldServerRegionsFolder.isDirectory() == false) {
							oldServerRegionsFolder.mkdirs();
						}
						
						String dateNowString = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).toString();
						File currentDateFolder = new File("MineRestarter/OldServerRegionFiles/" + dateNowString);
						if(currentDateFolder.exists() == false || currentDateFolder.isDirectory() == false) {
							currentDateFolder.mkdirs();
						}
						
						// Copy corrapted world region to backup folder
						Files.copy(new File(Main.serverWorldFolder + "/region/" + regionName).toPath(), 
								new File("MineRestarter/OldServerRegionFiles/" + dateNowString + "/" + regionName + "old").toPath(), StandardCopyOption.REPLACE_EXISTING);
						System.out.println("Corrapted region " + regionName + " copyed to backupfolder!");
						
						// Copy region file from unziped backup to local backup folder
						Files.copy(new File(Main.backupsFolder + "/" + backupName + "/backup/world/region/" + regionName).toPath(), 
								new File("MineRestarter/OldServerRegionFiles/" + dateNowString + "/" + regionName + "new").toPath(), StandardCopyOption.REPLACE_EXISTING);
						System.out.println("Backup region " + regionName + " copyed to backupfolder!");
						
						// Replace corrapted world region
						Files.copy(new File(Main.backupsFolder + "/" + backupName + "/backup/world/region/" + regionName).toPath(),
								new File(Main.serverWorldFolder + "/region/" + regionName).toPath(), StandardCopyOption.REPLACE_EXISTING);
						System.out.println("Corrapted world region was replaced with backup region!");
						
						// Delete unziped backup folder
						deleteDirectory(new File(Main.backupsFolder + "/" + backupName + "/backup"));
						System.out.println("Delete unziped files!");
						
						System.out.println("Starting server!");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Restarter thread is resumed!");
				Main.restoreInProgress = false;
			} else {
				System.out.println("Server offline!!!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++){ 
				boolean success = deleteDirectory(children[i]); 
				if (!success) { return false; } 
			} 
		} // either file or an empty directory 
		System.out.println("removing file or directory : " + dir.getName()); 
		return dir.delete();
	}
	
	public static UnzipingState unzipBackup(String ZipFolderPath) throws IOException {
		String fileZip = ZipFolderPath + "/backup.zip";
        File destDir = new File(ZipFolderPath + "/backup");
        
        if(new File(fileZip).exists()) {
	        byte[] buffer = new byte[1024];
	        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
	        ZipEntry zipEntry = zis.getNextEntry();
	        while (zipEntry != null) {
	        	
	        	
	        	File newFile = newFile(destDir, zipEntry);
	            if (zipEntry.isDirectory()) {
	                if (!newFile.isDirectory() && !newFile.mkdirs()) {
	                    throw new IOException("Failed to create directory " + newFile);
	                }
	            } else {
	            	System.out.println("Unziping file: " + zipEntry.getName());
	            	
	                // fix for Windows-created archives
	                File parent = newFile.getParentFile();
	                if (!parent.isDirectory() && !parent.mkdirs()) {
	                    throw new IOException("Failed to create directory " + parent);
	                }
	                
	                // write file content
	                FileOutputStream fos = new FileOutputStream(newFile);
	                int len;
	                while ((len = zis.read(buffer)) > 0) {
	                    fos.write(buffer, 0, len);
	                }
	                fos.close();
	            }
	        zipEntry = zis.getNextEntry();
	        }
	        zis.closeEntry();
	        zis.close();
	        
	        return UnzipingState.DONE;
        } else {
        	System.out.println("UNZIPING ERROR: backup file not found!");
        	return UnzipingState.ERROR_ZIP_FILE_NOT_EXIST;
        }
	}
	
	public static enum UnzipingState{
		DONE,
		ERROR_ZIP_FILE_NOT_EXIST
	}
	
	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
	    File destFile = new File(destinationDir, zipEntry.getName());
	 
	    String destDirPath = destinationDir.getCanonicalPath();
	    String destFilePath = destFile.getCanonicalPath();
	 
	    if (!destFilePath.startsWith(destDirPath + File.separator)) {
	        throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
	    }
	 
	    return destFile;
	}
	
	public static String getRegionName(int x, int z) {
		return "r." + (int)Math.floor((double)x / 512) + "." + (int)Math.floor((double)z / 512) + ".mca";
	}
}