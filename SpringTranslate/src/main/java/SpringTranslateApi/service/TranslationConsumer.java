package SpringTranslateApi.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import SpringTranslateApi.model.dto.TranslationRequest;

@Service
public class TranslationConsumer {
	 @Autowired
	    private KafkaTemplate<String, String> kafkaTemplate;

	    @Autowired
	    private OllamaTranslationService translationService;

	    @KafkaListener(topics = "translation-requests", groupId = "translation-group")
	    public void listen(String message) {
	        try {
	        	
	            System.out.println("Received message on translation-requests topic: " + message);

				Thread.sleep(15000);

	            JSONObject json = new JSONObject(message);
	            String correlationId = json.getString("correlationId");
	            String source = json.getString("sourceLang");
	            String target = json.getString("targetLang");
	            String text = json.getString("text");

	           
	            TranslationRequest req = new TranslationRequest(source, target, text);
	            String result = translationService.translate(req).getTranslatedText();

	            JSONObject responseJson = new JSONObject();
	            responseJson.put("correlationId", correlationId);
	            responseJson.put("translatedText", result);

	            kafkaTemplate.send("translation-responses", responseJson.toString());
	        
	            System.out.println("Sending translated message: " + responseJson.toString());
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	            
	        }
	        
	    }
	   
	    @KafkaListener(topics = "translation-responses", groupId = "debug-logger-group")
	    public void listenTranslationResponse(String message) {
	        try {
	            System.out.println("Kafka Logger - Received message on translation-responses topic: " + message);



	            JSONObject json = new JSONObject(message);
	            String correlationId = json.getString("correlationId");
	            String translatedText = json.getString("translatedText");

	            System.out.println("Kafka Logger - Translated Text: " + translatedText + " for correlationId: " + correlationId);
	        } catch (Exception e) {
	            System.err.println("Kafka Logger - Error parsing translation response");
	            e.printStackTrace();
	        }
	    }

}
