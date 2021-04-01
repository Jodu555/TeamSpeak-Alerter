package de.Jodu555.TeamspeakAlerter.configs;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import de.Jodu555.TeamspeakAlerter.objects.AlertThing;
import de.Jodu555.TeamspeakAlerter.objects.TeamSpeakServer;

public class GlobalHandler {
	@Expose
	ArrayList<TeamSpeakServer> servers = new ArrayList<>();
	@Expose
	ArrayList<AlertThing> alerts = new ArrayList<>();
	
	
	public ArrayList<TeamSpeakServer> getServers() {
		return servers;
	}
	
	public ArrayList<AlertThing> getAlerts() {
		return alerts;
	}
	
}
