package br.com.reminderbot.command;

import br.com.reminderbot.application.workday.dto.WorkDayResponse;
import br.com.reminderbot.model.Author;
import br.com.reminderbot.model.RegisterType;
import br.com.reminderbot.producer.MarkingRegisterProducer;
import br.com.reminderbot.producer.TimeMarkRegisteredEvent;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class InCommand implements DiscordCommand
{
	private final MarkingRegisterProducer markingRegisterProducer;

	public InCommand(MarkingRegisterProducer markingRegisterProducer)
	{
		this.markingRegisterProducer = markingRegisterProducer;
	}

	@Override
	public String name()
	{
		return "in";
	}

	@Override
	public void execute(MessageReceivedEvent event)
	{
		String content = event.getMessage().getContentRaw().trim().toLowerCase(Locale.ROOT);

		RegisterType registerType = resolveRegisterType(content);

		if (registerType == null)
		{
			event.getChannel()
				.sendMessage("Comando inválido. Use `in`, `in h`, `in home` ou `in (home)`.").queue();

			return;
		}

		User user = event.getAuthor();

		String channelId = event.getChannel().getId();

		Author author = new Author(user.getIdLong(), user.getName(), user.getGlobalName());

		TimeMarkRegisteredEvent timeMarkRegisteredEvent = new TimeMarkRegisteredEvent(author,
			registerType, LocalDateTime.now());

		WorkDayResponse workDayResponse = markingRegisterProducer.send(timeMarkRegisteredEvent);

		LocalTime exitTime = workDayResponse.getExitTime();

		if (exitTime == null)
		{
			return;
		}

		event.getChannel().sendMessage("Horário de saída: " + exitTime).queue();
	}

	private RegisterType resolveRegisterType(String content)
	{
		if (content.equals("in"))
		{
			return RegisterType.IN;
		}

		if (content.equals("in h") || content.equals("in home") || content.equals("in (home)"))
		{
			return RegisterType.IN_HOME;
		}

		return null;
	}
}
