package com.sybsuper.sybdiscordapi;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Config {
	public static ForgeConfigSpec.Builder cb = new ForgeConfigSpec.Builder();
	public static HashMap<String, ForgeConfigSpec.ConfigValue<?>> cv = new HashMap<>();
	public static ForgeConfigSpec c;

	public static String getString(String key) {
		return (String) cv.get(key).get();
	}

	public static Boolean getBoolean(String key) {
		return (Boolean) cv.get(key).get();
	}

	public static void init() {
		cb.comment("############################", " Bot Settings", "############################");
		configureOption("token", "The token of the bot", "0000");
		configureOption("guild", "The id of the guild that the bot will use", "0000");
		configureOption("channel", "The id of the channel that the bot will use", "0000");
		cb.comment("############################", " Events", "############################");
		configureEvent(
				"join",
				"Send a message when a player joins the server",
				"**%player% has joined the server**",
				"player: The name of the player",
				"uuid: The uuid of the player"
		);
		configureEvent(
				"quit",
				"Send a message when a player quits the server",
				"**%player% has left the server**",
				"player: The name of the player",
				"uuid: The uuid of the player"
		);
		configureEvent(
				"advancement",
				"Send a message when a player receives an advancement",
				"**%player% achieved %advancement_title% (%advancement_description%)**",
				"player: The name of the player",
				"uuid: The uuid of the player",
				"advancement_title: The title of the advancement",
				"advancement_description: The description of the advancement"
		);
		configureEvent(
				"chat",
				"Send a message when a player sends a message in chat",
				"**%player%**: %message%",
				"player: The name of the player",
				"uuid: The uuid of the player",
				"message: The message"
		);
		configureEvent(
				"discordchat",
				"Send a message when a user sends a message in the discord chat",
				"&3Discord &f%user%&7: &f%message%",
				false,
				"user: The name of the user",
				"message: The message"
		);
		configureEvent(
				"death",
				"Send a message when a player dies",
				"**%message%**",
				"player: The name of the player",
				"uuid: The uuid of the player",
				"message: The death message"
		);
		configureEvent(
				"start",
				"Send a message when the server starts",
				"**The server has started.**"
		);
		configureEvent(
				"stop",
				"Send a message when the server stops",
				"**The server has stopped.**"
		);
		configureEvent(
				"lag",
				"Send a message when the server has a lag spike",
				"**The server is lagging, the last tick was %last_tick_delta_seconds% seconds ago.**",
				"last_tick_delta_seconds: the time between now and the last tick."
		);
		c = cb.build();
	}

	public static void configureOption(String path, Object defaultValue) {
		cv.put(path, cb.define(path, defaultValue));
	}

	public static void configureOption(String path, String description, Object defaultValue) {
		cv.put(path, cb.comment(description).define(path, defaultValue));
	}

	public static void configureEvent(String name, String description, String message, String... params) {
		configureEvent(name, description, message, true, params);
	}

	public static void configureEvent(String name, String description, String message, boolean embed, String... params) {
		String path = "evt." + name;
		cb.comment(description);
		configureOption(path + ".enabled", true);
		if (embed) {
			configureOption(path + ".embed.enabled", true);
			configureOption(path + ".embed.title", "");
			configureOption(path + ".embed.color", "#969C9F");
			configureOption(path + ".embed.description", message);
			configureOption(path + ".embed.url", "");
			configureOption(path + ".embed.timestamp", false);
			configureOption(path + ".embed.footer.icon_url", "");
			configureOption(path + ".embed.footer.text", "");
			configureOption(path + ".embed.thumbnail", "");
			configureOption(path + ".embed.image", "");
			configureOption(path + ".embed.author.name", "");
			configureOption(path + ".embed.author.url", "");
			configureOption(path + ".embed.author.icon_url", "");
		}
		if (params.length > 0) {
			List<String> li = new ArrayList<>();
			li.add("Available parameters.");
			li.addAll(Arrays.asList(params));
			cb.comment(li.toArray(new String[0]));
		}
		configureOption(path + ".message", embed ? "" : message);
	}

	public static void setup() {
		CommentedFileConfig config = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve("sybdiscordapi.toml")).sync().autosave().writingMode(WritingMode.REPLACE).build();
		config.load();
		c.setConfig(config);
	}
}
