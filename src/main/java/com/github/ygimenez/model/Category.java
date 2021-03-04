package com.github.ygimenez.model;

import com.github.ygimenez.handler.CommandManager;
import net.dv8tion.jda.api.entities.*;

import java.util.Objects;
import java.util.Set;

public class Category {
	private final String name;
	private final String description;
	private final PrivilegeLevel privilegeLevel;
	private final boolean nsfw;
	private boolean enabled;

	public Category(String name, String description, PrivilegeLevel privilegeLevel, boolean nsfw) {
		this.name = name;
		this.description = description;
		this.privilegeLevel = privilegeLevel;
		this.nsfw = nsfw;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public PrivilegeLevel getPrivilegeLevel() {
		return privilegeLevel;
	}

	public Set<PreparedCommand> getCommands() {
		return UCH.getManager().getCommands(this);
	}

	public boolean isEnabled(MessageChannel channel) {
		if (!enabled) return false;
		else if (channel.getType() == ChannelType.PRIVATE) return true;
		else return !nsfw || ((TextChannel) channel).isNSFW();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Category category = (Category) o;
		return Objects.equals(name, category.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
