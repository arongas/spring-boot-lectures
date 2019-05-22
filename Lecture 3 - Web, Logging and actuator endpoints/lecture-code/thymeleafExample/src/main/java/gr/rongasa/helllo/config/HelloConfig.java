package gr.rongasa.helllo.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "hello")
@Data
@NoArgsConstructor
public class HelloConfig {
    private String name="World";
    private Config config=new Config();

    @Data
    @NoArgsConstructor
    public static class Config {
        private boolean track=true;
        private List<String> list=new ArrayList<>();
    }
}
