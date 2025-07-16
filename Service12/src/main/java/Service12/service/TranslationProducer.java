package Service12.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TranslationProducer {
    private static final Logger log = LoggerFactory.getLogger(TranslationProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final Map<String, CompletableFuture<String>> responseMap = new ConcurrentHashMap<>();

    public String sendTranslationRequest(String requestJson) {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<String> future = new CompletableFuture<>();
        responseMap.put(correlationId, future);


        JSONObject json = new JSONObject(requestJson);
        json.put("correlationId", correlationId);

        try {

            kafkaTemplate.send("translation-requests", json.toString()).get(15, TimeUnit.SECONDS);
            log.info("Translation is sending...: {}", json.toString());

        } catch (Exception e) {
            log.error("Failed to send message: correlationId={}, content={}, error={}", correlationId, json, e.getMessage());

            throw new RuntimeException("Failed to send message.", e);
        }

        try {
            String result = future.get(60, TimeUnit.SECONDS);
            log.info("The answer is: Translation={}, correlationId={}", result, correlationId);

            return result;
        } catch (Exception e) {

            throw new RuntimeException("Timeout waiting for Kafka response for correlationId: " + correlationId, e);
        } finally {
            responseMap.remove(correlationId);
        }
    }
    @KafkaListener(topics = "translation-responses", groupId = "translation-group")
    public void listenResponse(String message) {
        JSONObject json = new JSONObject(message);
        String correlationId = json.getString("correlationId");
        String translatedText = json.getString("translatedText");



        CompletableFuture<String> future = responseMap.get(correlationId);
        if (future != null) {
            future.complete(translatedText);
        }
    }

}


