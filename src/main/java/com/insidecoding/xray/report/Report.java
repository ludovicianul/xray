package com.insidecoding.xray.report;

public interface Report {

	String prettyFormat();

	String csv();

	String html();

	String recommendations();
}
