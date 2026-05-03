package br.com.reminderbot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface DiscordCommand
{
	String name();

	void execute(MessageReceivedEvent event);
}
