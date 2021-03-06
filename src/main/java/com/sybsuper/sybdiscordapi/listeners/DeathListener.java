package com.sybsuper.sybdiscordapi.listeners;

import com.sybsuper.sybdiscordapi.SybDiscordAPI;
import com.sybsuper.sybdiscordapi.SybTranslator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public class DeathListener {
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			HashMap<String, String> params = new HashMap<>();
			params.put("player", event.getEntityLiving().getName().getUnformattedComponentText());
			params.put("uuid", event.getEntityLiving().getUniqueID().toString());
			params.put("message", SybTranslator.translateTextComponent(event.getEntityLiving().getCombatTracker().getDeathMessage()));
			SybDiscordAPI.sendMessage("evt.death", params);
		}
	}
}
