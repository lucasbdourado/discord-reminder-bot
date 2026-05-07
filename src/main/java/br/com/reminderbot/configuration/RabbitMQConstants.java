package br.com.reminderbot.configuration;

public final class RabbitMQConstants
{
	public static final String MARKING_REGISTER_EXCHANGE = "marking.register.exchange";

	public static final String MARKING_REGISTER_QUEUE = "marking.register.queue";

	public static final String MARKING_REGISTER_ROUTING_KEY = "marking.register.created";

	public static final String REMINDER_EVENTS_EXCHANGE = "reminder.events";

	public static final String WORKDAY_REMINDER_PREFERENCE_CHANGED_ROUTING_KEY =
		"workday.reminder.preference.changed";

	private RabbitMQConstants()
	{
	}
}
