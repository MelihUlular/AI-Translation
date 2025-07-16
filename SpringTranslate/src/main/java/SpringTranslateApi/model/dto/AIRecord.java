package SpringTranslateApi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AIRecord {
    private String source;
    private String target;
    private String input;
    private String output;
    
   
}