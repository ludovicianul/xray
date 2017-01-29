package com.insidecoding.xray.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.insidecoding.xray.model.Metric;

@Component
public class RunawayReport implements Report {
	public static final String TITLE = "Runaway Syndrome";
	public static final String UNIT_TESTS = "unit tests";
	public static final String COMMIT_AGE = "commit age";
	public static final String TOTAL_COMMITS = "total commits";
	public static final String INTEGRATION_TESTS = "integ tests";
	public static final String MERGES = "merges";

	private static final String PLUS = "+";
	private static final String BAR = "|";
	private static final int HEADER_LENGTH = 15;
	private static final int DATA_LENGTH = HEADER_LENGTH + 3;

	private static final String LINE_DATA_DELIMITER = String.join("", Collections.nCopies(DATA_LENGTH, "-")) + PLUS;
	private static final String LINE_START_DELIMITER = PLUS + String.join("", Collections.nCopies(DATA_LENGTH - 2, "-"))
			+ PLUS;

	private static final String HEADER_FORMAT = " %-" + (HEADER_LENGTH + 1) + "s " + BAR;
	private static final String START_HEADER_FORMAT = BAR + " %-" + (HEADER_LENGTH - 1) + "s " + BAR;
	private static final String LINE_FORMAT = "%-" + DATA_LENGTH + "s";

	private Map<String, Map<String, Long>> data = new HashMap<>();

	public RunawayReport collect(String author, Map<String, Long> moreData) {
		this.data.get(author).putAll(moreData);
		return this;
	}

	public RunawayReport collect(String author, String dataKey, Long dataValue) {
		this.data.get(author).put(dataKey, dataValue);
		return this;
	}

	public RunawayReport author(String author) {
		this.data.put(author, new LinkedHashMap<>());
		return this;
	}

	@Override
	public String prettyFormat() {
		StringBuilder headerBuilder = new StringBuilder();
		StringBuilder lineBuilder = new StringBuilder();
		StringBuilder dataBuilder = new StringBuilder();
		StringBuilder finalBuilder = new StringBuilder();
		lineBuilder.append(LINE_START_DELIMITER);
		headerBuilder.append(String.format(START_HEADER_FORMAT, "Author"));

		this.createHeaders(headerBuilder, lineBuilder);
		this.appendData(dataBuilder);

		return finalBuilder.append(System.lineSeparator()).append(lineBuilder).append(System.lineSeparator())
				.append(headerBuilder).append(System.lineSeparator()).append(lineBuilder).append(System.lineSeparator())
				.append(dataBuilder).append(lineBuilder).toString();
	}

	@Override
	public String recommendations() {
		List<Metric> metrics = data.entrySet().stream()
				.map(entry -> entry.getValue().entrySet().stream()
						.map(data -> new Metric(entry.getKey(), data.getKey(), data.getValue()))
						.collect(Collectors.toList()))
				.collect(ArrayList::new, List::addAll, List::addAll);

		Map<String, List<Metric>> runaways = new HashMap<>();
		metrics.stream().collect(Collectors.groupingBy(Metric::name)).entrySet().forEach(entry -> {
			LongSummaryStatistics statistics = entry.getValue().stream()
					.collect(Collectors.summarizingLong(Metric::value));
			runaways.put(entry.getKey(),
					entry.getValue().stream().filter(metric -> metric.value() <= statistics.getAverage())
							.sorted((m1, m2) -> (int) (m1.value() - m2.value())).collect(Collectors.toList()));
		});

		return this.buildRunaways(runaways);
	}

	private String buildRunaways(Map<String, List<Metric>> runaways) {
		StringBuilder dataBuilder = new StringBuilder();
		StringBuilder lineBuilder = new StringBuilder();
		StringBuilder finalBuilder = new StringBuilder();
		int dataLength = this.getTotalAuthorsLength();
		String lineDataDelimiter = String.join("", Collections.nCopies(dataLength, "-")) + PLUS;
		String headerFormat = " %-" + (dataLength - 2) + "s " + BAR;

		lineBuilder.append(LINE_START_DELIMITER).append(lineDataDelimiter);
		dataBuilder.append(String.format(START_HEADER_FORMAT, "Area")).append(String.format(headerFormat, "Runaways"));
		dataBuilder.append(System.lineSeparator()).append(lineBuilder).append(System.lineSeparator());

		runaways.entrySet().forEach(entry -> {
			String row = entry.getValue().stream().map(data -> data.author() + " (" + data.value() + ")")
					.collect(Collectors.joining(", "));
			dataBuilder.append(String.format(START_HEADER_FORMAT, entry.getKey()))
					.append(String.format(headerFormat, row)).append(System.lineSeparator());

		});
		return finalBuilder.append(System.lineSeparator()).append(lineBuilder).append(System.lineSeparator())
				.append(dataBuilder).append(lineBuilder).toString();
	}

	private int getTotalAuthorsLength() {
		return this.data.keySet().stream().mapToInt(s -> s.length() + 5).sum();
	}

	private void appendData(StringBuilder dataBuilder) {
		data.entrySet().stream().forEach(item -> {
			dataBuilder.append(String.format(START_HEADER_FORMAT, shortName(item.getKey())))
					.append(item.getValue().entrySet().stream()
							.map(entry -> String.format(HEADER_FORMAT, String.valueOf(entry.getValue())))
							.collect(Collectors.joining()))
					.append(System.lineSeparator());
		});
	}

	private void createHeaders(StringBuilder headerBuilder, StringBuilder lineBuilder) {
		data.entrySet().stream().findFirst().ifPresent(item -> {
			headerBuilder.append(item.getValue().keySet().stream().map(header -> String.format(HEADER_FORMAT, header))
					.collect(Collectors.joining()));
			lineBuilder.append(item.getValue().keySet().stream()
					.map(header -> String.format(LINE_FORMAT, LINE_DATA_DELIMITER)).collect(Collectors.joining()));
		});
	}

	private static String shortName(String name) {
		String[] names = name.split(" ");
		String result = names[0];
		for (int i = 1; i < names.length; i++) {
			result += " " + names[i].substring(0, 1) + ".";
		}

		return result;
	}

	@Override
	public String csv() {
		throw new IllegalStateException("Not Implemented Yet");
	}

	@Override
	public String html() {
		throw new IllegalStateException("Not Implemented Yet");
	}

}
