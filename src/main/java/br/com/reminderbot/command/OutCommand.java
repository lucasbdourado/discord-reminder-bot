package br.com.reminderbot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class OutCommand implements DiscordCommand
{
	@Override
	public String name()
	{
		return "out";
	}

	@Override
	public void execute(MessageReceivedEvent event)
	{
		event.getChannel().sendMessage("Saída registrada").queue();
	}
}
