/**
 * 
 */
package io.damelyngdoh.java.triedictionarydemo;

/**
 * Class containing the meta-data of a word.
 * @author Dame
 *
 */
public class WordMetaData {
	
	private String word;
	private String pos;
	private String meaning;
	
	public WordMetaData(String word, String pos, String meaning) {
		this.word = word;
		this.pos = pos;
		this.meaning = meaning;
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the pos
	 */
	public String getPos() {
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(String pos) {
		this.pos = pos;
	}

	/**
	 * @return the meaning
	 */
	public String getMeaning() {
		return meaning;
	}

	/**
	 * @param meaning the meaning to set
	 */
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	
	@Override
	public String toString() {
		return "[" + this.word + "; part of speech: " + this.pos + "; meaning: " + this.meaning + "]";
	}
}
