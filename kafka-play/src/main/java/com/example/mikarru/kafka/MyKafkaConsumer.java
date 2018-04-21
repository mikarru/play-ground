package com.example.mikarru.kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class MyKafkaConsumer {
  private String topic;
  private KafkaConsumer<String, String> consumer;

  public MyKafkaConsumer(String topic) {
    this.topic = topic;
  }

  public void init() {
    consumer = createConsumer();
    consumer.subscribe(Arrays.asList(topic));
  }

  public void close() {
    if (consumer != null) {
      consumer.close();
      consumer = null;
    }
  }

  private void run(long runIntervalMs) {
    long start = System.currentTimeMillis();
    for (int i = 0; System.currentTimeMillis() - start < runIntervalMs; i++) {
      ConsumerRecords<String, String> records = consumer.poll(50L);
      for (ConsumerRecord<String, String> record : records) {
        String msg = "offset: " + record.offset() + ", key: " + record.key() + ", value: " + record.value();
        System.out.println(msg);
      }
      consumer.commitSync();
    }
  }

  private KafkaConsumer<String, String> createConsumer() {
    Properties props = new Properties();
     props.put("bootstrap.servers", ""); // SET BOOTSTRAP SERVERS
     props.put("group.id", "test_consumer");
     props.put("enable.auto.commit", "true");
     props.put("auto.commit.interval.ms", "100");
     props.put("session.timeout.ms", "30000");
     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
     return new KafkaConsumer<>(props);
  }

  public static void main(String[] args) throws Exception {
    String topic = args[0];
    MyKafkaConsumer consumer = new MyKafkaConsumer(topic);
    consumer.init();
    consumer.run(120L * 1000L);
    consumer.close();
  }
}
