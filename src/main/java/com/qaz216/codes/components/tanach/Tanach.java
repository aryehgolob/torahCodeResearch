package com.qaz216.codes.components.tanach;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.qaz216.codes.components.letters.Letter;
import com.qaz216.codes.components.letters.LettersXml;
import com.qaz216.codes.components.letters.SkipCharactersXml;
import com.qaz216.codes.measures.FrequencyMeasure;

public class Tanach {
	private static Logger log = Logger.getLogger(Tanach.class);	
	
	private String _dir = null;
	
	private List<BookLine> _lineList = new ArrayList<BookLine>();
	
	// Letter list intended for initial scan.  We iterate through this and 
	// then once we hit a target letter we begin a sub-scan to attempt to find 
	// and ELS match
	private List<Letter> _letterList = new ArrayList<Letter>();

	// This data structure is paried w/ _letterList and is an exact clone.
	// Once we hit a target letter this data structure is used for the subscan.

	private List<Letter> _scanList = null;
	// book -> lines
	private Map<String, List<BookLine>> _bookLineMap = new HashMap<String, List<BookLine>>();
	private Map<String, Book> _bookMap = new HashMap<String, Book>();
	
	private Map<String, Integer> _letterCountMap = new HashMap<String, Integer>();

	private LettersXml _lettersXml = null;
	private SkipCharactersXml _skipCharXml = null;

	public Tanach(String dir) {
		this._dir = dir;
	}
	
	public List<Letter> getLetterList() {
		return this._letterList;
	}

	public Tanach(String dir, LettersXml letters) {
		this._dir = dir;
		this._lettersXml = letters;
	}

	public Tanach(String dir, LettersXml letters, SkipCharactersXml skipCharXml) {
		this._dir = dir;
		this._lettersXml = letters;
		this._skipCharXml = skipCharXml;
	}

	private static final List<Book> bookList = new ArrayList<Book>();
	private static final Map<String, Book> bookMap = new HashMap<String, Book>();
	static {
		Book genesisBook = new Book(Book.GENESIS_NAME, Book.GENESIS_CODE, Book.GENESIS_FILE_NAME);
		Book exodusBook = new Book(Book.EXODUS_NAME, Book.EXODUS_CODE, Book.EXODUS_FILE_NAME);
		Book leviticusBook = new Book(Book.LEVITICUS_NAME, Book.LEVITICUS_CODE, Book.LEVITICUS_FILE_NAME);
		Book numbersBook = new Book(Book.NUMBERS_NAME, Book.NUMBERS_CODE, Book.NUMBERS_FILE_NAME);
		Book deuteronomyBook = new Book(Book.DEUTERONOMY_NAME, Book.DEUTERONOMY_FILE_NAME, Book.DEUTERONOMY_FILE_NAME);

		bookList.add(genesisBook);
		bookList.add(exodusBook);
		bookList.add(leviticusBook);
		bookList.add(numbersBook);
		bookList.add(deuteronomyBook);
		
		bookMap.put(Book.GENESIS_NAME, genesisBook);
		bookMap.put(Book.EXODUS_NAME, exodusBook);
		bookMap.put(Book.LEVITICUS_NAME, leviticusBook);
		bookMap.put(Book.NUMBERS_NAME, numbersBook);
		bookMap.put(Book.DEUTERONOMY_NAME, numbersBook);
	}
	
	public static Book getBook(String name) {
		return bookMap.get(name);
	}
	
	public void initializeLetterProperties() {
		List<Integer> numList = this._lettersXml.getUniqueLetterToNumberList();
		for(Integer num : numList) {
			//log.debug("this num: " + num);
			int counter = 1;
			boolean haveFoundNumber = false;
			int letterNumber = num.intValue();
			for(Letter letter : this._letterList) {
				int thisLetterNumber = letter.getNumInt();
				//log.debug("this letter number: "+thisLetterNumber);
    		
				if(thisLetterNumber == letterNumber) {
					haveFoundNumber = true;
					log.debug("thisLetterNumber: "+thisLetterNumber+" first: "+counter+" ref: "+letter.getVerseId());
				}
    		
				if(haveFoundNumber) {
					//log.debug("found match breaking");
					break;
				}
    		
				counter++;
			}
		}

		/*
	   	int letterNumber = 200;
    	boolean haveFoundNumber = false;
    	int counter = 1;
    	for(Letter letter : this._letterList) {
    		int thisLetterNumber = letter.getNumInt();
    		//log.debug("this letter number: "+thisLetterNumber);
    		
    		if(thisLetterNumber == letterNumber) {
    			haveFoundNumber = true;
    			log.debug("thisLetterNumber: "+thisLetterNumber);
    			log.debug("letterNumber: "+letterNumber);
    			log.debug("first encounter: "+counter);
    		}
    		
    		if(haveFoundNumber) {
    			log.debug("found match breaking");
    			break;
    		}
    		
    		counter++;
    	}
    	*/

	}

