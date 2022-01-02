package com.sybsuper.sybdiscordapi.jdalisteners;

import com.sybsuper.sybdiscordapi.Config;
import com.sybsuper.sybdiscordapi.SybDiscordAPI;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Message extends ListenerAdapter {
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		try {
			if (!event.getAuthor().isBot() && event.getGuild().getId().equals(SybDiscordAPI.guild.getId()) && event.getChannel().getId().equals(SybDiscordAPI.channel.getId())) {
				HashMap<String, String> params = new HashMap<>();
				params.put("user", event.getAuthor().getName());
				params.put("message", event.getMessage().getContentRaw());
				SybDiscordAPI.broadcast(SybDiscordAPI.parseParams(Config.getString("evt.discordchat.message"), params));
			}
		} catch (Exception e) {
			// idc lol
		}
	}
}
