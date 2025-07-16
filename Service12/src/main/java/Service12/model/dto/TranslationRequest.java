package Service12.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranslationRequest {
    @NotBlank(message = "Source language cannot be null or empty")
    private String sourceLang;

    @NotBlank(message = "Target language cannot be null or empty")
    private String targetLang;

    @NotBlank(message = "Text cannot be null or empty")
    private String text;
}