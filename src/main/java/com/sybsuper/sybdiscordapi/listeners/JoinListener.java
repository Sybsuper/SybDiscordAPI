package com.sybsuper.sybdiscordapi.listeners;

import com.sybsuper.sybdiscordapi.SybDiscordAPI;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public class JoinListener {
	@SubscribeEvent
	public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
		HashMap<String, String> params = new HashMap<>();
		params.put("player", event.getPlayer().getName().getUnformattedComponentText());
		params.put("uuid", event.getPlayer().getUniqueID().toString());
		SybDiscordAPI.sendMessage("evt.join", params);
	}
}
