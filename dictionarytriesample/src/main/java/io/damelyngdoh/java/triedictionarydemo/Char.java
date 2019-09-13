package io.damelyngdoh.java.triedictionarydemo;

import io.damelyngdoh.java.trie.TrieCharacter;

/**
 * Class representing a character of a word.
 * @author Dame
 *
 */
public class Char extends TrieCharacter {

	private final char c;
	
	public Char(char c) {
		this.c = c;
	}
	
	public char getChar() { return this.c; }
	
	@Override
	public boolean equals(Object o) {
		return this.c == ((Char)o).getChar();
	}

	@Override
	public int hashCode() {
		return (int)this.c;
	}
	
	@Override
	public String toString() { return this.c + ""; }
	
}
