package br.com.reminderbot.consumer;

public record WorkDayReminderNotificationEvent(String discordUserId, String message)
{
}
