package com.insidecoding.xray.sensor;

import java.util.List;
import java.util.Set;

import com.insidecoding.xray.model.Commit;
import com.insidecoding.xray.report.Report;

public interface XRaySensor {

	Report analyse(List<Commit> commits, Set<String> authors);
}
