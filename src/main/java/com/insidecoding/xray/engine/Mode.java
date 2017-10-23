package com.insidecoding.xray.engine;

import com.insidecoding.xray.sensor.FileClanSensor;
import com.insidecoding.xray.sensor.XRaySensor;

public enum Mode {

	CLAN("clan", FileClanSensor.class);

	private String text;
	private Class<? extends XRaySensor> sensor;

	Mode(String mode, Class<? extends XRaySensor> sensor) {
		this.text = mode;
		this.sensor = sensor;
	}

	public static Mode fromString(String input) {
		for (Mode mode : values()) {
			if (mode.text.equalsIgnoreCase(input)) {
				return mode;
			}
		}
		throw new IllegalStateException("'" + input + "' mode not available");
	}

	public Class<? extends XRaySensor> sensor() {
		return this.sensor;
	}
}
