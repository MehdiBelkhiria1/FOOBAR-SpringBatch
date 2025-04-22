package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;

@Configuration
@EnableKafka
public class KafkaConfig {
	
	
	  private final KafkaProperties props;
	  
	  public KafkaConfig(KafkaProperties props) {
	        this.props = props;
	  }
	
	  @Bean
	  public ProducerFactory<String,String> producerFactory() {
	    Map<String,Object> cfg = new HashMap<>(props.buildConsumerProperties());
	    cfg.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	    cfg.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	    return new DefaultKafkaProducerFactory<>(cfg);
	  }

	  @Bean
	  public KafkaTemplate<String,String> kafkaTemplate() {
	    return new KafkaTemplate<>(producerFactory());
	  }

	  @Bean
	  public ConsumerFactory<String,String> consumerFactory() {
	    Map<String,Object> cfg = new HashMap<>(props.buildConsumerProperties());
	    cfg.put(ConsumerConfig.GROUP_ID_CONFIG, "batch-group");
	    cfg.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	    cfg.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	    return new DefaultKafkaConsumerFactory<>(cfg);
	  }

	  @Bean
	  public ConcurrentKafkaListenerContainerFactory<String,String> kafkaListenerContainerFactory() {
	    ConcurrentKafkaListenerContainerFactory<String,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory());
	    
	    // disable auto-commit and switch to MANUAL ack(comminting only after batch jobs successful executions)
	    factory.getContainerProperties().setAckMode(AckMode.MANUAL);
	    
	    // rise number of threads for consumer listener to optimize performance
	    factory.setConcurrency(8);
	    
	    return factory;
	  }
	  
}
