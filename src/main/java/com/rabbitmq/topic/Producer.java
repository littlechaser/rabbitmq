package com.rabbitmq.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.commons.ExchangeType;

public class Producer {

	private static final String QUEUE_NAME = "BOYS";
	private static final String QUEUE_NAME_2 = "GRILS";
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
			channel.queueDeclare(QUEUE_NAME, false, false, true, null);
			channel.queueBind(QUEUE_NAME, ExchangeType.TOPIC.getName(), "boys.*.*");
			channel.queueDeclare(QUEUE_NAME_2, false, false, true, null);
			channel.queueBind(QUEUE_NAME_2, ExchangeType.TOPIC.getName(), "*.*.girls");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	public static void send(String msg) throws IOException, TimeoutException{
		
		try {
			/*1.交换机名称；2.路由关键字即routing key；3；4.消息，字节数组格式*/
			channel.basicPublish(ExchangeType.TOPIC.getName(), "boys.like.girls", null, msg.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			channel.close();
			connection.close();
		}
	}
	
	
	public static void main(String[] args) {
		try {
			send("hello rabbitmq");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
}
