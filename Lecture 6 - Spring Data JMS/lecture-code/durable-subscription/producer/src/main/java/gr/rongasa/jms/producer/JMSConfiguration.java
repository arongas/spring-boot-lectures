package gr.rongasa.jms.producer;

import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring boot configuration class used to create a bean specifying the jms
 * listener configuration.
 *
 * @author arongas
 * @see DefaultJmsListenerContainerFactory
 */
@EnableScheduling
@Configuration
public class JMSConfiguration {


}
