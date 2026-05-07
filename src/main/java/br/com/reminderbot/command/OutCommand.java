package br.com.reminderbot.command;

import br.com.reminderbot.application.workday.dto.WorkDayResponse;
import br.com.reminderbot.model.Author;
import br.com.reminderbot.model.RegisterType;
import br.com.reminderbot.producer.MarkingRegisterProducer;
import br.com.reminderbot.producer.TimeMarkRegisteredEvent;
import br.com.reminderbot.service.DiscordPrivateMessageService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class OutCommand implements DiscordCommand
{
	private final MarkingRegisterProducer markingRegisterProducer;

	private final DiscordPrivateMessageService discordPrivateMessageService;

	public OutCommand(MarkingRegisterProducer markingRegisterProducer,
		DiscordPrivateMessageService discordPrivateMessageService)
	{
		this.markingRegisterProducer = markingRegisterProducer;
		this.discordPrivateMessageService = discordPrivateMessageService;
	}

	@Override
	public String name()
	{
		return "out";
	}

	@Override
	public void execute(MessageReceivedEvent event)
	{
		String content = event.getMessage().getContentRaw().trim().toLowerCase(Locale.ROOT);

		if (!content.equals("out"))
		{
			discordPrivateMessageService.sendMessage(event.getAuthor(),
				"Comando inv\u00e1lido. Use `out`.");

			return;
		}

		User user = event.getAuthor();

		Author author = new Author(user.getIdLong(), user.getName(), user.getGlobalName());

		TimeMarkRegisteredEvent timeMarkRegisteredEvent = new TimeMarkRegisteredEvent(author,
			RegisterType.OUT, LocalDateTime.now());

		WorkDayResponse workDayResponse = markingRegisterProducer.send(timeMarkRegisteredEvent);

		LocalTime exitTime = workDayResponse.getExitTime();

		if (exitTime == null)
		{
			return;
		}

		discordPrivateMessageService.sendWorkDaySummary(user, workDayResponse);
	}
}