	public void buildLetterMatrix() {
		int count = 0;
		boolean freeze = false;
		for(Book book : bookList) {
			String name = book.getName();
			String code = book.getCode();
			String fileName = this.getFullyQualifiedFileName(book.getFileName());
			List<BookLine> bookLineList = this._bookLineMap.get(name);
			if(bookLineList == null) {
				bookLineList = new ArrayList<BookLine>();
				this._bookLineMap.put(name, bookLineList);
			}
			try {
				FileInputStream fis = new FileInputStream(fileName);
				DataInputStream dis = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(new InputStreamReader(dis));
				String rawLine;
				while((rawLine=br.readLine()) != null) {
					rawLine = rawLine.trim();
					BookLine line = new BookLine(rawLine);
					log.debug("line: "+line);
					book.addLine(line);
					bookLineList.add(line);
					this._lineList.add(line);
					
					String lineText = line.getLineText();
					char[] lineTextCharArray = lineText.toCharArray();
					for(char thisChar : lineTextCharArray) {
						if(freeze) {
							break;
						}
						if(this._skipCharXml.skip(thisChar)) {
							// we can safely skip this character
							//log.debug("skip thisChar: "+thisChar);
							continue;
						}
						
						if(this._lettersXml.isValidCharacter(thisChar)) {
							//log.debug("valid thisChar: "+thisChar+" book: "+book.getName());
							Letter letter = this._lettersXml.getLetter(thisChar);
							String numValue = letter.getNumValue();
							Letter uniqueLetter = (Letter) letter.clone();
							uniqueLetter.setVerseId(line.toString());
							
							Integer letterCount = this._letterCountMap.get(numValue);
							if(letterCount == null) {
								this._letterCountMap.put(numValue, new Integer(1));
							}
							else {
								int countValue = letterCount.intValue();
								countValue++;
								this._letterCountMap.put(numValue, new Integer(countValue));
							}
							
							//log.debug("num value: "+numValue+" this char: "+thisChar+" line text: "+line.toString());
							this._letterList.add(uniqueLetter);
							count++;
						}
						else {
							log.error("\n");
							log.error("this character is undefined: '"+thisChar+"' - Book Name: "+name);
							log.error("line: "+line.toString());
							log.error("\n");
						}
					}
					
					
					
					//log.debug("line: "+lineText);
					if(freeze) {
						break;
					}
				}
				
		
				// close streams
				br.close();
				dis.close();
				fis.close();
			}
			catch(IOException e) {
				log.warn("could not open file: "+book.toString()+" "+e.getMessage());
			}
			
			
			// cloning letter list (target scan list)
			this._scanList = new ArrayList<Letter>(this._letterList.size());
			for(Letter letter : this._letterList) {
				this._scanList.add((Letter) letter.clone());
			}
		}

		/*
		Iterator<String> countIter = this._letterCountMap.keySet().iterator();
		int totalCount = 0;
		while(countIter.hasNext()) {
			String letter = countIter.next();
			Integer letterCount = this._letterCountMap.get(letter);
			log.debug("\tletter: "+letter);
			log.debug("\tletterCount: "+letterCount);
			int letterCountInt = letterCount.intValue();
			totalCount += letterCountInt;
		}
		log.debug("totalCount "+totalCount);
		*/
		log.debug("count "+count);
	}

	public void intitialize() {
		// first we get all the lines with their respective books
		this.initializeLines();
		
		// then we build global book character string (array of characters for that book) 
		// so that we can build ELS matrix.
		//this.buildGlobalCharacterString();
	}
	
