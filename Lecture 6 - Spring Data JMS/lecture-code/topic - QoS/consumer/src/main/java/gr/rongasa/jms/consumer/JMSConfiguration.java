package gr.rongasa.jms.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;

import javax.jms.ConnectionFactory;

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

    @Value("${producer.jms.redelivery.backOffMultiplier}")
    double backOffMultiplier;

    @Value("${producer.jms.redelivery.useExponentialBackOff}")
    boolean useExponentialBackOff;

    @Value("${producer.jms.redelivery.maximumRedeliveries}")
    int maximumRedeliveries;


    @Bean
    public InitializingBean connectionFactory(ConnectionFactory connectionFactory) {
        return () ->
        {
            if (connectionFactory instanceof ActiveMQConnectionFactory) {
                configureRedeliveryPolicy((ActiveMQConnectionFactory) connectionFactory);
            }else if (connectionFactory instanceof SingleConnectionFactory
                    && ((SingleConnectionFactory) connectionFactory).getTargetConnectionFactory() instanceof ActiveMQConnectionFactory){
                configureRedeliveryPolicy((ActiveMQConnectionFactory) ((SingleConnectionFactory) connectionFactory).getTargetConnectionFactory());
            }
        };
    }

    private void configureRedeliveryPolicy(ActiveMQConnectionFactory connectionFactory) {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setUseExponentialBackOff(useExponentialBackOff);
        redeliveryPolicy.setBackOffMultiplier(backOffMultiplier);
        redeliveryPolicy.setMaximumRedeliveries(maximumRedeliveries);
        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);
    }
}
