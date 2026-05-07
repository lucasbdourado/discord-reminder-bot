package br.com.reminderbot.configuration;

import static br.com.reminderbot.configuration.RabbitMQConstants.MARKING_REGISTER_EXCHANGE;
import static br.com.reminderbot.configuration.RabbitMQConstants.MARKING_REGISTER_QUEUE;
import static br.com.reminderbot.configuration.RabbitMQConstants.MARKING_REGISTER_ROUTING_KEY;
import static br.com.reminderbot.configuration.RabbitMQConstants.REMINDER_EVENTS_EXCHANGE;

import br.com.reminderbot.application.workday.dto.WorkDayResponse;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
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
	public TopicExchange reminderEventsExchange()
	{
		return new TopicExchange(REMINDER_EVENTS_EXCHANGE);
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
		DefaultClassMapper classMapper = new DefaultClassMapper();
		classMapper.setIdClassMapping(Map.of(
			"br.com.lucasbdourado.electronictimemarking.application.dto.WorkDayResponse",
			WorkDayResponse.class
		));

		JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
		converter.setClassMapper(classMapper);
		return converter;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
		JacksonJsonMessageConverter jsonMessageConverter,
		@Value("${app.rabbitmq.marking-register.reply-timeout:30000}") long replyTimeout)
	{
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter);
		rabbitTemplate.setReplyTimeout(replyTimeout);

		return rabbitTemplate;
	}
}
