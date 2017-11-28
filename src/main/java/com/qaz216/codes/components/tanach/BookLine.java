package com.qaz216.codes.components.tanach;

import org.apache.log4j.Logger;

public class BookLine {
	private static Logger log = Logger.getLogger(BookLine.class);	

	private String _lineText = null;
	private String _bookCode = null;
	private String _chapter = null;
	private String _verse = null;
	
	public BookLine(String rawLine) {
		this._lineText = rawLine.substring(12, rawLine.length());
		
		String verseText = rawLine.substring(0, 12);
		String[] verseElem = verseText.split(" ");
		this._bookCode = verseElem[0];
		
		String verseInfo = verseElem[1];
		
		String[] chapElem = verseInfo.split(":");
		this._chapter = chapElem[0];
		this._verse = chapElem[1];
		//log.debug("verse: "+this._verse);
	}
	
	public String getLineText() {
		return this._lineText;
	}
	
	public String toString() {
		return this._chapter+"-"+this._verse+" "+this._lineText;
	}

}
