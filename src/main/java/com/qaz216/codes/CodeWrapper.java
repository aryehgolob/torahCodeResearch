package com.qaz216.codes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.qaz216.codes.components.letters.Letter;
import com.qaz216.codes.components.letters.LettersXml;
import com.qaz216.codes.components.letters.SkipCharactersXml;
import com.qaz216.codes.components.tanach.Book;
import com.qaz216.codes.components.tanach.Tanach;
import com.qaz216.codes.etc.FileUtil;
import com.qaz216.codes.measures.FrequencyMeasure;

public class CodeWrapper {
	private static Logger log = Logger.getLogger(CodeWrapper.class);	

	private static String CONFIG_FILE = "etc/code_wrapper.properties";
	
	// Initialize global letter properties
	private static String LETTERS_XML = "etc/letters.xml";
	private static String SKIP_CHARACTERS_XML = "etc/skip_characters.xml";
	//LettersXml letters = new LettersXml(LETTERS_XML);
	LettersXml _lettersXml = null;
	SkipCharactersXml _skipCharXml = null;

	private String _mode = null;
	private String _tanachDir = null;

	private Tanach _tanach = null;
	
	public LettersXml getLettersXml() {
		return this._lettersXml;
	}


	private void init() {
		InputStream input = null;

		try {
			Properties prop = new Properties();

			input = new FileInputStream(CONFIG_FILE);

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			this._mode = prop.getProperty("code_wrapper.mode");
			this._tanachDir = prop.getProperty("code_wrapper.tanach.dir");
			
			if(this._mode == null || this._mode.trim().equals("")) {
				log.error("Mode is not set");
				return;
			}
			
			this._lettersXml = new LettersXml(LETTERS_XML);
			this._skipCharXml = new SkipCharactersXml(SKIP_CHARACTERS_XML);
			
		} 
		catch (IOException ex) {
			ex.printStackTrace();
		} 
		finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public CodeWrapper() {
		this.init();
	}
	
	

    public static void main(String[] args) {
    	BasicConfigurator.configure();
        log.debug("Code Wrapper started ...");
        
        CodeWrapper codeWrapper = new CodeWrapper();
        
        String mode = codeWrapper.getMode();

		if(mode.equals("test")) {
			codeWrapper.runTest();
		}
		else if(mode.equals("produce_matrix")) {
			codeWrapper.produceMatrix();
		}
		else if(mode.equals("check_frequency_measure")) {
			codeWrapper.checkFrequencyMeasure();
		}
		else if(mode.equals("get_fibonacci_numbers")) {
			codeWrapper.getFibonacciNumbers();
		}
		else if(mode.equals("init")) {
			codeWrapper.initializeTanach();
		}
        
        log.debug("mode: "+mode);
    }
    
    public void getFibonacciNumbers() {
    	this._tanach = new Tanach(this._tanachDir, this._lettersXml, this._skipCharXml);
    	this._tanach.buildLetterMatrix();
    	
    	List<Letter> letterList = this._tanach.getLetterList();
    	
    	int letterNumber = 200;
    	boolean haveFoundNumber = false;
    	int counter = 1;
    	for(Letter letter : letterList) {
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
    }
    
    public void initializeTanach() {
    	this._tanach = new Tanach(this._tanachDir, this._lettersXml, this._skipCharXml);
    	this._tanach.buildLetterMatrix();
    	this._tanach.initializeLetterProperties();
    }
    

    public void runTest() {
    	log.debug("tanach dir: "+this._tanachDir);
    	
    	this._tanach = new Tanach(this._tanachDir, this._lettersXml);
    	this._tanach.scan();
    	
    	this._tanach.skip(Book.GENESIS_NAME, 50, 4, 'T');
    	this._tanach.skip(Book.EXODUS_NAME, 50, 4, 'T');
    	this._tanach.skip(Book.LEVITICUS_NAME, 8, 4, 'Y');
    	this._tanach.skip(Book.DEUTERONOMY_NAME, 50, 4, 'H');
    	this._tanach.skip(Book.NUMBERS_NAME, 50, 4, 'T');
    }
    
    public void checkFrequencyMeasure() {
    	this._tanach = new Tanach(this._tanachDir, this._lettersXml, this._skipCharXml);
    	this._tanach.buildLetterMatrix();
    	
    	List<FrequencyMeasure> measureList = new ArrayList<FrequencyMeasure>();
    	FrequencyMeasure tafMeasure = new FrequencyMeasure(400);
    	FrequencyMeasure vavMeasure = new FrequencyMeasure(6);
    	FrequencyMeasure raishMeasure = new FrequencyMeasure(200);
    	FrequencyMeasure hayMeasure = new FrequencyMeasure(5);
    	
    	measureList.add(tafMeasure);
    	measureList.add(vavMeasure);
    	measureList.add(raishMeasure);
    	measureList.add(hayMeasure);

    	this._tanach.applyFrequencyMeasure(measureList, 2, 50);
    }

    public void produceMatrix() {
    	this._tanach = new Tanach(this._tanachDir);
    	//this._tanach.intitialize();

    	//this._tanach.produceBasicMatrix(Book.GENESIS_NAME, 50, 4, 'T');

    }

    public String getMode() {
    	return this._mode;
    }
}