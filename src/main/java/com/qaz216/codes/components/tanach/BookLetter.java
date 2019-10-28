package com.qaz216.codes.components.tanach;

import org.apache.log4j.Logger;

import com.qaz216.codes.components.letters.Letter;

public class BookLetter {
	private static Logger log = Logger.getLogger(BookLetter.class);	
	
	private Letter letter = null;
	
	public void setLetter(Letter letter) {
		this.letter = letter;
	}

}
