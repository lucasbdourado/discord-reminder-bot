package br.com.reminderbot.producer;

import br.com.reminderbot.model.Author;
import br.com.reminderbot.model.RegisterType;
import java.time.LocalDateTime;

public record MarkingRegisterEvent(Author author, String channelId, RegisterType type,
                                   LocalDateTime registeredAt)
{
}
