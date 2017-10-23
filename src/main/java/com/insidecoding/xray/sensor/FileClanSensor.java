package com.insidecoding.xray.sensor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.insidecoding.xray.model.Commit;
import com.insidecoding.xray.model.XRayFile;
import com.insidecoding.xray.report.FileClanReport;
import com.insidecoding.xray.report.Report;

public class FileClanSensor extends XRayFileSensor {

	@Override
	public Report analyse(List<Commit> commits, String root) {
		List<Commit> matchingCommits = commits.stream()
				.filter(commit -> commit.files().stream().anyMatch(file -> file.fullName().endsWith(root)))
				.collect(Collectors.toList());

		List<XRayFile> files = matchingCommits.stream().flatMap(commit -> commit.files().stream())
				.collect(Collectors.toList());

		Map<XRayFile, Long> clan = files.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		return new FileClanReport(clan);
	}

}
