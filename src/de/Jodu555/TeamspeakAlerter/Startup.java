package de.Jodu555.TeamspeakAlerter;

import java.util.ArrayList;

import com.google.gson.Gson;

import de.Jodu555.TeamspeakAlerter.objects.AlertThing;
import de.Jodu555.TeamspeakAlerter.objects.TeamSpeakServer;
import de.Jodu555.TeamspeakAlerter.utils.Document;
import de.Jodu555.TeamspeakAlerter.utils.FileManager;

public class Startup {

	private ArrayList<AlertThing> alerts = new ArrayList<>();
	private ArrayList<TeamSpeakServer> servers = new ArrayList<>();
	
	public static final Gson GSON = new Gson();
	private static Startup intance;
	
	public Startup() {
		intance = this;
		System.out.println("Startup!");
		new FileManager();
		
		servers.forEach(server -> {
			server.connect();
		});
		
		alerts.forEach(alert -> {
			alert.init();
		});
		
	}
	
	public ArrayList<AlertThing> getAlertsByServerID(int id) {
		ArrayList<AlertThing> list = new ArrayList<>();
		for (AlertThing alerts : alerts) {
			if(alerts.getServerid() == id) {
				list.add(alerts);
			}
		}
		return list;
	}
	
	public TeamSpeakServer getServerByID(int id) {
		for (TeamSpeakServer servers : servers) {
			if(servers.getId() == id) {
				return servers;
			}
		}
		return null;
	}
	
	public ArrayList<TeamSpeakServer> getServers() {
		return servers;
	}
	
	public ArrayList<AlertThing> getAlerts() {
		return alerts;
	}
	
	public static Startup getIntance() {
		return intance;
	}
	
	public static void main(String[] args) {
		new Startup();
	}
}
