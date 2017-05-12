package com.rabbitmq.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.commons.ExchangeType;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Consumer {
	private static final String QUEUE_NAME = "BOYS";
	private static final String QUEUE_NAME_2 = "GRILS";
	private static final String QUEUE_NAME_3 = "LOVE";
	private static ConnectionFactory factory;
	private static Connection connection;
	private static Channel channel;
	static{
		try {
			factory = new ConnectionFactory();
			factory.setHost("localhost");
			factory.setPort(5672);
			factory.setVirtualHost("/");
			factory.setUsername("guest");
			factory.setPassword("guest");
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(ExchangeType.TOPIC.getName(), ExchangeType.TOPIC.getType());
			/*1.队列名称，2.是否可持久化，3.是否排他列队，4.是否自动删除（空闲时）*/
			channel.queueDeclare(QUEUE_NAME, false, false, true, null).getQueue();
			channel.queueBind(QUEUE_NAME, ExchangeType.TOPIC.getName(), "boys.*.*");
			channel.queueDeclare(QUEUE_NAME_2, false, false, true, null).getQueue();
			channel.queueBind(QUEUE_NAME_2, ExchangeType.TOPIC.getName(), "*.*.girls");
			channel.queueDeclare(QUEUE_NAME_3, false, false, true, null).getQueue();
			channel.queueBind(QUEUE_NAME_3, ExchangeType.TOPIC.getName(), "*.love.*");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	public static void receive() throws ShutdownSignalException, ConsumerCancelledException, InterruptedException, IOException{

		
		com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body);
				System.out.println("cousumer received message:" + msg);
			}
		};
		com.rabbitmq.client.Consumer consumer2 = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body);
				System.out.println("cousumer2 received message:" + msg);
			}
		};
		com.rabbitmq.client.Consumer consumer3 = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body);
				System.out.println("cousumer3 received message:" + msg);
			}
		};
		/*1.队列名称；2.是否自动发送ack；3.消费者*/
		channel.basicConsume(QUEUE_NAME, true, consumer);
		channel.basicConsume(QUEUE_NAME_2, true, consumer2);
		channel.basicConsume(QUEUE_NAME_3, true, consumer3);
	}
	
	public static void main(String[] args) throws IOException, TimeoutException {
		try {
			receive();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
