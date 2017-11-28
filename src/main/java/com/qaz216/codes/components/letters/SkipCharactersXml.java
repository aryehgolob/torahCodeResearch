package com.qaz216.codes.components.letters;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class SkipCharactersXml {
	private static Logger log = Logger.getLogger(SkipCharactersXml.class);	
	
	private String _fileName = null;
	private Set<String> _charSet = new HashSet<String>();
	
	public SkipCharactersXml(String fileName) {
		this._fileName = fileName;
		this.parse();
	}
	
	public boolean skip(String character) {
		return this._charSet.contains(character);
	}

	public boolean skip(char character) {
		if(character == ' ') {
			return true;
		}

		String charString =  String.valueOf(character);
		return this._charSet.contains(charString);
	}

	private void parse() {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(this._fileName);
		
		try {
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<Element> charElemList = rootNode.getChildren("character");
			if(charElemList != null) {
				for(Element elem : charElemList) {
					String charString = elem.getAttributeValue("name").trim();
					this._charSet.add(charString);
					//log.debug("char string: "+charString);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
