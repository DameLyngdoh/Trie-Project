package io.damelyngdoh.java.trie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * TrieNode class represents a node or a vertex (in Graph language) of the Trie 
 * data-structure. The node consists primarily of:
 * <ul>
 * <li>
 * <tt>long</tt> type identifier 
 * which is unique for the node in the <tt>Trie</tt> object instance which it resides
 * </li>
 * <li>
 * a <tt>TrieCharacter</tt> object which represents the character which the node is 
 * associated with
 * </li>
 * <li>
 * <tt>V</tt> (generic) type object which represents the data which the node contains
 * </li>
 * <li>
 * a reference to the parent node or null if the node is contained in the root-map of the trie
 * </li>
 * <li>
 * a Map type object which maps the next characters to the corresponding nodes (child nodes).
 * </li>
 * </ul>
 * <p>
 * A node is a valid node if it is the last node of a string added to the trie.
 * In other words, it is the node which is associated with the last character of 
 * the string. This node holds data associated with the string. The data held by 
 * an invalid node is <tt>null</tt>.
 * </p>
 * @param <V> Data class type.
 * 
 * @author Dame Lyngdoh
 * @since 1.0.0
 */
public class TrieNode<V> {

    /**
     * The identifier for the node.
     */
    private long id;
    /**
     * Flag indicating if the node is valid or not. A valid node is a node which represents a character at the end of the string.
     */
    private boolean validity;
    /**
     * Map to the children nodes (or next characters).
     */
    private Map<TrieCharacter, TrieNode<V>> childrenMap;
    /**
     * Reference to the parent of the node. Null if the parent is the root-map.
     */
    private TrieNode<V> parent;
    /**
     * The character identifying or associated with the node.
     */
    private TrieCharacter character;
    /**
     * The data contained by the node if the node is valid or null if invalid.
     */
    private V data;

    /**
     * Contructs a new TrieNode object with the specified parameters.
     * @param id The numerical identifier for the node.
     * @param character The character associated with the node.
     * @param parent The parent-node.
     */
    public TrieNode(long id, TrieCharacter character, TrieNode<V> parent) {
        // Character should not be null
        if(character==null) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.parent = parent;
        this.character = character;
        this.childrenMap = new HashMap<TrieCharacter, TrieNode<V>>();
    }

    /**
     * Gets a set of all character to children node mapping of the current node.
     * @return Returns a Set object of all TrieCharacter to TrieNode mapping.
     */
    public Set<TrieCharacter> getAllCharacters() {
        return this.childrenMap.keySet();
    }

    /**
     * Gets all children of the node.
     * @return Returns a Collection of children of the node.
     */
    public Collection<TrieNode<V>> getAllChildren() {
        return this.childrenMap.values();
    }

    /**
     * Gets the chil-node associated with the character argument.
     * @param character The character whose corresponding node is to be returned.
     * @return Returns the TrieNode object associated with the character.
     */
    public TrieNode<V> getChild(TrieCharacter character) {
        if(character==null) {
            throw new IllegalArgumentException("Null character exception while getting child.");
        }
        return this.childrenMap.get(character);
    }

    /**
     * Adds a new mapping to the node.
     * @param character The character to map to the child node.
     * @param node The child node to be mapped to the character.
     */
    public void addChild(TrieCharacter character, TrieNode<V> node) {
        if(character==null) {
            throw new IllegalArgumentException("Null character exception.");
        }
        if(node==null) {
            throw new IllegalArgumentException("Null node exception.");
        }
        this.childrenMap.put(character, node);
    }

    /**
     * Checks if the node has a child node associated with the character.
     * @param character The character whose child node is to be checked for.
     * @return Returns true if the child node mapping exists or false otherwise.
     */
    public boolean hasChild(TrieCharacter character) {
        if(character==null) {
            throw new IllegalArgumentException("Null character exception.");
        }
        return this.childrenMap.containsKey(character);
    }

    /**
     * Checks if the node has the mentioned child node.
     * @param childNode The child-node object.
     * @return Returns true if the child node mapping exists or false otherwise.
     */
    public boolean hasChild(TrieNode<V> childNode) {
        if(childNode==null) {
            throw new IllegalArgumentException("Null child node.");
        }
        return this.childrenMap.containsKey(childNode.getCharacter());
    }

    /**
     * Gets the node corresponding to the character.
     * @param character The character to obtained the mapped node.
     * @return Returns the TrieNode object corresponding to the character.
     * @throws IllegalArgumentException Thrown when the character argument is null.
     */
    public TrieNode<V> getChildNode(TrieCharacter character) {
        if(character==null) {
            throw new IllegalArgumentException("Null character exception.");
        }
        return this.childrenMap.get(character);
    }

    /**
     * Removes the child node corresponding to the character.
     * @param character The character object.
     */
    public void removeChild(TrieCharacter character) {
        if(character==null) {
            throw new IllegalArgumentException("Null character exception.");
        }
        this.childrenMap.remove(character);
    }

    /**
     * Removes the child node if present in the map.
     * @param childNode The child node to be removed.
     */
    public void removeChild(TrieNode<V> childNode) {
        if(childNode==null) {
            throw new IllegalArgumentException("Null child node.");
        }
        this.removeChild(childNode.getCharacter());
    }

    /**
     * Gets the number of child nodes the node has.
     * @return Returns the integer count of the child nodes.
     */
    public int getChildrenCount() {
        return this.childrenMap.size();
    }

    /**
     * Gets the character associated with the node.
     * @return Returns the TrieCharacter object associated with the node.
     */
    public TrieCharacter getCharacter() {
        return this.character;
    } 

    /**
     * Gets the parent of the node. If the node is part of the root-map of the Trie then this method returns null.
     * @return Returns the TrieNode object which is the parent of the node or null if the node is in the root-map.
     */
    public TrieNode<V> getParent() {
        return this.parent;
    }

    /**
     * Gets the unique numerical identifier of the node.
     * @return Returns the long id of the node.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Gets the validity of the node.
     * @return Returns true if the node is valid or false otherwise.
     */
    public boolean isValid() {
        return this.validity;
    }

    // Data Methods
    /**
     * Gets the data contained by the node.
     * @return Returns the data the node holds if the node is valid or null if the node is invalid.
     */
    public V getData() {
        return this.data;
    }

    /**
     * Adds data to the node.
     * @param data The data to be associated with the node.
     */
    public void putData(V data) {
        this.data = data;
        this.validity = true;
    }

    /**
     * Removes the data from the node and invalidates (sets the isValid flag to false) the node.
     */
    public void removeData() {
        this.data = null;
        this.validity = false;
    }
}