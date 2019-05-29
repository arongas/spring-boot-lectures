package gr.rongasa.sensor.config;

import gr.rongasa.sensor.domain.MetricDTO;
import gr.rongasa.sensor.domain.SensorDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring boot configuration class used to create a bean specifying the jms
 * listener configuration.
 *
 * @author arongas
 * @see DefaultJmsListenerContainerFactory
 */
@Configuration
@EnableJms
@EnableScheduling
public class JMSConfiguration {

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        Map<String,Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put(MetricDTO.class.getSimpleName(), MetricDTO.class);
        typeIdMappings.put(SensorDTO.class.getSimpleName(), SensorDTO.class);
        converter.setTypeIdMappings(typeIdMappings);
        return converter;
    }


}
