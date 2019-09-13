package io.damelyngdoh.java.trie;

/**
 * <p>
 * This abstract class must be extended by classes which 
 * are meant to represent characters. The methods <tt>equals</tt> and 
 * <tt>hashCode</tt> must be defined in the extended methods which will 
 * be used by the <tt>HashMap</tt> objects within <tt>Trie</tt> and 
 * <tt>TrieNode</tt>.
 * </p>
 * @author Dame Lyngdoh
 * @since 1.0.0
 */
public abstract class TrieCharacter extends Object{

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
