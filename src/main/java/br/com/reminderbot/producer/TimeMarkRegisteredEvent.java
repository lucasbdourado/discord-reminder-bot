package br.com.reminderbot.producer;

import br.com.reminderbot.model.Author;
import br.com.reminderbot.model.RegisterType;
import java.time.LocalDateTime;

public record TimeMarkRegisteredEvent(Author author, RegisterType type, LocalDateTime registeredAt)
{
}
