package gr.rongasa.jms.consumer;

import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

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


}
