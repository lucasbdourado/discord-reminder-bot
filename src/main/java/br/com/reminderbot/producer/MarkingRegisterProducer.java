package br.com.reminderbot.producer;

import static br.com.reminderbot.configuration.RabbitMQConstants.MARKING_REGISTER_EXCHANGE;
import static br.com.reminderbot.configuration.RabbitMQConstants.MARKING_REGISTER_ROUTING_KEY;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class MarkingRegisterProducer
{
	private final RabbitTemplate rabbitTemplate;

	public MarkingRegisterProducer(RabbitTemplate rabbitTemplate)
	{
		this.rabbitTemplate = rabbitTemplate;
	}

	public void send(TimeMarkRegisteredEvent event)
	{
		rabbitTemplate.convertAndSend(MARKING_REGISTER_EXCHANGE, MARKING_REGISTER_ROUTING_KEY, event);
	}
}
