package br.com.reminderbot.command;

import br.com.reminderbot.application.workday.dto.WorkDayResponse;
import br.com.reminderbot.model.RegisterType;
import br.com.reminderbot.producer.MarkingRegisterProducer;
import br.com.reminderbot.producer.RegisterTimeMarkCommand;
import br.com.reminderbot.service.DiscordPrivateMessageService;
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

	private final DiscordPrivateMessageService discordPrivateMessageService;

	public InCommand(MarkingRegisterProducer markingRegisterProducer,
		DiscordPrivateMessageService discordPrivateMessageService)
	{
		this.markingRegisterProducer = markingRegisterProducer;
		this.discordPrivateMessageService = discordPrivateMessageService;
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
			discordPrivateMessageService.sendMessage(event.getAuthor(),
				"Comando inv\u00e1lido. Use `in`, `in h`, `in home` ou `in (home)`.");

			return;
		}

		User user = event.getAuthor();

		RegisterTimeMarkCommand registerTimeMarkCommand = new RegisterTimeMarkCommand(user.getIdLong(),
			user.getName(), user.getGlobalName(), registerType, LocalDateTime.now());

		WorkDayResponse workDayResponse = markingRegisterProducer.send(registerTimeMarkCommand);

		LocalTime exitTime = workDayResponse.getExitTime();

		if (exitTime == null)
		{
			return;
		}

		discordPrivateMessageService.sendWorkDaySummary(user, workDayResponse);
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
