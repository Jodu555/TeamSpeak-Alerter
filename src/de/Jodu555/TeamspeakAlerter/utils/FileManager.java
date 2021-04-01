package de.Jodu555.TeamspeakAlerter.utils;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.Jodu555.TeamspeakAlerter.Startup;
import de.Jodu555.TeamspeakAlerter.configs.GlobalHandler;
import de.Jodu555.TeamspeakAlerter.objects.AlertThing;
import de.Jodu555.TeamspeakAlerter.objects.TeamSpeakServer;

public class FileManager {

	File configFile;

	public FileManager() {
		configFile = new File("config.json");

		if (!configFile.exists()) {
			GlobalHandler globalHandler = new GlobalHandler();
			globalHandler.getServers().add(new TeamSpeakServer(1, "server4.jodu555.de", "test", "nickname"));
			globalHandler.getAlerts().add(new AlertThing(1, "Live Check", "https://www.youtube.com/channel/UC2GBv0o0CbSj68lSbr5EUrQ",
					"Jetzt live", new ArrayList<>(Arrays.asList("3KNYFKJQ+xQbMFFcwDMA9g9/0nQ=")), "XDXDXD", 10));
			new Document("config", globalHandler).saveAsConfig(configFile);
		}
		load();
	}
	
	private void load() {
		GlobalHandler globalHandler = null;
		try {
			globalHandler = Document.$loadDocument(configFile).getObject("config", GlobalHandler.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Startup.getIntance().getServers().addAll(globalHandler.getServers());
		Startup.getIntance().getAlerts().addAll(globalHandler.getAlerts());
	}

}
