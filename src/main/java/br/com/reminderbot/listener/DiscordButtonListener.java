package br.com.reminderbot.listener;

import br.com.reminderbot.producer.WorkDayReminderPreferencePublisher;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DiscordButtonListener extends ListenerAdapter
{
	public static final String WORKDAY_REMINDER_ENABLE_COMPONENT_ID = "workday_reminder_enable";

	private static final String COMPONENT_ID_SEPARATOR = ":";

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscordButtonListener.class);

	private final WorkDayReminderPreferencePublisher workDayReminderPreferencePublisher;

	public DiscordButtonListener(WorkDayReminderPreferencePublisher workDayReminderPreferencePublisher)
	{
		this.workDayReminderPreferencePublisher = workDayReminderPreferencePublisher;
	}

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event)
	{
		String componentId = event.getComponentId();

		if (!isWorkDayReminderEnableComponent(componentId))
		{
			return;
		}

		String discordUserId = event.getUser().getId();
		LocalTime exitTime = resolveExitTime(componentId);

		try
		{
			workDayReminderPreferencePublisher.publish(discordUserId, true, exitTime);
			event.reply("\u2705 Solicita\u00e7\u00e3o registrada. O lembrete ser\u00e1 habilitado em breve.")
				.setEphemeral(true).queue();
		}
		catch (RuntimeException error)
		{
			LOGGER.error("Could not register work day reminder preference for discordUserId={}",
				discordUserId, error);
			event.reply("\u274c N\u00e3o foi poss\u00edvel registrar sua solicita\u00e7\u00e3o agora.")
				.setEphemeral(true).queue();
		}
	}

	public static String buildWorkDayReminderEnableComponentId(LocalTime exitTime)
	{
		if (exitTime == null)
		{
			return WORKDAY_REMINDER_ENABLE_COMPONENT_ID;
		}

		return WORKDAY_REMINDER_ENABLE_COMPONENT_ID + COMPONENT_ID_SEPARATOR + exitTime;
	}

	private boolean isWorkDayReminderEnableComponent(String componentId)
	{
		return WORKDAY_REMINDER_ENABLE_COMPONENT_ID.equals(componentId)
			|| componentId.startsWith(WORKDAY_REMINDER_ENABLE_COMPONENT_ID + COMPONENT_ID_SEPARATOR);
	}

	private LocalTime resolveExitTime(String componentId)
	{
		String prefix = WORKDAY_REMINDER_ENABLE_COMPONENT_ID + COMPONENT_ID_SEPARATOR;

		if (!componentId.startsWith(prefix))
		{
			return null;
		}

		String exitTime = componentId.substring(prefix.length());

		try
		{
			return LocalTime.parse(exitTime);
		}
		catch (DateTimeParseException error)
		{
			LOGGER.warn("Could not parse exit time from work day reminder button componentId={}",
				componentId);
			return null;
		}
	}
}
