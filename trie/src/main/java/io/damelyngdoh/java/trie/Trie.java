package io.damelyngdoh.java.trie;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * Trie class represents a Trie data-structure which saves, retrieves or removes 
 * strings which are a sequence of characters of type <tt>TrieCharacter</tt>. The 
 * vertices of the tree are represented by the <tt>TrieNode</tt> objects. The trie 
 * object contains a map object which represents the root node of the tree and contains 
 * mappings of <tt>TrieCharacter</tt> to <tt>TrieNode</tt> which are the initial characters 
 * of the strings added to the trie.
 * </p>
 * 
 * @param <T> The type of characters the trie will contain, must extends <tt>TrieCharacter</tt> class.
 * @param <V> The type of data which the Trie data-structure will contain in its nodes.
 * 
 * @author Dame Lyngdoh
 * @since 1.0.0
 */
public class Trie<T extends TrieCharacter, V> implements Map<List<TrieCharacter>, V> {

    /**
     * Counter for the number of strings in the Trie.
     */
    private int stringCount;
    /**
     * Counter for the identifier for the nodes.
     */
    private AtomicLong nodeIdCounter;
    /**
     * The traversal method of searching for the Trie.
     */
    private TrieTraversal traversal;
    /**
     * Root-map of the trie containing the first character of every string.
     */
    private Map<TrieCharacter, TrieNode<V>> rootMap;
    /**
     * Flag indicating if overwrite is allowed or not.
     */
    private boolean overwriteAllowed = true;

    /**
     * Constructs a new empty Trie object.
     */
    public Trie() {
        this.traversal = TrieTraversal.Incremental;
        this.stringCount = 0;
        this.rootMap = new HashMap<>();
        this.nodeIdCounter = new AtomicLong(0);
    }

    /**
     * Constructs a new Trie object with the provided traversal method.
     * @param traversal The traversal type.
     * @see TrieTraversal
     */
    public Trie(TrieTraversal traversal) {
        this.traversal = traversal;
        this.stringCount = 0;
        this.rootMap = new HashMap<>();
        this.nodeIdCounter = new AtomicLong(0);
    }

    /**
     * @return Returns the current traversal method of the trie.
     * @see TrieTraversal
     */
    public TrieTraversal getTraversal() {
        return this.traversal;
    }

    /**
     * Sets the traversal method of the trie.
     * @param traversal The traversal method
     * @see TrieTraversal
     */
    public void setTraversal(TrieTraversal traversal) {
        this.traversal = traversal;
    }

    /**
     * Returns true if overwrite is allowed or false otherwise.
     * @return Returns true if overwrite is allowed or false otherwise.
     */
    public boolean isOverwriteAllowed() {
        return this.overwriteAllowed;
    }

    /**
     * Sets the overwrite flag of the trie.
     * @param overwriteAllowed True when overwrite is allowed or false to disallow.
     */
    public void setOverwriteAllowed(boolean overwriteAllowed) {
        this.overwriteAllowed = overwriteAllowed;
    }

    /**
     * Returns the number of strings in the trie.
     * @return Returns the number of strings in the trie.
     */
    @Override
    public int size() {
        return this.stringCount;
    }

    /**
     * Returns true if the trie is empty or false if otherwise.
     * @return Returns true if the trie is empty or false if otherwise.
     */
    @Override
    public boolean isEmpty() {
        return this.stringCount==0;
    }

    /**
     * Checks if a string is present in the trie or not.
     * @param key The list of characters (or string) to be searched.
     * @return Returns true if the string is found or false otherwise.
     * @throws ClassCastException Thrown when the object is not an instance of <tt>List</tt> or when one of the elements in the list is not an instance of <tt>TrieCharacter</tt>
     * @throws IllegalArgumentException Thrown when one of the elements of the list is null.
     * @throws NullPointerException Thrown when the argument is null.
     */
    @Override
    public boolean containsKey(Object key) throws IllegalArgumentException, ClassCastException, NullPointerException {
        List<TrieCharacter> string = Trie.validateString(key);
        // Check if string is empty
        if(string.isEmpty()) {
            return false;
        }
        
        List<TrieNode<V>> traversedNodes = this.DFS(string);
        if(traversedNodes.size()!=string.size()) {
            return false;
        }


        TrieNode<V> lastNode = traversedNodes.get(traversedNodes.size()-1);
        return lastNode.isValid();
    }

