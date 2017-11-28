package com.qaz216.codes.measures;

import org.apache.log4j.Logger;

public class FrequencyMeasure {
	private static Logger log = Logger.getLogger(FrequencyMeasure.class);
	
	private int _number = -1;

	public FrequencyMeasure(int number) {
		this._number = number;
	}
	
	public int getNumber() {
		return this._number;
	}

}
