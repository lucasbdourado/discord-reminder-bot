package br.com.reminderbot.command;

import br.com.reminderbot.model.Author;
import br.com.reminderbot.model.RegisterType;
import br.com.reminderbot.producer.MarkingRegisterProducer;
import br.com.reminderbot.producer.TimeMarkRegisteredEvent;
import java.time.LocalDateTime;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class OutCommand implements DiscordCommand
{
	private final MarkingRegisterProducer markingRegisterProducer;

	public OutCommand(MarkingRegisterProducer markingRegisterProducer)
	{
		this.markingRegisterProducer = markingRegisterProducer;
	}

	@Override
	public String name()
	{
		return "out";
	}

	@Override
	public void execute(MessageReceivedEvent event)
	{
		User user = event.getAuthor();

		String channelId = event.getChannel().getId();

		Author author = new Author(user.getIdLong(), user.getName(), user.getGlobalName());

		TimeMarkRegisteredEvent timeMarkRegisteredEvent = new TimeMarkRegisteredEvent(author,
			RegisterType.OUT, LocalDateTime.now());

		markingRegisterProducer.send(timeMarkRegisteredEvent);
	}
}
