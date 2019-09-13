package io.damelyngdoh.java.triedictionarydemo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.damelyngdoh.java.trie.Trie;
import io.damelyngdoh.java.trie.Trie.PostTraversalOperation;
import io.damelyngdoh.java.trie.TrieCharacter;
import io.damelyngdoh.java.trie.TrieNode;

/**
 * @author Dame
 *
 */
public class DictionaryImplementation {

	/**
	 * Main method
	 * @param args Command line arguments.
	 */
	public static final String sourceFile = "source.csv";
	public static final String[] inputWords = {"walk", "sky", "lie"};
	public static void main(String[] args) throws IOException {
		
		// Initializing Trie object
		Trie<Char,WordMetaData> dictionary = new Trie<>();
		
		// Reading words and the meta-data from source file
		try(BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
			for(String line = reader.readLine(); line!=null; line = reader.readLine()) {
				String[] tokens = line.split(",");
				
				// Populating Trie object
				dictionary.put(stringToTrieString(tokens[0]), new WordMetaData(tokens[0], tokens[1], tokens[2]));
			}
		}
		
		// Searching the dictionary for the meta-data of the input-words.
		for(int i=0; i<inputWords.length; i++) {
			System.out.println(dictionary.get(stringToTrieString(inputWords[i])));
		}
		
		
		// Find all nouns in the dictionary
		// Defining list to hold all nouns
		final List<WordMetaData> nouns = new ArrayList<>();
		// Defining lambda body which is executed every time the 
		// depth-first traversal method encounters a valid node (or a string)
		PostTraversalOperation<WordMetaData> nounSearch = (Stack<TrieNode<WordMetaData>> traversedNodes) -> {
			TrieNode<WordMetaData> lastNode = traversedNodes.lastElement(); 
			if(lastNode.getData().getPos().equalsIgnoreCase("noun")) {
				nouns.add(lastNode.getData());
			}
			return true;
		};
		// Calling the depth-first traversal
		dictionary.DFT(nounSearch);
		System.out.println("\n\nNouns in the dictionary:");
		for(WordMetaData meta : nouns) {
			System.out.println(meta);
		}
	}
	
	/**
	 * Converts String object to List<TrieCharacter> object.
	 * @param string The string object to be converted.
	 * @return Returns List<TrieCharacter> object with ordered characters.
	 */
	public static List<TrieCharacter> stringToTrieString(String string) {
		List<TrieCharacter> list = new ArrayList<>(string.length());
		for(int i=0; i<string.length(); i++) {
			list.add(new Char(string.charAt(i)));
		}
		return list;
	}
}
