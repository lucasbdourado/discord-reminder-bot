package br.com.reminderbot.command;

import br.com.reminderbot.model.Author;
import br.com.reminderbot.model.RegisterType;
import br.com.reminderbot.producer.MarkingRegisterEvent;
import java.time.LocalDateTime;
import java.util.Locale;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class InCommand implements DiscordCommand
{
	@Override
	public String name()
	{
		return "in";
	}

	@Override
	public void execute(MessageReceivedEvent event)
	{
		User user = event.getAuthor();

		String channelId = event.getChannel().getId();

		String content = event.getMessage().getContentRaw().trim().toLowerCase(Locale.ROOT);

		Author author = new Author(user.getIdLong(), user.getName(), user.getGlobalName());

		if (content.equals("in"))
		{
			event.getChannel().sendMessage("Entrada registrada").queue();

			MarkingRegisterEvent markingRegisterEvent = new MarkingRegisterEvent(author, channelId,
				RegisterType.IN, LocalDateTime.now());

			return;
		}

		if (content.equals("in h") || content.equals("in home") || content.equals("in (home)"))
		{
			event.getChannel().sendMessage("Entrada home office registrada").queue();

			MarkingRegisterEvent markingRegisterEvent = new MarkingRegisterEvent(author, channelId,
				RegisterType.IN_HOME, LocalDateTime.now());

			return;
		}

		event.getChannel().sendMessage("Comando inválido. Use `in`, `in h`, `in home` ou `in (home)`.")
			.queue();
	}
}
