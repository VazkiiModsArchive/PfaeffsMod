package net.minecraft.src;


public class mod_Pfaeff extends BaseMod {
	
	@Override
	public void modsLoaded() {
		PfaeffModLogger.writeToLog("Loaded mods:", false);	
		for (BaseMod mod : (Iterable<BaseMod>)ModLoader.getLoadedMods()) {
			PfaeffModLogger.writeToLog("\t\t\t" + mod.toString(), false);
		}
	}

	@Override
	public void load() {
	}	
	
	@Override
	public String getVersion() {
		return "1.2.2";
	}
}
