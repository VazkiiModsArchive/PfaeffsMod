package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;

public class PfaeffModPropertyReader {
	private static Map<String, String> properties = new HashMap<String, String>();
	
	static {	
			File mcDir = Minecraft.getMinecraftDir();
			File file = new File(mcDir, "pfaeff/mod.properties");
		try {	
			if (file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));				
				String line = br.readLine();				
				while (line != null) {
					line = line.replaceAll(" ", "");
					line = line.replaceAll("\t", "");
					String[] s = line.split("=");
					if (s.length == 2) {
						String leftSide = s[0];
						String rightSide = s[1];						
						properties.put(leftSide, rightSide);
					}
					line = br.readLine();
				}
			}
		} catch (FileNotFoundException e) {
			PfaeffModLogger.writeToLog("Property File:\tNo property file found in " + file, false);
		} catch (IOException e) {
			PfaeffModLogger.writeToLog("Property File:\tError while reading property file", false);
		}
	}
	
	public static int getIntValue(String key, int defaultValue) {
		String val = properties.get(key);
		try {
			if (val != null) {
				int result;
				result = Integer.parseInt(val);				
				PfaeffModLogger.writeToLog("Property File:\tReading Integer value " + result + " from key " + key, false);				
				return result;
			} else {
				PfaeffModLogger.writeToLog("Property File:\tKey " + key + " not found! Using default value " + defaultValue, false);
				return defaultValue;
			}
		} catch(NumberFormatException e) {
			PfaeffModLogger.writeToLog("Property File:\tKey " + val + " is not a valid Integer! Using default value " + defaultValue, false);
			return defaultValue;
		}		
	}
	
	public static boolean getBooleanValue(String key, boolean defaultValue) {
		String val = properties.get(key);
		try {
			if (val != null) {
				boolean result;
				result = Boolean.parseBoolean(val);
				PfaeffModLogger.writeToLog("Property File:\tReading Boolean value " + result + " from key " + key, false);
				return result;
			} else {
				PfaeffModLogger.writeToLog("Property File:\tKey " + key	+ " not found! Using default value " + defaultValue, false);
				return defaultValue;
			}
		} catch (NumberFormatException e) {
			PfaeffModLogger.writeToLog("Property File:\tKey " + val	+ " is not a valid Boolean! Using default value " + defaultValue, false);
			return defaultValue;
		}
	}
}
