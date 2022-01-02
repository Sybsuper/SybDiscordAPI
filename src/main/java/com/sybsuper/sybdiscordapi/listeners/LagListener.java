package com.sybsuper.sybdiscordapi.listeners;

import com.sybsuper.sybdiscordapi.SybDiscordAPI;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public class LagListener {
	long lastTick = 0;
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
				if (now - lastTick > 3000) {
					HashMap<String, String> params = new HashMap<>();
					params.put("last_tick_delta_seconds", String.valueOf(Math.round((now - lastTick) / 10.0) / 100.0));
					SybDiscordAPI.sendMessage("evt.lag", params);
				}
			}
		};
		thread = new Thread(target);
	}

	@SubscribeEvent
	public void onStop(TickEvent.ServerTickEvent event) {
		lastTick = System.currentTimeMillis();
	}
}