	/*
	public void buildGlobalCharacterString() {
	//private List<BookLine> _lineList = new ArrayList<BookLine>();
		LettersXml letters = CodeWrapper.getLettersXml();

	public Tanach(String dir, LettersXml letters, SkipCharactersXml skipCharXml) {
		this._dir = dir;
		this._lettersXml = letters;
		this._skipCharXml = skipCharXml;
	}

	private static final List<Book> bookList = new ArrayList<Book>();
	private static final Map<String, Book> bookMap = new HashMap<String, Book>();
	static {
		Book genesisBook = new Book(Book.GENESIS_NAME, Book.GENESIS_CODE, Book.GENESIS_FILE_NAME);
		Book exodusBook = new Book(Book.EXODUS_NAME, Book.EXODUS_CODE, Book.EXODUS_FILE_NAME);
		Book leviticusBook = new Book(Book.LEVITICUS_NAME, Book.LEVITICUS_CODE, Book.LEVITICUS_FILE_NAME);
		Book numbersBook = new Book(Book.NUMBERS_NAME, Book.NUMBERS_CODE, Book.NUMBERS_FILE_NAME);
		Book deuteronomyBook = new Book(Book.DEUTERONOMY_NAME, Book.DEUTERONOMY_FILE_NAME, Book.DEUTERONOMY_FILE_NAME);

		bookList.add(genesisBook);
		bookList.add(exodusBook);
		bookList.add(leviticusBook);
		bookList.add(numbersBook);
		bookList.add(deuteronomyBook);
		
		bookMap.put(Book.GENESIS_NAME, genesisBook);
		bookMap.put(Book.EXODUS_NAME, exodusBook);
		bookMap.put(Book.LEVITICUS_NAME, leviticusBook);
		bookMap.put(Book.NUMBERS_NAME, numbersBook);
		bookMap.put(Book.DEUTERONOMY_NAME, numbersBook);
	}
	
	public static Book getBook(String name) {
		return bookMap.get(name);
	}
	
	public void intitialize() {
		// first we get all the lines with their respective books
		this.initializeLines();
		
		// then we build global book character string (array of characters for that book) 
		// so that we can build ELS matrix.
		//this.buildGlobalCharacterString();
	}
	
	/*
	public void buildGlobalCharacterString() {
	//private List<BookLine> _lineList = new ArrayList<BookLine>();
		LettersXml letters = CodeWrapper.getLettersXml();
		
		for(BookLine line : this._lineList) {
			//System.out.println("line: "+line);
			String lineText = line.getLineText();
			//log.debug("lineText: "+lineText);
			
			char[] chars = lineText.toCharArray();
			for(char character : chars) {
				// skip empty characters
				if(character == ' ' || 
				   character == '.' || 
				   character == ':' || 
				   character == '-') {
					//log.debug("char: "+character);
					continue;
				}
				
				//log.debug("\tcharacter: "+character);
				
				boolean isValidChar = letters.isValidCharacter(character);
			}

		}

		
	}
	*/

	public void applyFrequencyMeasure(List<FrequencyMeasure> measureList, int lowerBound, int upperBound) {
		for(FrequencyMeasure measure : measureList) {
			int number = measure.getNumber();
			log.debug("number: "+number);
		}
	}

	public void applyFrequencyMeasure2(List<FrequencyMeasure> measureList, int lowerBound, int upperBound) {
		int measureListSize = measureList.size();
		int letterListSize = this._letterList.size();
		int currentMeasureIndex = 0;
		FrequencyMeasure measure = measureList.get(currentMeasureIndex);
		int measureNum = measure.getNumber();
		int currentCount = lowerBound;
		for(int i = 0; i < this._letterList.size(); i++) {
			Letter letter = this._letterList.get(i);
			int letterNum = letter.getNumInt();
			if(measureNum == letterNum) {
				//log.debug("letter num: "+letterNum+" count: "+i);
				int nextIndex = i + currentCount;
				while(nextIndex <= letterListSize) {
					Letter nextLetter = this._scanList.get(nextIndex);
					int nextLetterNum = nextLetter.getNumInt();
					if(measureNum == nextLetterNum && currentMeasureIndex < measureListSize) {
						currentMeasureIndex++;
						FrequencyMeasure nextMeasure = measureList.get(currentMeasureIndex);
						measureNum = nextMeasure.getNumber();
					}
				}
				/*
				if(nextIndex < measureListSize) {
					Letter nextLetter = this._scanList.get(nextIndex);
					int nextLetterNum = nextLetter.getNumInt();
					log.debug("\tnext num: "+nextLetterNum+" index = "+nextIndex);
				}
				else {
					if(currentCount <= upperBound) {
					}
				}
				*/
			}
		}

		/*
		for(Letter letter : this._letterList) {
			int letterVal = letter.getNumInt();
			log.debug(letterVal);
		}
		*/
		/*
		for(FrequencyMeasure measure : measureList) {
			int number = measure.getNumber();
			log.debug("number: "+number);
		}
		*/
		
	}

	public void initializeLines() {
		for(Book book : bookList) {
			String name = book.getName();
			String code = book.getCode();
			String fileName = this.getFullyQualifiedFileName(book.getFileName());
			List<BookLine> bookLineList = this._bookLineMap.get(name);
			if(bookLineList == null) {
				bookLineList = new ArrayList<BookLine>();
				this._bookLineMap.put(name, bookLineList);
			}
			try {
				FileInputStream fis = new FileInputStream(fileName);
				DataInputStream dis = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(new InputStreamReader(dis));
		
				String rawLine;
				while((rawLine=br.readLine()) != null) {
					rawLine = rawLine.trim();
					BookLine line = new BookLine(rawLine);
					//log.debug("line: "+line);
					book.addLine(line);
					bookLineList.add(line);
					this._lineList.add(line);
					
					String lineText = line.getLineText();
					log.debug("line: "+line);
				}
		
				// close streams
				br.close();
				dis.close();
				fis.close();
			}
			catch(IOException e) {
				log.warn("could not open file: "+book.toString()+" "+e.getMessage());
			}
		}
	}

