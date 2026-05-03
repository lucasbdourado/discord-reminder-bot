package br.com.reminderbot.configuration;

import static br.com.reminderbot.configuration.RabbitMQConstants.MARKING_REGISTER_EXCHANGE;
import static br.com.reminderbot.configuration.RabbitMQConstants.MARKING_REGISTER_QUEUE;
import static br.com.reminderbot.configuration.RabbitMQConstants.MARKING_REGISTER_ROUTING_KEY;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig
{
	@Bean
	public DirectExchange markingRegisterExchange()
	{
		return new DirectExchange(MARKING_REGISTER_EXCHANGE);
	}

	@Bean
	public Queue markingRegisterQueue()
	{
		return new Queue(MARKING_REGISTER_QUEUE, true);
	}

	@Bean
	public Binding markingRegisterBinding(Queue markingRegisterQueue,
		DirectExchange markingRegisterExchange)
	{
		return BindingBuilder.bind(markingRegisterQueue).to(markingRegisterExchange)
			.with(MARKING_REGISTER_ROUTING_KEY);
	}

	@Bean
	public JacksonJsonMessageConverter jsonMessageConverter()
	{
		return new JacksonJsonMessageConverter();
	}
}
