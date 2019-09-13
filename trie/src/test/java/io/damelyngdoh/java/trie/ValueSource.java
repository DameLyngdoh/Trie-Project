package io.damelyngdoh.java.trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.damelyngdoh.java.trie.TrieCharacter;

/**
 * Class containig all values to be used for testing.
 * @author Dame
 *
 */
public final class ValueSource {
	public static final int Valid = 0, Sub = 1, Super = 2, Extended1 = 3, Extended2 = 4, Common = 5, UnitLength = 6, ValidStringCount = 7;
	public static final int Invalid = -1, Absent = -2, Empty = -3, Null = -4;
	public static final String 
		ValidString = "wOdJlILmQoSWg",
		SubString = "EGqBXuz",
		SuperString = "EGqBXuzrcwn",
		Extended1String = "iFxSjnJjOFe",
		Extended2String = "iFxSjnJpUIn",
		CommonString = "iFxSjnJ",
		UnitLengthString = "D",
		AbsentString = "FfFabcdefgh",
		InvalidString = "abcdefghi",
		EmptyString = "",
		NullString = null;
	public static final Map<Integer, List<TrieCharacter>> stringMap = new HashMap<>();
	public static final Map<Integer, String> dataMap = new HashMap<>();
	
	public static final void init() {
		dataMap.put(0, ValidString);
		dataMap.put(1, SubString);
		dataMap.put(2, SuperString);
		dataMap.put(3, Extended1String);
		dataMap.put(4, Extended2String);
		dataMap.put(5, CommonString);
		dataMap.put(6, UnitLengthString);
		dataMap.put(-1, InvalidString);
		dataMap.put(-2, AbsentString);
		dataMap.put(-3, EmptyString);
		dataMap.put(-4, NullString);
		
		for(Integer i : dataMap.keySet()) {
        	stringMap.put(i, convertToTrieString(dataMap.get(i)));
        }
	}
	
	/**
     * Converts a string to a list of TrieCharacter.
     * @param str The string to be processed.
     * @return Returns a new Trie string.
     */
    public static final List<TrieCharacter> convertToTrieString(String str) {
        if(str==null) {
        	return null;
        }
    	List<TrieCharacter> trieStr= new ArrayList<>(str.length());
        for(int i=0; i<str.length(); i++) {
            trieStr.add(new TestCharacter(str.charAt(i)));
        }
        return trieStr;
    }
}