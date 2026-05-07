package br.com.reminderbot.consumer;

import static br.com.reminderbot.configuration.RabbitMQConstants.WORKDAY_REMINDER_NOTIFICATION_QUEUE;

import br.com.reminderbot.service.DiscordPrivateMessageService;
import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkDayReminderNotificationConsumer
{
	private static final Logger LOGGER =
		LoggerFactory.getLogger(WorkDayReminderNotificationConsumer.class);

	private final JDA jda;

	private final DiscordPrivateMessageService discordPrivateMessageService;

	public WorkDayReminderNotificationConsumer(JDA jda,
		DiscordPrivateMessageService discordPrivateMessageService)
	{
		this.jda = jda;
		this.discordPrivateMessageService = discordPrivateMessageService;
	}

	@RabbitListener(queues = WORKDAY_REMINDER_NOTIFICATION_QUEUE)
	public void consume(WorkDayReminderNotificationEvent event)
	{
		if (event == null || event.discordUserId() == null || event.discordUserId().isBlank())
		{
			LOGGER.warn("Ignoring work day reminder notification with missing discord user id");
			return;
		}
		if (event.message() == null || event.message().isBlank())
		{
			LOGGER.warn("Ignoring work day reminder notification with missing message. discordUserId={}",
				maskDiscordUserId(event.discordUserId()));
			return;
		}

		jda.retrieveUserById(event.discordUserId().trim())
			.queue(user -> discordPrivateMessageService.sendMessage(user, event.message()),
				error -> LOGGER.warn("Could not retrieve Discord user {} to send work day reminder",
					maskDiscordUserId(event.discordUserId()), error));
	}

	private String maskDiscordUserId(String discordUserId)
	{
		String trimmed = discordUserId.trim();
		if (trimmed.length() <= 4)
		{
			return "****";
		}
		return "****" + trimmed.substring(trimmed.length() - 4);
	}
}
