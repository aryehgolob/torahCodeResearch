package com.qaz216.codes.components.letters;

public class Letter implements Cloneable {
	private int _gematria = 0;
	private String _name = null;
	private String _numValue = null;
	private String _charTrans = null;
	private String _verseId = null;
	private int _firstEncounter = -1;
	
	public Letter(String name, String numValue, String charTrans) {
		this._name = name;
		this._numValue = numValue;
		this._charTrans = charTrans;
	}
	
	//public Letter clone()throws CloneNotSupportedException{  
	public Object clone() {  
		try {
			return (Letter) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
	 }
	
	public void setVerseId(String verseId) {
		this._verseId = verseId;
	}
	
	public String getVerseId() {
		return this._verseId;
	}

	public String getName() {
		return this._name;
	}
	
	public int getNumInt() {
		return Integer.parseInt(this._numValue);
	}

	public String getNumValue() {
		return this._numValue;
	}
	
	public String getCharTrans() {
		return this._charTrans;
	}
	
	public String toString() {
		String s = "\n\n--- Letter \n" +
	               "Name: "+this._name+"\n" +
	               "Numerical Value: "+this._numValue+"\n" +
	               "Char Trans: "+this._charTrans+"\n\n";

		return s;
	}

}
