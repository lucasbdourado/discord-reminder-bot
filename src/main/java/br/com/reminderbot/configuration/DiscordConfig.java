package br.com.reminderbot.configuration;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.MESSAGE_CONTENT;

import br.com.reminderbot.listener.DiscordButtonListener;
import br.com.reminderbot.listener.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordConfig
{

	@Bean
	public JDA jda(@Value("${discord.token}") String token, MessageListener messageListener,
		DiscordButtonListener discordButtonListener)
	{
		return JDABuilder.createDefault(token).enableIntents(GUILD_MESSAGES, MESSAGE_CONTENT)
			.addEventListeners(messageListener, discordButtonListener).build();
	}
}
