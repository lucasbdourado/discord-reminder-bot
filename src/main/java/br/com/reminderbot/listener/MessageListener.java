package br.com.reminderbot.listener;

import br.com.reminderbot.command.DiscordCommand;
import java.util.List;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class MessageListener extends ListenerAdapter
{
	private final List<DiscordCommand> commands;

	public MessageListener(List<DiscordCommand> commands)
	{
		this.commands = commands;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if (event.getAuthor().isBot())
		{
			return;
		}

		String content = event.getMessage().getContentRaw().toLowerCase();

		commands.stream().filter(command -> content.startsWith(command.name())).findFirst()
			.ifPresent(command -> command.execute(event));
	}
}
