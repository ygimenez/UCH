package com.github.ygimenez.model;

import com.github.ygimenez.handler.CommandManager;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class UCH {
	private static CommandManager manager;
	private static Map<String, PrivilegeLevel> privilegeLevels;
	private static Map<String, Category> categories;
	private static boolean localized;
	private static boolean finished;
	public static final Logger logger = JDALogger.getLog("UCH");

	public static CommandManager getManager() {
		if (!finished) throw new IllegalStateException("You cannot retrieve UCH values before finishing setup.");
		return manager;
	}

	public static void setManager(CommandManager manager) {
		if (finished) throw new IllegalStateException("You cannot modify UCH settings after finishing setup.");
		UCH.manager = manager;
	}

	public static Map<String, PrivilegeLevel> getPrivilegeLevels() {
		if (!finished) throw new IllegalStateException("You cannot retrieve UCH values before finishing setup.");
		return privilegeLevels;
	}

	public static void setPrivilegeLevels(Map<String, PrivilegeLevel> privilegeLevels) {
		if (finished) throw new IllegalStateException("You cannot modify UCH settings after finishing setup.");
		UCH.privilegeLevels = privilegeLevels;
	}

	public static Map<String, Category> getCategories() {
		if (!finished) throw new IllegalStateException("You cannot retrieve UCH values before finishing setup.");
		return categories;
	}

	public static void setCategories(Map<String, Category> categories) {
		if (finished) throw new IllegalStateException("You cannot modify UCH settings after finishing setup.");
		UCH.categories = categories;
	}

	public static boolean isLocalized() {
		if (!finished) throw new IllegalStateException("You cannot retrieve UCH values before finishing setup.");
		return localized;
	}

	public static void setLocalized(boolean localized) {
		if (finished) throw new IllegalStateException("You cannot modify UCH settings after finishing setup.");
		UCH.localized = localized;
	}

	public static ResourceBundle getLocale(String lang) {
		if (!finished) throw new IllegalStateException("You cannot retrieve UCH values before finishing setup.");
		return ResourceBundle.getBundle("locale", Locale.forLanguageTag(lang));
	}

	public static boolean isFinished() {
		return finished;
	}

	public static void finish() {
		if (finished) throw new IllegalStateException("You cannot modify UCH settings after finishing setup.");
		UCH.finished = true;
	}
}
