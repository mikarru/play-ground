package com.example.mikarru.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class MyKafkaProducer {
  private String topic;
  private KafkaProducer<String, String> producer;

  public MyKafkaProducer(String topic) {
    this.topic = topic;
  }

  public void init() {
    producer = createProducer();
  }

  public void close() {
    if (producer != null) {
      producer.flush();
      producer.close();
      producer = null;
    }
  }

  public void run(long runIntervalMs) throws InterruptedException {
    long start = System.currentTimeMillis();
    for (int i = 0; System.currentTimeMillis() - start < runIntervalMs; i++) {
      String key = "" + i;
      String value = "" + i + " th record at " + System.currentTimeMillis();
      ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
      producer.send(record);
      producer.flush();
      System.out.println("Sent " + i + " th record");
      Thread.sleep(100L);
    }
  }

  private KafkaProducer<String, String> createProducer() {
    Properties props = new Properties();
    props.put("bootstrap.servers", ""); // SET BOOTSTRAP SERVERS
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    return new KafkaProducer<>(props);
  }

  public static void main(String[] args) throws Exception {
    String topic = args[0];
    MyKafkaProducer producer = new MyKafkaProducer(topic);
    producer.init();
    producer.run(120L * 1000L);
    producer.close();
  }
}
