package com.qaz216.codes.components.letters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.qaz216.codes.CodeWrapper;

public class LettersXml {
	private static Logger log = Logger.getLogger(LettersXml.class);	
	
	private Map<String, Letter> letterMap = new HashMap<String, Letter>();

	private String fileName = null;
	private Set<String> _charSet = new HashSet<String>();

	public LettersXml(String fileName) {
		this.fileName = fileName;
		this.parse();
	}
	
	public boolean isValidCharacter(String character) {
		return this._charSet.contains(character);
	}

	public boolean isValidCharacter(char thisChar) {
		if(thisChar == ' ') {
			log.debug("hit empty character");
			return false;
		}
		else {
			return this._charSet.contains(String.valueOf(thisChar));
		}
	}
	
	public List<Integer> getUniqueLetterToNumberList() {
		Set<Integer> set = new HashSet<Integer>();
		List<Integer> returnList = new ArrayList<Integer>();
		
		Iterator<String> letterIter = this.letterMap.keySet().iterator();
		while(letterIter.hasNext()) {
			String letterString = letterIter.next();
			Letter letter = this.letterMap.get(letterString);
			int letterNumber = letter.getNumInt();
			Integer num = new Integer(letterNumber);
			if(!set.contains(num)) {
				set.add(num);
				returnList.add(num);
			}
		}
		
		
		return returnList;
	}
	
	public int[] getLetterToNumberArray() {
		int[] returnArray = new int[this.letterMap.size()];
		Iterator<String> letterIter = this.letterMap.keySet().iterator();
		int x = 0;
		while(letterIter.hasNext()) {
			String letterString = letterIter.next();
			Letter letter = this.letterMap.get(letterString);
			int letterNumber = letter.getNumInt();
			returnArray[x] = letterNumber;
			x++;
		}
		return returnArray;
	}
	
	public Letter getLetter(char thisChar) {
		//log.debug("aryeh this char: "+thisChar);
		if(this.letterMap == null) {
			log.error("letter map is null ...");
			return null;
		}
		
		String charString = String.valueOf(thisChar).trim();
		Letter letter = this.letterMap.get(charString);
		if(letter == null) {
			//log.error("letter is null ...");
			return null;

		}
		return this.letterMap.get(String.valueOf(thisChar));
	}

	private void parse() {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(this.fileName);
		
		try {
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<Element> lettersElemList = rootNode.getChildren("letter");
			if(lettersElemList == null) {
				log.error("lettersElemList is null, cannot continue");
				return;
			}
			
			for(Element letterElem : lettersElemList) {
				String name = letterElem.getAttributeValue("name").trim();
				String numValue = letterElem.getChild("numerical_value").getAttributeValue("value").trim();
				String charTrans = letterElem.getChild("character_translation").getAttributeValue("value").trim();
				this._charSet.add(charTrans);
				Letter letter = new Letter(name, numValue, charTrans);
				this.letterMap.put(charTrans, letter);
				//log.debug("golob name: "+name+" value: "+numValue+" char trans: "+charTrans);
						
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
