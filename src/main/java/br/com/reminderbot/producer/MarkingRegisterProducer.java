package br.com.reminderbot.producer;

import static br.com.reminderbot.configuration.RabbitMQConstants.MARKING_REGISTER_EXCHANGE;
import static br.com.reminderbot.configuration.RabbitMQConstants.MARKING_REGISTER_ROUTING_KEY;

import br.com.reminderbot.application.workday.dto.WorkDayResponse;
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

	public WorkDayResponse send(RegisterTimeMarkCommand event)
	{
		Object response = rabbitTemplate.convertSendAndReceive(MARKING_REGISTER_EXCHANGE,
			MARKING_REGISTER_ROUTING_KEY, event);

		if (response == null)
		{
			throw new IllegalStateException("Timeout aguardando resposta do processamento de ponto");
		}

		return (WorkDayResponse) response;
	}
}
