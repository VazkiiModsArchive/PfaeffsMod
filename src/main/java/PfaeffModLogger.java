package net.minecraft.src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import net.minecraft.client.Minecraft;

public class PfaeffModLogger {	
	private static File logFile;
	private static boolean canWrite;
	
	static {
		File mcDir = Minecraft.getMinecraftDir();
		logFile = new File(mcDir, "pfaeff");

		canWrite = true;
		if (!logFile.exists()) {
			if (!logFile.mkdirs()) {
				canWrite = false;
			}
		}
		
		logFile = new File(logFile, "log.txt");
		
		writeToLog("=====================================", false);
		writeToLog("Logging started!", true);
		writeToLog("=====================================", false);
	}
	
	public static void writeToLog(String line, boolean useTime) {
		if (!canWrite) {
			return;
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
			String dateTimeString = "";
			if (useTime) {
				dateTimeString = (new SimpleDateFormat()).format(System.currentTimeMillis()) + "\t-\t";
			}
			bw.write("\n" + dateTimeString + line);
			bw.close();
		} catch (IOException e) {
		}
	}
}
