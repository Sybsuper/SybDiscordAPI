package com.sybsuper.sybdiscordapi.listeners;

import com.sybsuper.sybdiscordapi.SybDiscordAPI;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public class ChatListener {
	@SubscribeEvent
	public void onChat(ServerChatEvent event) {
		HashMap<String, String> params = new HashMap<>();
		params.put("player", event.getUsername());
		params.put("uuid", event.getPlayer().getUniqueID().toString());
		params.put("message", event.getMessage());
		SybDiscordAPI.sendMessage("evt.chat", params);
	}
}