    /**
     * Checks to see if the value is present in the trie.
     * @return Returns true if the value is found or false otherwise.
     */
    @Override
    public boolean containsValue(Object value) {
        final AtomicBoolean contains = new AtomicBoolean(false);
        this.DFT((Stack<TrieNode<V>> traversedNodes)->{
            if(traversedNodes.peek().getData().equals(value)) {
                contains.set(true);
                return false;
            }
            return true;
        });
        return contains.get();
    }

    /**
     * Gets the value/data associated with the string key.
     * @param key The string to be searched.
     * @return Returns the value associated with the key or <tt>null</tt> if the string is not found.
     * @throws ClassCastException Thrown when the object is not an instance of <tt>List</tt> or when one of the elements in the list is not an instance of <tt>TrieCharacter</tt>
     * @throws IllegalArgumentException Thrown when one of the elements of the list is null.
     * @throws NullPointerException Thrown when the argument is null.
     */
    @Override
    public V get(Object key) throws ClassCastException, IllegalArgumentException, NullPointerException {
        
        // Casting key object to string
        List<TrieCharacter> string = Trie.validateString(key);

        // Check if string is empty
        if(string.isEmpty()) {
            return null;
        }

        List<TrieNode<V>> traversedNodes = this.DFS(string);
        TrieNode<V> lastNode = traversedNodes.size()==0 ? null : traversedNodes.get(traversedNodes.size()-1);
        // Check if string is found
        if(string.size()!=traversedNodes.size() || !lastNode.isValid()) {
            return null;
        }
        return lastNode.getData();
    }

    /**
     * Adds a string with its associated data. If the string is already present then the overwrite flag will be checked.
     * @param key The string to be added to the trie.
     * @param value The data/value to be mapped to the string.
     * @return Returns the value associated with the key that was added.
	 * @throws ClassCastException Thrown when the object is not an instance of <tt>List</tt> or when one of the elements in the list is not an instance of <tt>TrieCharacter</tt>
	 * @throws IllegalArgumentException Thrown when one of the elements of the list is null.
	 * @throws NullPointerException Thrown when the argument is null.
     */
    @Override
    public V put(List<TrieCharacter> key, V value) throws IllegalArgumentException, ClassCastException, NullPointerException {
      
        // Casting key object to string
        List<TrieCharacter> string = Trie.validateString(key);
        if(string.isEmpty()) {
            return null;
        }
        List<TrieNode<V>> traverseNodes = this.DFS(string);
        if(traverseNodes.isEmpty()) {
            this.rootMap.put(string.get(0), this.newChain(null, string, 0, value));
        }
        else {
            TrieNode<V> lastNode = traverseNodes.get(traverseNodes.size()-1);
            if(traverseNodes.size()==string.size()) {
                // Restricting overwrite if overwrite flag is set to true
                if(!lastNode.isValid() || (lastNode.isValid() && this.isOverwriteAllowed())) {
                    lastNode.putData(value);
                }
            }
            else {
                lastNode.addChild(string.get(traverseNodes.size()), this.newChain(lastNode, string, traverseNodes.size(), value));
            }
        }
        this.stringCount++;
        return value;
    }

