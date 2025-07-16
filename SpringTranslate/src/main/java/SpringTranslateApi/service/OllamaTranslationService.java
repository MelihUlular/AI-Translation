package SpringTranslateApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import SpringTranslateApi.exception.TranslationServiceException;
import SpringTranslateApi.model.dto.TranslationRequest;
import SpringTranslateApi.model.dto.TranslationResponse;
import SpringTranslateApi.model.dto.AIRecord;
import SpringTranslateApi.repository.AIRecordRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@Service
public class OllamaTranslationService {

    //private static final String OLLAMA_API_URL = "http://localhost:11434/api/generate";
    //private static final String MODEL = "llama3.2:latest";
	private static final Logger logger = LoggerFactory.getLogger(OllamaTranslationService.class);

    
    @Value("${ollama.api.url}")
    private String ollamaApiUrl;

    @Value("${ollama.model.name}")
    private String model;
    
    @Value("${prompt.template}")  
    private String promptTemplate;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private AIRecordRepository aiRecordRepository;
    

    public TranslationResponse translate(TranslationRequest request) {
    	String prompt = String.format(promptTemplate, 
                request.getSourceLang(), 
                request.getTargetLang(), 
                request.getText());
    	
        String translatedText = sendPostRequest(model, prompt);
       
        AIRecord rec = new AIRecord(request.getSourceLang(), request.getTargetLang(), request.getText(), translatedText);
        
        saveRecord(rec);
        
        return new TranslationResponse(translatedText);
    }
    
    private void saveRecord(AIRecord rec) {
    	System.out.println(rec.toString());
    	aiRecordRepository.insertRecord(rec);
    }

    private String sendPostRequest(String model, String prompt) {
    	 try {
             
             String jsonBody = "{ \"model\": \"" + model + "\", \"prompt\": \"" + prompt + "\", \"stream\": false }";

             
             HttpHeaders headers = new HttpHeaders();
             headers.setContentType(MediaType.APPLICATION_JSON);
             
            
             HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
             
             
             ResponseEntity<String> response = restTemplate.exchange(ollamaApiUrl, HttpMethod.POST, entity, String.class);
             
            
             String responseStr = response.getBody();
             String resultText = parseResponse(responseStr);
             
             return resultText.trim();
             
        } catch (Exception e) {
        	 logger.error("Failed to communicate with Ollama API: {}", e.getMessage());
             throw new TranslationServiceException("Failed to communicate with Ollama API", e);
        }
    }

	private String parseResponse(String responseStr) {
		try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode jsonNode = objectMapper.readTree(responseStr);
	        return jsonNode.path("response").asText();
	    } catch (Exception e) {
	        logger.error("Failed to parse translation response", e);
	        throw new TranslationServiceException("Error parsing the translation response from Ollama", e);
	    }
	}
}
