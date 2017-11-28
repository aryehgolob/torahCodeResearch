package com.qaz216.codes.components.tanach;

import org.apache.log4j.Logger;

public class Book {
	private static Logger log = Logger.getLogger(Book.class);	
	
	public static final String GENESIS_NAME = "Genesis";
	public static final String EXODUS_NAME = "Exodus";
	public static final String LEVITICUS_NAME = "Leviticus";
	public static final String NUMBERS_NAME = "Numbers";
	public static final String DEUTERONOMY_NAME = "Deuteronomy";

	public static final String GENESIS_FILE_NAME = "Genesis.txt";
	public static final String EXODUS_FILE_NAME = "Exodus.txt";
	public static final String LEVITICUS_FILE_NAME = "Leviticus.txt";
	public static final String NUMBERS_FILE_NAME = "Numbers.txt";
	public static final String DEUTERONOMY_FILE_NAME = "Deuteronomy.txt";

	public static final String GENESIS_CODE = "GEN";
	public static final String EXODUS_CODE = "EXO";
	public static final String LEVITICUS_CODE = "LEV";
	public static final String NUMBERS_CODE = "NUM";
	public static final String DEUTERONOMY_CODE = "DEU";
	
	private String _name = null;
	private String _code = null;
	private String _fileName = null;

	public Book(String name, String code, String fileName) {
		this._name = name;
		this._code = code;
		this._fileName = fileName;
	}
	
	public void addLine(BookLine line) {
	}

	public String getName() {
		return this._name;
	}

	public String getCode() {
		return this._code;
	}

	public String getFileName() {
		return this._fileName;
	}

	public void setName(String name) {
		this._name = name;
	}

	public void setCode(String code) {
		this._code = code;
	}

	public void setFileName(String fileName) {
		this._fileName = fileName;
	}


}
