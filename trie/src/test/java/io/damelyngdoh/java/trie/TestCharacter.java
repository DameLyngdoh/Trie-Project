package io.damelyngdoh.java.trie;

import io.damelyngdoh.java.trie.TrieCharacter;

public class TestCharacter extends TrieCharacter {

    private char c;

    public TestCharacter(char c) {
        this.c = c;
    }

    public char getC() {
        return this.c;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof TrieCharacter) {
            TestCharacter c = (TestCharacter)o;
            return this.c == c.getC();
        }
        throw new ClassCastException();
    }

    @Override
    public int hashCode() {
        return (int)c;
    }
    
    @Override
    public String toString() { return this.c + ""; }
}