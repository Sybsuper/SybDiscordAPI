package com.sybsuper.sybdiscordapi;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

public class SybTranslator {
	public static String translate(String key, Object... args) {
		String txt = LanguageMap.getInstance().getLanguageData().get(key);
		if (txt == null || txt.isEmpty()) {
			return txt;
		}
		if (args.length > 0) {
			return String.format(txt, translateList(args));
		} else {
			return txt;
		}
	}

	public static String translate(TranslationTextComponent component) {
		return discordFormat(translate(component.getKey(), component.getFormatArgs()), component.getStyle());
	}

	public static Object[] translateList(Object... list) {
		for (int i = 0; i < list.length; i++) {
			if (list[i] instanceof ITextComponent) {
				list[i] = translateTextComponent((ITextComponent) list[i]);
			}
		}
		return list;
	}

	public static String translateTextComponent(ITextComponent component) {
		StringBuilder raw = new StringBuilder();
		// Do this or not idk man?
		//raw.append(component.getUnformattedComponentText());
		if (component.getSiblings().isEmpty()) {
			if (component instanceof TranslationTextComponent) {
				raw.append(translate((TranslationTextComponent) component));
			} else {
				raw.append(discordFormat(component));
			}
		} else {
			for (ITextComponent sibling : component.getSiblings()) {
				raw.append(translateTextComponent(sibling));
			}
		}
		return discordFormat(raw.toString(), component.getStyle());
	}

	public static String discordFormat(ITextComponent component) {
		String raw = component.getUnformattedComponentText();
		Style style = component.getStyle();
		return discordFormat(raw, style);
	}

	public static String discordFormat(String raw, Style style) {
		if (style.getBold()) {
			raw = "**" + raw + "**";
		}
		if (style.getItalic()) {
			raw = "_" + raw + "_";
		}
		if (style.getObfuscated()) {
			raw = "||" + raw + "||";
		}
		if (style.getUnderlined()) {
			raw = "__" + raw + "__";
		}
		return raw;
	}
}
