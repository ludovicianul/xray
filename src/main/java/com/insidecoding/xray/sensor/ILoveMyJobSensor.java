package com.insidecoding.xray.sensor;

import java.util.List;
import java.util.Set;

import com.insidecoding.xray.model.Commit;
import com.insidecoding.xray.report.Report;

public class ILoveMyJobSensor extends XRayTeamSensor {

	@Override
	public Report analyse(List<Commit> commits, Set<String> authors) {
		return null;
	}

}
