package br.com.reminderbot.producer;

import br.com.reminderbot.model.RegisterType;
import java.time.LocalDateTime;

public record RegisterTimeMarkCommand(Long authorDiscordId, String authorCode, String authorName,
                                      RegisterType type, LocalDateTime registeredAt)
{
}
