package br.com.reminderbot.configuration;

public final class RabbitMQConstants
{
	public static final String MARKING_REGISTER_EXCHANGE = "marking.register.exchange";

	public static final String MARKING_REGISTER_QUEUE = "marking.register.queue";

	public static final String MARKING_REGISTER_ROUTING_KEY = "marking.register.created";

	private RabbitMQConstants()
	{
	}
}