    /**
     * Removes a string from the trie.
     * @param key The string to be removed.
     * @return Returns the data associated with the string if the string was present in the trie or null if absent.
     * @throws ClassCastException Thrown when the object is not an instance of <tt>List</tt> or when one of the elements in the list is not an instance of <tt>TrieCharacter</tt>
	 * @throws IllegalArgumentException Thrown when one of the elements of the list is null.
	 * @throws NullPointerException Thrown when the argument is null.
     */
    @Override
    public V remove(Object key) throws ClassCastException, IllegalArgumentException, NullPointerException {
        
        // Casting key object to string
        List<TrieCharacter> string = Trie.validateString(key);

        // Check if string is empty
        if(string.isEmpty()) {
            return null;
        }

        List<TrieNode<V>> traversedNodes = this.DFS(string);

        // String not found
        if(string.size()!=traversedNodes.size()) {
            return null;
        }

        TrieNode<V> lastNode = traversedNodes.get(traversedNodes.size()-1);

        // String not found
        if(!lastNode.isValid()) {
            return null;
        }

        // Removing data from last node
        V data = lastNode.getData();
        lastNode.removeData();
        this.stringCount--;

        if(lastNode.getChildrenCount() > 0) {
            return data;
        }

        // Removing longest empty chain from last node
        TrieNode<V> currentNode = lastNode, parent = currentNode.getParent();

        while(parent!=null) {
            if(parent.getChildrenCount() > 1 || parent.isValid()) {
                break;
            }
            currentNode = parent;
            parent = currentNode.getParent();
        }
        if(parent==null) {
            this.rootMap.remove(currentNode.getCharacter());
        }
        else {
            parent.removeChild(currentNode);
        }
        return data;
    }

    /**
     * Puts all mappings in map argument into this instance.
     * @param m The map whose mapping is to be included.
     * @throws ClassCastException Thrown when a string object in the map is not an instance of <tt>List</tt> or when one of the elements of the string is not an instance of <tt>TrieCharacter</tt>
	 * @throws IllegalArgumentException Thrown when there exists a string in the key-set of the map that contains a <tt>null</tt> character.
	 * @throws NullPointerException Thrown when there exists a null string in the map.
     */
    @Override
    public void putAll(Map<? extends List<TrieCharacter>, ? extends V> m) throws NullPointerException, ClassCastException, IllegalArgumentException {
        if(m==null) {
            return;
        }
        for(List<TrieCharacter> string : m.keySet()) {
            this.put(string, m.get(string));
        }
    }

    /**
     * Empties the trie.
     */
    @Override
    public void clear() {
        this.rootMap.clear();
        this.stringCount = 0;
        this.nodeIdCounter = new AtomicLong(0);
    }

    /**
     * Gets the set of all distinct strings (or keys) in the trie.
     * @return Returns the <tt>Set</tt> object containing all the strings within the trie.
     */
    @Override
    public Set<List<TrieCharacter>> keySet() {
        final Set<List<TrieCharacter>> keySet = new HashSet<>();
        this.DFT((Stack<TrieNode<V>> nodes) -> {
            List<TrieCharacter> string = new ArrayList<>();
            for(TrieNode<V> node : nodes) {
                string.add(node.getCharacter());
            }
            keySet.add(string);
            return true;
        });
        return keySet;
    }

    /**
     * Gets a collection of all the values or data assocaited with any string within the trie.
     * @return Returns a <tt>Collection</tt> of all the values.
     */
    @Override
    public Collection<V> values() {
        final Collection<V> values = new ArrayList<>();
        this.DFT((Stack<TrieNode<V>> nodes) -> {
            values.add(nodes.peek().getData());
            return true;
        });
        return values;
    }

    /**
     * Gets a set of all the <tt>Entry</tt> objects or entries in the trie.
     * @return Returns a <tt>Set</tt> of all possible <tt>Entry</tt> objects.
     * @see Entry
     */
    @Override
    public Set<Entry<List<TrieCharacter>, V>> entrySet() {
        final Set<Entry<List<TrieCharacter>, V>> entrySet = new HashSet<>();
        this.DFT((Stack<TrieNode<V>> nodes) -> {
            List<TrieCharacter> string = new ArrayList<>();
            for(TrieNode<V> node : nodes) {
                string.add(node.getCharacter());
            }
            entrySet.add(new AbstractMap.SimpleEntry<>(string, nodes.peek().getData()));
            return true;
        });
        return entrySet;
    }

