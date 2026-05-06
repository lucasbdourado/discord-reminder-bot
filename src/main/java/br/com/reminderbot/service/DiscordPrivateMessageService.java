package br.com.reminderbot.service;

import static br.com.reminderbot.listener.DiscordButtonListener.WORKDAY_REMINDER_ENABLE_COMPONENT_ID;

import br.com.reminderbot.application.workday.dto.WorkDayResponse;
import java.awt.Color;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DiscordPrivateMessageService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DiscordPrivateMessageService.class);

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	public void sendMessage(User user, String message)
	{
		if (user == null)
		{
			LOGGER.warn("Could not send private Discord message because user is null");
			return;
		}

		user.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue(null,
			error -> LOGGER.warn("Could not send private Discord message to user {}", user.getId(),
				error));
	}

	public void sendWorkDaySummary(User user, WorkDayResponse response)
	{
		if (user == null)
		{
			LOGGER.warn("Could not send work day summary because user is null");
			return;
		}

		user.openPrivateChannel()
			.flatMap(channel -> channel.sendMessageEmbeds(buildWorkDaySummaryEmbed(response)))
			.queue(null,
				error -> LOGGER.warn("Could not send work day summary to user {}", user.getId(), error));
	}

	public MessageEmbed buildWorkDaySummaryEmbed(WorkDayResponse response)
	{
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Resumo do ponto");
		embed.setColor(Color.GREEN);
		embed.addField("**Data**", formatDate(response), false);
		embed.addField("**Marcações**", formatMarkings(response), false);
		embed.addField("**Tempo trabalhado**",
			formatMinutes(response == null ? 0 : response.getWorkedMinutes()), true);
		embed.addField("**Tempo restante**",
			formatMinutes(response == null ? 0 : response.getRemainingMinutes()), true);
		embed.addField("**Horário de saída**", formatExitTime(response), true);

		return embed.build();
	}

	private String formatMarkings(WorkDayResponse response)
	{
		List<LocalTime> times = response == null ? null : response.getTimes();

		if (times == null || times.isEmpty())
		{
			return "Nenhuma marcação encontrada";
		}

		StringBuilder markings = new StringBuilder();

		for (LocalTime time : times)
		{
			markings.append(formatTime(time)).append("\n");
		}

		return markings.toString();
	}

	private String formatExitTime(WorkDayResponse response)
	{
		if (response == null)
		{
			return "Não calculado";
		}

		return formatTime(response.getExitTime());
	}

	private String formatTime(LocalTime time)
	{
		if (time == null)
		{
			return "Não calculado";
		}

		return time.format(TIME_FORMATTER);
	}

	private String formatStatus(WorkDayResponse response)
	{
		if (response == null || response.getStatus() == null || response.getStatus().isBlank())
		{
			return "Não informado";
		}

		return response.getStatus();
	}

	private String formatMinutes(long minutes)
	{
		String sign = minutes < 0 ? "-" : "";
		long absoluteMinutes = Math.abs(minutes);
		long hours = absoluteMinutes / 60;
		long remainingMinutes = absoluteMinutes % 60;

		return String.format("%s%02d:%02d", sign, hours, remainingMinutes);
	}

	private String formatDate(WorkDayResponse response)
	{
		if (response == null || response.getDate() == null)
		{
			return "Não informada";
		}

		return response.getDate().format(DATE_FORMATTER);
	}
}
