package com.insidecoding.xray.report;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.insidecoding.xray.model.XRayFile;

public class FileClanReport implements Report {

	private Map<XRayFile, Long> clan;

	public FileClanReport(Map<XRayFile, Long> files) {
		clan = files.entrySet().stream().sorted(Map.Entry.<XRayFile, Long>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	@Override
	public String prettyFormat() {
		return this.csv();
	}

	@Override
	public String csv() {
		StringBuilder builder = new StringBuilder();
		builder.append("File").append(",").append("Strength").append("\n");
		String data = clan.entrySet().stream().map(entry -> entry.getKey().fullName() + "," + entry.getValue())
				.collect(Collectors.joining("\n"));

		return builder.append(data).toString();
	}

	@Override
	public String html() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String recommendations() {
		// TODO Auto-generated method stub
		return null;
	}

}
