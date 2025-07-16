package SpringTranslateApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;


@ComponentScan(basePackages = "SpringTranslateApi")
@SpringBootApplication
@EnableAutoConfiguration(exclude = MongoAutoConfiguration.class)
@EnableKafka
public class SpringTranslateApplication {

	
	
	public static void main(String[] args) {

		SpringApplication.run(SpringTranslateApplication.class, args);
	}

}
