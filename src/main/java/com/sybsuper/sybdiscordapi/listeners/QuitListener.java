package com.sybsuper.sybdiscordapi.listeners;

import com.sybsuper.sybdiscordapi.SybDiscordAPI;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public class QuitListener {
	@SubscribeEvent
	public void onQuit(PlayerEvent.PlayerLoggedOutEvent event) {
		HashMap<String, String> params = new HashMap<>();
		params.put("player", event.getPlayer().getName().getUnformattedComponentText());
		params.put("uuid", event.getPlayer().getUniqueID().toString());
		SybDiscordAPI.sendMessage("evt.quit", params);
	}
}
