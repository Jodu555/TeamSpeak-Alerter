package de.Jodu555.TeamspeakAlerter.objects;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.google.gson.annotations.Expose;

import de.Jodu555.TeamspeakAlerter.Startup;

public class AlertThing {
	@Expose
	int serverid;
	@Expose
	String name;
	@Expose
	String proofURL;
	@Expose
	String containURL;
	@Expose
	ArrayList<String> clientList;
	@Expose
	String message;
	@Expose
	int checkInSeconds;

	Timer timer;
	int checks;
	Consumer<TextMessageEvent> onMessageConsumer;

	public AlertThing(int serverid, String name, String proofURL, String containURL, ArrayList<String> clientList,
			String message, int checkInSeconds) {
		this.serverid = serverid;
		this.name = name;
		this.proofURL = proofURL;
		this.containURL = containURL;
		this.clientList = clientList;
		this.message = message;
		this.checkInSeconds = checkInSeconds;
	}

	public int getServerid() {
		return serverid;
	}

	public Consumer<TextMessageEvent> getOnMessageConsumer() {
		return onMessageConsumer;
	}

	public void init() {
		System.out.println("Loaded Alert: " + name);
		initConsumers();
		initTimer();
		System.out.println("Successfully loaded Alert: " + name);
	}

	private void initConsumers() {
		onMessageConsumer = new Consumer<TextMessageEvent>() {
			@Override
			public void accept(TextMessageEvent event) {
				if (clientList.contains(event.getInvokerUniqueId())) {
					String clientId = event.getInvokerUniqueId();

					String message = event.getMessage();
					String[] args = message.split(" ");

					if (args[0].equalsIgnoreCase("!stop")) {
						if (args[1].equalsIgnoreCase(name)) {
							Startup.getIntance().getServerByID(serverid).sendMessage(clientId,
									"Alert " + name + " stopped!");
							timer.cancel();
						}
					} else if (args[0].equalsIgnoreCase("!start")) {
						if (args[1].equalsIgnoreCase(name)) {
							Startup.getIntance().getServerByID(serverid).sendMessage(clientId,
									"Alert " + name + " started!");
							initTimer();
						}
					}
				}
			}
		};
	}

	private void initTimer() {
		timer = new Timer();
		timer.schedule(new AlertTimertask(), 0, checkInSeconds * 1000);
	}

	public void check() {
		checks++;
		boolean bool = false;
		String response = "";
		try {
			response = sendGet(proofURL);
		} catch (Exception e) {
			System.out.println("Blocked-Alert: " + name);
		}
		if (response.contains(containURL)) {
			bool = true;
		} else {
			bool = false;
		}

		if (bool) {
			clientList.forEach(clients -> {
				Startup.getIntance().getServerByID(serverid).sendMessage(clients,
						"Alert: " + name + " Nachricht: " + message);
				Startup.getIntance().getServerByID(serverid).sendMessage(clients,
						"Soll ich aufhören: !stop " + name);
			});
		}

		System.out.println("---------------------------");
		System.out.println("Name: " + name);
		System.out.println(
				"Time: " + new SimpleDateFormat("dd.MM.yyy HH:mm:ss").format(Calendar.getInstance().getTimeInMillis()));
		System.out.println("Contains: " + bool);
		System.out.println("Checks: " + checks);
		System.out.println("---------------------------");

	}

	private String sendGet(String restUrl) throws Exception {
		URL url = new URL(restUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("User-Agent", "PostmanRuntime/7.26.1");
		connection.setRequestMethod("GET");
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = null;
		StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
		while ((line = reader.readLine()) != null) {
			out.append(line);
		}
		String response = out.toString();
		return response;
	}

	private class AlertTimertask extends TimerTask {
		@Override
		public void run() {
			check();
		}
	}

}
