package de.Jodu555.TeamspeakAlerter.objects;

import java.util.logging.Level;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.PrivilegeKeyUsedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.google.gson.annotations.Expose;

import de.Jodu555.TeamspeakAlerter.Startup;

public class TeamSpeakServer {
	@Expose
	int id;
	@Expose
	String address;
	@Expose
	String queryPassword;
	@Expose
	String nickname;
	
	public static final TS3Config config = new TS3Config();
	public static final TS3Query query = new TS3Query(config);
	public static final TS3Api api = query.getApi();
	
	public TeamSpeakServer(int id, String address, String queryPassword, String nickname) {
		this.id = id;
		this.address = address;
		this.queryPassword = queryPassword;
		this.nickname = nickname;
	}
	
	public int getId() {
		return id;
	}
	
	public void connect() {
		System.out.println("Loaded TeamSpeak Server: " + address);
		config.setHost(address);
		config.setDebugLevel(Level.ALL);
		query.connect();
		api.login("serveradmin", queryPassword);
		api.selectVirtualServerById(1);
		api.setNickname(nickname);
		init();
		System.out.println("Successfully Connected to " + address);
	}
	
	public void sendMessage(String clientId, String message) {
		for (Client c : api.getClients()) {
			if(!c.isServerQueryClient()) {
				if (c.getUniqueIdentifier().equalsIgnoreCase(clientId)) {
					api.sendPrivateMessage(c.getId(), message);
				}
			}
			
		}
	}
	
	private void init() {
		api.registerAllEvents();
		api.addTS3Listeners(new TS3Listener() {
			
			@Override
			public void onTextMessage(TextMessageEvent event) {
				for (AlertThing alerts : Startup.getIntance().getAlertsByServerID(id)) {
					alerts.getOnMessageConsumer().accept(event);
				}
			}
			
			@Override
			public void onServerEdit(ServerEditedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClientMoved(ClientMovedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClientLeave(ClientLeaveEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClientJoin(ClientJoinEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChannelPasswordChanged(ChannelPasswordChangedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChannelMoved(ChannelMovedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChannelEdit(ChannelEditedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChannelDeleted(ChannelDeletedEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChannelCreate(ChannelCreateEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
