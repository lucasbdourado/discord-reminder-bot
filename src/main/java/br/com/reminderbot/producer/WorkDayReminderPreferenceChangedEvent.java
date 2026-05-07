package br.com.reminderbot.producer;

import java.time.LocalTime;

public record WorkDayReminderPreferenceChangedEvent(String discordUserId, boolean enabled,
	LocalTime exitTime)
{
}
