package io.project.KitapChooseBot.config;





import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@PropertySource("application.properties")
@Data

public class BotConfig {

	@Value("${bot.name}")
	String botName;
	
	
	@Value("${bot.token}")
	String token;


	public String getBotName() {
		// TODO Auto-generated method stub
		return this.botName;
	}
	
	public String getToken() {
		return this.token;
	}
	
	
	
}