    /**
     * Validates if an object is a list of objects of sub-class of TrieCharacter.
     * @param object The object to be validated.
     * @return Returns List of objects which are instances of TrieCharacter.
     * @throws IllegalArgumentException Thrown if the object is a list and one of its elements is null.
     * @throws ClassCastException Thrown if the object is not a list or if one of the elements of the list is not an instance of TrieCharacter.
     * @throws NullPointerException Thrown when the object (or string) is null.
     */
    public static List<TrieCharacter> validateString(Object object) throws IllegalArgumentException, NullPointerException, ClassCastException {
        if(object==null) {
            throw new NullPointerException("Object cannot be null.");
        }
        if(object instanceof List) {
            List<Object> list = (List<Object>)object;
            for(Object listObject : list) {
                if(listObject==null) {
                    throw new IllegalArgumentException("Trie character cannot be null.");
                }
                if(!(listObject instanceof TrieCharacter)) {
                    throw new ClassCastException("Character not sub-class of TrieCharacter.");
                }
            }
        }
        else {
            throw new ClassCastException("Object is not a sub-class of list.");
        }
        List<TrieCharacter> string = (List<TrieCharacter>)object;
        return string;
    }

    /**
     * Creates a chain of nodes (in a linked-list fashion) using the characters from the string.
     * @param parent The node which the chain is to be attached to.
     * @param string The string of characters to be used in the same order of the chain.
     * @param startIndex The starting index of the character in the string to be the starting character of the chain.
     * @param data The data to be associated with the last node of the chain.
     * @return Returns the first node of the chain.
     */
    public TrieNode<V> newChain(TrieNode<V> parent, List<TrieCharacter> string, int startIndex, V data) {

        // Creating new starting node
        TrieNode<V> newNode = this.getNewNode(parent, string.get(startIndex), data, false);

        // Creating new subsequent nodes
        TrieNode<V> currentNode = newNode;
        TrieNode<V> nextNode = null;
        for(startIndex+=1 ; startIndex < string.size(); startIndex++) {
            nextNode = this.getNewNode(currentNode, string.get(startIndex), null, false);
            currentNode.addChild(nextNode.getCharacter(), nextNode);
            currentNode = nextNode;
        }

        // If no next nodes or if stirng contains only one character
        if(nextNode==null) {
            currentNode.putData(data);
        }
        // Else put data on last node of the chain
        else {
            nextNode.putData(data);
        }
        return newNode;
    }

    /**
     * Performs a depth-first search for a string in the trie.
     * @param string The string to be searched.
     * @return Returns the list of nodes traversed during the search.
     */
    public List<TrieNode<V>> DFS(List<TrieCharacter> string) {
        Trie.validateString(string);
    	switch (this.traversal) {
            case Incremental:
                return this.DFSIncremental((string));
            case Recursive:
                List<TrieNode<V>> traversedNodes = new ArrayList<>();
                this.DFSRecursive(string, 0, this.rootMap.get(string.get(0)), traversedNodes);
                return traversedNodes;
        }
        return null;
    }

    /**
     * Performs a depth-first search for a string in the trie.
     * @param string The string to be searched.
     * @param traversal The traversal method to use for the search.
     * @return Returns the list of nodes traversed during the search.
     */
    public List<TrieNode<V>> DFS(List<TrieCharacter> string, TrieTraversal traversal) {
        TrieTraversal current = this.getTraversal();
        this.setTraversal(traversal);
    	Trie.validateString(string);
    	switch (this.traversal) {
            case Incremental:
                return this.DFSIncremental((string));
            case Recursive:
                List<TrieNode<V>> traversedNodes = new ArrayList<>();
                this.DFSRecursive(string, 0, this.rootMap.get(string.get(0)), traversedNodes);
                this.setTraversal(current);
                return traversedNodes;
        }
        return null;
    }
    
