package com.sybsuper.sybdiscordapi.listeners;

import com.sybsuper.sybdiscordapi.SybDiscordAPI;
import net.minecraft.advancements.DisplayInfo;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public class AdvancementListener {
	@SubscribeEvent
	public void onAdvancement(AdvancementEvent event) {
		HashMap<String, String> params = new HashMap<>();
		params.put("player", event.getPlayer().getName().getUnformattedComponentText());
		params.put("uuid", event.getPlayer().getUniqueID().toString());
		DisplayInfo di = event.getAdvancement().getDisplay();
		if (di != null) {
			params.put("advancement_title", event.getAdvancement().getDisplayText().getUnformattedComponentText());
			params.put("advancement_description", di.getDescription().getUnformattedComponentText());
			SybDiscordAPI.sendMessage("evt.advancement", params);
		}
	}
}
