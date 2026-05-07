package br.com.reminderbot.producer;

import static br.com.reminderbot.configuration.RabbitMQConstants.REMINDER_EVENTS_EXCHANGE;
import static br.com.reminderbot.configuration.RabbitMQConstants.WORKDAY_REMINDER_PREFERENCE_CHANGED_ROUTING_KEY;

import java.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class WorkDayReminderPreferencePublisher
{
	private static final Logger LOGGER =
		LoggerFactory.getLogger(WorkDayReminderPreferencePublisher.class);

	private final RabbitTemplate rabbitTemplate;

	public WorkDayReminderPreferencePublisher(RabbitTemplate rabbitTemplate)
	{
		this.rabbitTemplate = rabbitTemplate;
	}

	public void publish(String discordUserId, boolean enabled, LocalTime exitTime)
	{
		WorkDayReminderPreferenceChangedEvent event =
			new WorkDayReminderPreferenceChangedEvent(discordUserId, enabled, exitTime);

		try
		{
			rabbitTemplate.convertAndSend(REMINDER_EVENTS_EXCHANGE,
				WORKDAY_REMINDER_PREFERENCE_CHANGED_ROUTING_KEY, event);
			LOGGER.info(
				"Published work day reminder preference change for discordUserId={} enabled={} exitTime={}",
				discordUserId, enabled, exitTime);
		}
		catch (RuntimeException error)
		{
			LOGGER.error("Could not publish work day reminder preference change for discordUserId={}",
				discordUserId, error);
			throw error;
		}
	}
}
