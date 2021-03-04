package com.github.ygimenez.model;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.EnumSet;

public class PrivilegeLevel {
	private final String name;
	private final Permission[] requirements;

	public PrivilegeLevel(String name, Permission... requirements) {
		this.name = name;
		this.requirements = requirements;
	}

	public PrivilegeLevel(String name, EnumSet<Permission> requirements) {
		this.name = name;
		this.requirements = requirements.toArray(new Permission[0]);
	}

	public String getName() {
		return name;
	}

	public Permission[] getRequirements() {
		return requirements;
	}

	public boolean checkRequirements(Message message) {
		if (message.getChannelType() == ChannelType.PRIVATE) return false;

		Member mb = message.getMember();
		assert mb != null;
		return mb.hasPermission(message.getTextChannel(), requirements);
	}
}
