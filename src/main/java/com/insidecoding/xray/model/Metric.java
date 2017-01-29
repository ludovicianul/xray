package com.insidecoding.xray.model;

public class Metric {

	private String author;
	private String name;
	private long value;

	public Metric(String author, String name, long value) {
		this.author = author;
		this.name = name;
		this.value = value;
	}

	public String author() {
		return this.author;
	}

	public String name() {
		return this.name;
	}

	public long value() {
		return this.value;
	}

	@Override
	public String toString() {
		return "Metric [author=" + author + ", name=" + name + ", value=" + value + "]";
	}

}
