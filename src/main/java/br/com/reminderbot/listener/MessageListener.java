package br.com.reminderbot.listener;

import br.com.reminderbot.command.DiscordCommand;
import br.com.reminderbot.service.DiscordPrivateMessageService;
import java.util.List;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageListener extends ListenerAdapter
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

	private static final String CHANNEL_ID = "1501564696120266792";

	private final List<DiscordCommand> commands;

	private final DiscordPrivateMessageService discordPrivateMessageService;

	public MessageListener(List<DiscordCommand> commands,
		DiscordPrivateMessageService discordPrivateMessageService)
	{
		this.commands = commands;
		this.discordPrivateMessageService = discordPrivateMessageService;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if (event.getAuthor().isBot())
		{
			return;
		}

		String content = event.getMessage().getContentRaw().toLowerCase();

		String channel = event.getChannel().getId();

		if (!channel.equals(CHANNEL_ID))
		{
			return;
		}

		commands.stream().filter(command -> content.startsWith(command.name())).findFirst()
			.ifPresent(command -> execute(command, event));
	}

	private void execute(DiscordCommand command, MessageReceivedEvent event)
	{
		try
		{
			command.execute(event);
		}
		catch (RuntimeException exception)
		{
			LOGGER.error("Failed to execute Discord command {}", command.name(), exception);
			discordPrivateMessageService.sendMessage(event.getAuthor(),
				"N\u00e3o foi poss\u00edvel processar seu ponto agora. Tente novamente em alguns instantes.");
		}
	}
}
