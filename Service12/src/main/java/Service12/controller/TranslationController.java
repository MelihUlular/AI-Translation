package Service12.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import Service12.model.dto.TranslationRequest;
import Service12.model.dto.TranslationResponse;
import Service12.service.TranslationProducer;

import org.json.JSONObject;

@RestController
@RequestMapping("/api")
@Validated
public class TranslationController {

    @Autowired
    private TranslationProducer producer;

    @PostMapping("/translate")
    public TranslationResponse translate(@RequestBody @Valid TranslationRequest request) {
        JSONObject json = new JSONObject();
        json.put("sourceLang", request.getSourceLang());
        json.put("targetLang", request.getTargetLang());
        json.put("text", request.getText());

        String translated = producer.sendTranslationRequest(json.toString());
        return new TranslationResponse(translated);
    }
}