    /**
     * Searches for a string in the tree using a loop structure by traversing it depth-first.
     * @param string The string to be searched in the tree.
     * @return Returns the list of nodes traversed during the search.
     */
    private List<TrieNode<V>> DFSIncremental(List<TrieCharacter> string) {
        List<TrieNode<V>> traversedNodes = new ArrayList<>(string.size());
        TrieNode<V> currentNode = this.rootMap.get(string.get(0));
        if(currentNode!=null) {
            traversedNodes.add(currentNode);
            TrieNode<V> nextNode = null;
            for(int i=1; i<string.size(); i++) {
                nextNode = currentNode.getChildNode(string.get(i));
                if(nextNode==null) {
                    break;
                }
                traversedNodes.add(nextNode);
                currentNode = nextNode;
            }
        }
        return traversedNodes;
    }

    /**
     * Searches for a string in the tree by recursively traversing the tree depth-first.
     * @param string The string to be searched.
     * @param currentIndex The index of the character reached currently in the string.
     * @param currentNode The node currently reached in the tree.
     * @param traversedNodes The list of nodes that have been traversed.
     */
    private void DFSRecursive(List<TrieCharacter> string, int currentIndex, TrieNode<V> currentNode, List<TrieNode<V>> traversedNodes) {
        if(currentNode==null) {
            return;
        }
        traversedNodes.add(currentNode);
        currentIndex++;
        if(currentIndex >= string.size()) {
            return;
        }
        this.DFSRecursive(string, currentIndex, currentNode.getChildNode(string.get(currentIndex)), traversedNodes);
    }

    /**
     * Creates a new node and returns the newly created node.
     * @param parent The parent of the new node.
     * @param character The character to be associated with the new node.
     * @param data The data the node must contain.
     * @param isValid True if the node is a valid node or false otherwise.
     * @return Returns the new node with updated node id.
     */
    private TrieNode<V> getNewNode(TrieNode<V> parent, TrieCharacter character, V data, boolean isValid) {
        TrieNode<V> newNode = new TrieNode<V>(this.nodeIdCounter.incrementAndGet(), character, parent);
        if(isValid) {
            newNode.putData(data);
        }
        return newNode;
    }

    /**
     * Initiates the depth-first traversal of the tree from the root map.
     * @param operation The lambda expression to perform when a valid node is encountered during traversal.
     */
    public void DFT(PostTraversalOperation<V> operation) {
        Stack<TrieNode<V>> traversedNodes = new Stack<>();
        for(TrieNode<V> currentNode : this.rootMap.values()) {
            this.DFTRecursive(currentNode, traversedNodes, operation);
        }
    }

    /**
     * Recursively traverses the tree and calls operation method whenever a valid node is encountered.
     * @param currentNode The current node reached during the traversal.
     * @param traversedNodes The list of nodes traversed (in order).
     * @param operation The method to be called when the current node is valid.
     */
    private void DFTRecursive(TrieNode<V> currentNode, Stack<TrieNode<V>> traversedNodes, PostTraversalOperation<V> operation) {
        traversedNodes.push(currentNode);
        if(currentNode.isValid()) {
            if(!operation.postOperation(traversedNodes)) {
                return;
            };
        }
        if(currentNode.getChildrenCount() > 0) {
            for(TrieCharacter c : currentNode.getAllCharacters()) {
                this.DFTRecursive(currentNode.getChild(c), traversedNodes, operation);
            }
        }
        traversedNodes.pop();
    }

    /**
     * Functional interface for declaring operations whenever a valid node is encountered during a depth-first traversal of the complete tree.
     * @param <V> Data class type.
     */
    public interface PostTraversalOperation<V> {
        /**
         * Invoked whenever by depth-first traversal method whenever a valid node is encountered.
         * @param traversedNodes The nodes traversed (string of characters) from the root.
         * @return Returns true if further traversal of other nodes is required or false if traversal needs to be stopped.
         */
        boolean postOperation(Stack<TrieNode<V>> traversedNodes);
    }
}
