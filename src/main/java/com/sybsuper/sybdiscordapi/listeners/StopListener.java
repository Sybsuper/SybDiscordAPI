package com.sybsuper.sybdiscordapi.listeners;

import com.sybsuper.sybdiscordapi.SybDiscordAPI;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import java.util.HashMap;

public class StopListener {
	@SubscribeEvent
	public void onStop(FMLServerStoppingEvent event) {
		HashMap<String, String> params = new HashMap<>();
		SybDiscordAPI.sendMessage("evt.stop", params);
	}
}
