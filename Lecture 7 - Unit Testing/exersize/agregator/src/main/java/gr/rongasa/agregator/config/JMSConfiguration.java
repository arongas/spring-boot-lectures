package gr.rongasa.agregator.config;

import gr.rongasa.agregator.domain.dto.MetricDTO;
import gr.rongasa.agregator.domain.dto.SensorDTO;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
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



    @Bean("metricsListenerFactory")
    public DefaultJmsListenerContainerFactory notificationEventContainerFactory(
            DefaultJmsListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }

}
