package com.sybsuper.sybdiscordapi;

import com.sybsuper.sybdiscordapi.jdalisteners.Message;
import com.sybsuper.sybdiscordapi.listeners.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.time.Instant;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod("sybdiscordapi")
public class SybDiscordAPI {
	public static final Logger LOGGER = LogManager.getLogger();
	public static JDA jda;
	public static TextChannel channel;
	public static Guild guild;
	public static HashMap<String, Object> listeners = new HashMap<>();
	public static HashMap<String, Object> jdalisteners = new HashMap<>();
	public static MinecraftServer server;
	public static UUID randomuuid = UUID.randomUUID();

	public SybDiscordAPI() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static String parseParams(String msg, HashMap<String, String> params) {
		String m = msg;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String varname = entry.getKey();
			String value = entry.getValue();
			m = m.replaceAll("%" + varname + "%", value);
		}
		return m;
	}

	public static void sendMessage(String configpath, HashMap<String, String> params) {
		String msg = Config.getString(configpath + ".message");
		MessageBuilder builder = new MessageBuilder();
		builder.setContent(parseParams(msg, params));
		if (jda == null) {
			LOGGER.info(parseParams(msg, params));
			return;
		}
		if (Config.getBoolean(configpath + ".embed.enabled")) {
			EmbedBuilder eb = new EmbedBuilder();
			String title = Config.getString(configpath + ".embed.title");
			String color = Config.getString(configpath + ".embed.color");
			String description = Config.getString(configpath + ".embed.description");
			String url = Config.getString(configpath + ".embed.url");
			boolean timestamp = Config.getBoolean(configpath + ".embed.timestamp");
			String footer_icon = Config.getString(configpath + ".embed.footer.icon_url");
			String footer_text = Config.getString(configpath + ".embed.footer.text");
			String thumbnail = Config.getString(configpath + ".embed.thumbnail");
			String image = Config.getString(configpath + ".embed.image");
			String author_name = Config.getString(configpath + ".embed.author.name");
			String author_url = Config.getString(configpath + ".embed.author.url");
			String author_icon = Config.getString(configpath + ".embed.author.icon_url");
			if (!title.isEmpty() && url.isEmpty()) {
				eb.setTitle(parseParams(title, params));
			}
			if (!color.isEmpty()) {
				eb.setColor(Color.decode(color));
			}
			if (!description.isEmpty()) {
				eb.setDescription(parseParams(description, params));
			}
			if (!title.isEmpty() && !url.isEmpty()) {
				eb.setTitle(parseParams(title, params), parseParams(url, params));
			}
			if (timestamp) {
				eb.setTimestamp(Instant.now());
			}
			if (!footer_text.isEmpty()) {
				if (footer_icon.isEmpty()) {
					eb.setFooter(parseParams(footer_text, params));
				} else {
					eb.setFooter(parseParams(footer_text, params), parseParams(footer_icon, params));
				}
			}
			if (!thumbnail.isEmpty()) {
				eb.setThumbnail(parseParams(thumbnail, params));
			}
			if (!image.isEmpty()) {
				eb.setImage(parseParams(image, params));
			}
			if (!author_name.isEmpty()) {
				if (!author_icon.isEmpty()) {
					if (!author_url.isEmpty()) {
						eb.setAuthor(parseParams(author_name, params), parseParams(author_url, params), parseParams(author_icon, params));
					} else {
						eb.setAuthor(parseParams(author_name, params), null, parseParams(author_icon, params));
					}
				} else {
					if (!author_url.isEmpty()) {
						eb.setAuthor(parseParams(author_name, params), parseParams(author_url, params));
					} else {
						eb.setAuthor(parseParams(author_name, params), null);
					}
				}
			}
			builder.setEmbeds(eb.build());
		}
		channel.sendMessage(builder.build()).queue();
	}

	public static void broadcast(String msg) {
		for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
			player.sendMessage(new StringTextComponent(msg.replaceAll("&", "\u00A7")), randomuuid);
		}
	}

	private void setup(final FMLCommonSetupEvent event) {

	}

	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		server = event.getServer();
		LOGGER.info("Initializing config...");
		Config.init();
		LOGGER.info("Setting up config...");
		Config.setup();
		LOGGER.info("Building JDA...");
		try {
			try {
				jda = JDABuilder.createDefault(Config.getString("token")).setEnabledIntents(EnumSet.allOf(GatewayIntent.class)).build();
			} catch (Exception e) {
				throw new StartException(e);
			}
			LOGGER.info("Awaiting JDA...");
			try {
				jda.awaitReady();
			} catch (Exception e) {
				throw new StartException(e);
			}
			guild = jda.getGuildById(Config.getString("guild"));
			if (guild == null) throw new StartException("No guild found with the configured id.");
			channel = guild.getTextChannelById(Config.getString("channel"));
			if (channel == null) throw new StartException("No text channel found with the configured id.");
		} catch (StartException e) {
			e.printStackTrace();
			jda = null;
		}
		for (Map.Entry<String, ForgeConfigSpec.ConfigValue<?>> entry : Config.cv.entrySet()) {
			String s = entry.getKey();
			ForgeConfigSpec.ConfigValue<?> configValue = entry.getValue();
			if (s.startsWith("evt.") && s.endsWith("enabled")) {
				if ((Boolean) configValue.get()) {
					switch (s.split("\\.")[1]) {
						case "join":
							listeners.put("join", new JoinListener());
							break;
						case "quit":
							listeners.put("quit", new QuitListener());
							break;
						case "advancement":
							listeners.put("advancement", new AdvancementListener());
							break;
						case "chat":
							listeners.put("chat", new ChatListener());
							break;
						case "death":
							listeners.put("death", new DeathListener());
							break;
						case "start":
							listeners.put("start", new StartListener());
							break;
						case "stop":
							listeners.put("stop", new StopListener());
							break;
						case "lag":
							listeners.put("lag", new LagListener());
							break;
						case "discordchat":
							jdalisteners.put("discordchat", new Message());
							break;
					}
				}
			}
		}
		for (Map.Entry<String, Object> entry : listeners.entrySet()) {
			String s = entry.getKey();
			Object o = entry.getValue();
			LOGGER.info("Registering event(s) for the " + s + " listener.");
			MinecraftForge.EVENT_BUS.register(o);
		}
		for (Map.Entry<String, Object> entry : jdalisteners.entrySet()) {
			String s = entry.getKey();
			Object o = entry.getValue();
			LOGGER.info("Registering jda event(s) for the " + s + " listener.");
			if (jda != null) {
				jda.addEventListener(o);
			}
		}
	}

	@SubscribeEvent
	public void onStop(FMLServerStoppedEvent event) {
		if (jda != null) {
			try {
				jda.shutdown();
			} catch (Exception e) {
				//noinspection ConstantConditions
				if (!(e instanceof ClassNotFoundException)) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class StartException extends Exception {
		public StartException(String s) {
			super(s);
		}

		public StartException(Throwable e) {
			super(e);
		}
	}
}
