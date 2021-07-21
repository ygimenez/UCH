package com.github.ygimenez.handler;

import com.github.ygimenez.annotations.Command;
import com.github.ygimenez.annotations.Requires;
import com.github.ygimenez.interfaces.Executable;
import com.github.ygimenez.model.Category;
import com.github.ygimenez.model.PreparedCommand;
import com.github.ygimenez.model.UCH;
import net.dv8tion.jda.api.Permission;
import org.reflections8.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommandManager {
	private final Set<Class<?>> cmds;

	public CommandManager(String commandsPath) {
		Reflections refl = new Reflections(commandsPath);
		this.cmds = refl.getTypesAnnotatedWith(Command.class);
	}

	public Set<PreparedCommand> getCommands() {
		Set<PreparedCommand> commands = new HashSet<>();

		for (Class<?> cmd : cmds) {
			Command params = cmd.getDeclaredAnnotation(Command.class);
			extractCommand(commands, cmd, params, null);
		}

		return commands;
	}

	public Set<PreparedCommand> getCommands(Category category) {
		Set<PreparedCommand> commands = new HashSet<>();

		for (Class<?> cmd : cmds) {
			Command params = cmd.getDeclaredAnnotation(Command.class);
			if (params.category().equals(category.getName())) {
				extractCommand(commands, cmd, params, null);
			}
		}

		return commands;
	}

	public Set<PreparedCommand> getCommands(String locale) {
		Set<PreparedCommand> commands = new HashSet<>();

		for (Class<?> cmd : cmds) {
			Command params = cmd.getDeclaredAnnotation(Command.class);
			extractCommand(commands, cmd, params, locale);
		}

		return commands;
	}

	public Set<PreparedCommand> getCommands(Category category, String locale) {
		Set<PreparedCommand> commands = new HashSet<>();

		for (Class<?> cmd : cmds) {
			Command params = cmd.getDeclaredAnnotation(Command.class);
			if (params.category().equals(category.getName())) {
				extractCommand(commands, cmd, params, locale);
			}
		}

		return commands;
	}

	public PreparedCommand getCommand(String name) {
		for (Class<?> cmd : cmds) {
			Command params = cmd.getDeclaredAnnotation(Command.class);
			if (name.equalsIgnoreCase(params.name()) || Arrays.stream(params.aliases()).anyMatch(name::equalsIgnoreCase)) {
				Requires req = cmd.getDeclaredAnnotation(Requires.class);
				return new PreparedCommand(
						params.name(),
						params.aliases(),
						params.usage(),
						UCH.isLocalized() ?
								"cmd_" + cmd.getSimpleName()
										.replaceAll("Command", "")
										.replaceAll("[a-z](?=[A-Z])", "$0-")
										.toLowerCase()
								: params.description(),
						params.category(),
						req == null ? new Permission[0] : req.value(),
						buildCommand(cmd),
						null
				);
			}
		}

		return null;
	}

	public PreparedCommand getCommand(String name, String locale) {
		for (Class<?> cmd : cmds) {
			Command params = cmd.getDeclaredAnnotation(Command.class);
			if (name.equalsIgnoreCase(params.name()) || Arrays.stream(params.aliases()).anyMatch(name::equalsIgnoreCase)) {
				Requires req = cmd.getDeclaredAnnotation(Requires.class);
				return new PreparedCommand(
						params.name(),
						params.aliases(),
						params.usage(),
						UCH.isLocalized() ?
								"cmd_" + cmd.getSimpleName()
										.replaceAll("Command", "")
										.replaceAll("[a-z](?=[A-Z])", "$0-")
										.toLowerCase()
								: params.description(),
						params.category(),
						req == null ? new Permission[0] : req.value(),
						buildCommand(cmd),
						locale
				);
			}
		}

		return null;
	}

	private void extractCommand(Set<PreparedCommand> commands, Class<?> cmd, Command params, String locale) {
		Requires req = cmd.getDeclaredAnnotation(Requires.class);
		commands.add(new PreparedCommand(
				params.name(),
				params.aliases(),
				params.usage(),
				UCH.isLocalized() ?
						"cmd_" + cmd.getSimpleName()
								.replaceAll("Command|Reaction", "")
								.replaceAll("[a-z](?=[A-Z])", "$0-")
								.toLowerCase()
						: params.description(),
				params.category(),
				req == null ? new Permission[0] : req.value(),
				buildCommand(cmd),
				locale
		));
	}

	private Executable buildCommand(Class<?> klass) {
		try {
			return (Executable) klass.getConstructor().newInstance();
		} catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			UCH.logger.error(e.getMessage(), e);

			return null;
		}
	}
}