package com.sybsuper.sybdiscordapi.listeners;

import com.sybsuper.sybdiscordapi.SybDiscordAPI;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class LagListener {
	HashMap<String, Long> lastTicks = new HashMap<>();
	Thread thread;

	public LagListener() {
		Runnable target = () -> {
			while (true) {
				try {
					//noinspection BusyWait
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					break;
				}
				long now = System.currentTimeMillis();
				for (Map.Entry<String, Long> entry : lastTicks.entrySet()) {
					String worldname = entry.getKey();
					Long lastTick = entry.getValue();
					if (now - lastTick > 3000) {
						ServerWorld w = null;
						for (ServerWorld world : SybDiscordAPI.server.getWorlds()) {
							if (world.getWorldInfo() instanceof IServerWorldInfo) {
								if (((IServerWorldInfo) world.getWorldInfo()).getWorldName().equals(worldname)) {
									w = world;
									break;
								}
							}
						}
						if (w != null && w.isInsideTick()) {
							HashMap<String, String> params = new HashMap<>();
							params.put("world", worldname);
							params.put("last_tick_delta_seconds", String.valueOf(Math.round((now - lastTick) / 10.0) / 100.0));
							SybDiscordAPI.sendMessage("evt.lag", params);
						}
					}
				}
			}
		};
		thread = new Thread(target);
	}

	@SubscribeEvent
	public void onStop(TickEvent.WorldTickEvent event) {
		if (event.world != null && event.world.getWorldInfo() instanceof IServerWorldInfo) {
			lastTicks.put(((IServerWorldInfo) event.world.getWorldInfo()).getWorldName(), System.currentTimeMillis());
		}
	}
}