	public static String[] getBibleCharacters(String bookName) {
		Book deutBook = getBook(Book.DEUTERONOMY_NAME);
		return null;
	}

	public void produceBasicMatrix(String book, int skipInterval, int verseCount, char startLetter) {
		List<BookLine> lineBufferList = this._bookLineMap.get(book);
		if(lineBufferList == null) {
			log.error("No book for: "+book);
			return;
		}
		
		String[] bibleCharacters = getBibleCharacters(Book.GENESIS_NAME);
		String[][] matrix = new String[skipInterval][verseCount];
		
		
		
		boolean foundStartChar = false;
		boolean record = false;
		int letterCount = 0;
		List<Character> charList = new ArrayList<Character>();

		for(BookLine line : lineBufferList) {
			//log.debug("line: "+line);
			char[] chars = line.getLineText().toCharArray(); 	
			for(char character : chars) {
				if(character == ' ' || character == '.' || character == ':' || character == '-') {
					//log.debug("char: "+character);
					continue;
				}
				
				//log.debug("char: "+character);
				Character thisChar = new Character(character);

				if(!foundStartChar && character == startLetter) {
					foundStartChar = true;
					record = true;
					charList.add(thisChar);
				}
				else if(!foundStartChar) {
					continue;
				}
				else {
					letterCount++;
					if(letterCount == skipInterval) {
						charList.add(thisChar);
						letterCount = 0;
					}
				}
			}
		}
		
		for(int i = 0; i < verseCount; i++) {
			Character character = charList.get(i);
			log.debug(book+" char: "+character.toString());
		}
	}
	
	public void skip(String book, int skipInterval, int verseCount, char startLetter) {
		List<BookLine> lineBufferList = this._bookLineMap.get(book);
		if(lineBufferList == null) {
			log.error("No book for: "+book);
			return;
		}
		
		
		
		boolean foundStartChar = false;
		int letterCount = 0;
		List<Character> charList = new ArrayList<Character>();

		for(BookLine line : lineBufferList) {
			char[] chars = line.getLineText().toCharArray(); 	
			for(char character : chars) {
				if(character == ' ' || character == '.' || character == ':' || character == '-') {
					//log.debug("char: "+character);
					continue;
				}
				
				//log.debug("char: "+character);
				Character thisChar = new Character(character);

				if(!foundStartChar && character == startLetter) {
					foundStartChar = true;
					charList.add(thisChar);
				}
				else if(!foundStartChar) {
					continue;
				}
				else {
					letterCount++;
					if(letterCount == skipInterval) {
						charList.add(thisChar);
						letterCount = 0;
					}
				}
			}
		}
		
		for(int i = 0; i < verseCount; i++) {
			Character character = charList.get(i);
			log.debug(book+" char: "+character.toString());
		}
	}
	
	public void scan() {
		String bookName = null;
		for(Book book : bookList) {
			String name = book.getName();
			String code = book.getCode();
			String fileName = this.getFullyQualifiedFileName(book.getFileName());
			List<BookLine> bookLineList = this._bookLineMap.get(name);
			if(bookLineList == null) {
				bookLineList = new ArrayList<BookLine>();
				this._bookLineMap.put(name, bookLineList);
			}
			try {
				FileInputStream fis = new FileInputStream(fileName);
				DataInputStream dis = new DataInputStream(fis);
				BufferedReader br = new BufferedReader(new InputStreamReader(dis));
		
				String rawLine;
				while((rawLine=br.readLine()) != null) {
					rawLine = rawLine.trim();
					BookLine line = new BookLine(rawLine);
					//log.debug("line: "+line);
					book.addLine(line);
					bookLineList.add(line);
					this._lineList.add(line);

					String lineText = line.getLineText();
					char[] charArray = lineText.toCharArray();
					for(char thisChar : charArray) {
						if(this._lettersXml.isValidCharacter(thisChar)) {
							Letter letter = this._lettersXml.getLetter(thisChar);
							//log.debug("thisChar: "+letter.getCharTrans());
						}
					}
				}
		
				// close streams
				br.close();
				dis.close();
				fis.close();
			}
			catch(IOException e) {
				log.warn("could not open file: "+book.toString()+" "+e.getMessage());
			}
		}
	}
	
	public String getFullyQualifiedFileName(String name) {
		return this._dir + "/" + name;
	}
}
