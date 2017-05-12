package com.rabbitmq.commons;

public enum ExchangeType {

	FANOUT("fanout", "fanout"),
	DIRECT("direct", "direct"),
	TOPIC("topic", "topic"),
	HEADERS("headers", "headers");
	
	private ExchangeType(String name, String type){
		this.name = name;
		this.type = type;
	}
	
	private String name;
	private String type;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
