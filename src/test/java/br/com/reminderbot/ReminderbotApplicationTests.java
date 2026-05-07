package br.com.reminderbot;

import net.dv8tion.jda.api.JDA;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ReminderbotApplicationTests
{
	@MockitoBean
	private JDA jda;

	@Test
	void contextLoads()
	{
	}

}
