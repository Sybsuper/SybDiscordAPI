package com.sybsuper.sybdiscordapi.listeners;

import com.sybsuper.sybdiscordapi.SybDiscordAPI;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public class StartListener {
	public boolean started = false;

	@SubscribeEvent
	public void onTick(TickEvent.ServerTickEvent event) {
		if (!started) {
			started = true;
			HashMap<String, String> params = new HashMap<>();
			SybDiscordAPI.sendMessage("evt.start", params);
		}
	}
}
