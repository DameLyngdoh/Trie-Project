package io.damelyngdoh.java.trie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import io.damelyngdoh.java.trie.Trie;
import io.damelyngdoh.java.trie.TrieCharacter;
import io.damelyngdoh.java.trie.TrieTraversal;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrieTest {

    private Trie<TestCharacter,String> trie;
    
    @BeforeEach
    void beforeEachTest() throws FileNotFoundException, IOException {
        // Initialization
    	ValueSource.init();
    	this.trie = new Trie<>();
        
        // Adding valid strings to trie
        for(int i=0; i<ValueSource.ValidStringCount; i++) {
            this.trie.put(ValueSource.stringMap.get(i), ValueSource.dataMap.get(i));
        }
        
        // Creating string with null character
        List<TrieCharacter> temp = ValueSource.stringMap.get(ValueSource.Invalid);
        temp.add(RandomUtils.nextInt(0, temp.size()), null);
        ValueSource.stringMap.put(ValueSource.Invalid, temp);
    }

    @Test
    @DisplayName("Validate String Test")
    @Order(1)
    void validateStringTest() {
        assertDoesNotThrow(()->{
            Trie.validateString(ValueSource.stringMap.get(ValueSource.Valid));
        }, "Valid string validation threw an exception.");
        assertThrows(NullPointerException.class, ()->{
            Trie.validateString(null);
        }, "Validation did not throw NullPointerException.");
        assertThrows(IllegalArgumentException.class, ()->{
            Trie.validateString(ValueSource.stringMap.get(ValueSource.Invalid));
        }, "Validation did not throw IllegalArgumentException.");
        assertThrows(ClassCastException.class, ()->{
            Trie.validateString("");
        }, "Validation did not throw ClassCastException.");
        assertThrows(ClassCastException.class, ()->{
            List<String> invalid = new ArrayList<String>();
            invalid.add("abc");
            Trie.validateString(invalid);
        }, "Validation did not throw ClassCastException.");
    }

    @Test
    @DisplayName("String Count Test")
    @Order(2)
    void stringCountTest() {
        assertEquals(ValueSource.ValidStringCount, this.trie.size(), "String count does not match");
    }

    @ParameterizedTest
    @Order(3)
    @DisplayName("Invalid String Put Test")
    @EnumSource(TrieTraversal.class)
    void invalidPut(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        assertThrows(IllegalArgumentException.class, ()->{
            this.trie.put(ValueSource.stringMap.get(ValueSource.Invalid), null);
        }, "Invalid insertion did not throw an IllegalArgumentException");
        assertEquals(ValueSource.ValidStringCount, this.trie.size(), "Stirng count did not match after invalid insertion success");
    }

    @Test
    @Order(4)
    @DisplayName("Empty String Put Test")
    void emptyStringPut() {
        assertNull(this.trie.put(ValueSource.stringMap.get(ValueSource.Empty), ""), "Empty string put did not return null.");
        assertEquals(ValueSource.ValidStringCount, this.trie.size(), "String changed after empty string put.");
    }

    @Test
    @Order(5)
    @DisplayName("Null String Put Test")
    void nullStringPut() {
        assertThrows(NullPointerException.class, ()->{
            this.trie.put(null, null);
        }, "Null string put did not throw NullPointerException.");
        assertEquals(ValueSource.ValidStringCount, this.trie.size(), "String changed after null string put.");
    }

    @ParameterizedTest
    @Order(6)
    @DisplayName("Contains Key Test")
    @EnumSource(TrieTraversal.class)
    void containsKeyTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        assertTrue(this.trie.containsKey(ValueSource.stringMap.get(ValueSource.Valid)), "Contains key test failed with random indexed string");
        assertFalse(this.trie.containsKey(ValueSource.stringMap.get(ValueSource.Absent)), "Contains key test failed with absent string");
        assertFalse(this.trie.containsKey(ValueSource.stringMap.get(ValueSource.Empty)), "Contains key test failed with empty string");
        assertThrows(IllegalArgumentException.class, ()->{
            this.trie.containsKey(ValueSource.stringMap.get(ValueSource.Invalid));
        }, "Invalid string contains test did not throw an exception");
    }

    @Test
    @Order(7)
    @DisplayName("Contains Null Test")
    void containsNullStringTest() {
        assertThrows(NullPointerException.class, ()->{
            this.trie.containsKey(null);
        }, "Null string contains key test did not throw a NullPointerException.");
    }

    @ParameterizedTest
    @Order(8)
    @DisplayName("Get Test")
    @EnumSource(TrieTraversal.class)
    void getTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        assertNotNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Valid)), "Valid string's data is null");
        assertEquals(ValueSource.dataMap.get(ValueSource.Valid), this.trie.get(ValueSource.stringMap.get(ValueSource.Valid)), "Valid string's data does not match in search test");

        assertNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Absent)), "Absent string's data is not null");

        assertNotNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Sub)), "Sub-string's data is null");
        assertEquals(ValueSource.dataMap.get(ValueSource.Sub), this.trie.get(ValueSource.stringMap.get(ValueSource.Sub)), "Sub-string's data does not match in search test");

        assertNotNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Super)), "Super-string's data is null");
        assertEquals(ValueSource.dataMap.get(ValueSource.Super), this.trie.get(ValueSource.stringMap.get(ValueSource.Super)), "Super-string's data does not match in search test");

        assertNotNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Extended1)), "Extended1 string's data is null");
        assertEquals(ValueSource.dataMap.get(ValueSource.Extended1), this.trie.get(ValueSource.stringMap.get(ValueSource.Extended1)), "Extended1 string's data does not match in search test");

        assertNotNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Extended2)), "Extended2 string's data is null");
        assertEquals(ValueSource.dataMap.get(ValueSource.Extended2), this.trie.get(ValueSource.stringMap.get(ValueSource.Extended2)), "Extended2 string's data does not match in search test");

        assertNotNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Common)), "Common string's data is null");
        assertEquals(ValueSource.dataMap.get(ValueSource.Common), this.trie.get(ValueSource.stringMap.get(ValueSource.Common)), "Common string's data does not match in search test");

        assertNotNull(this.trie.get(ValueSource.stringMap.get(ValueSource.UnitLength)), "UnitLength string's data is null");
        assertEquals(ValueSource.dataMap.get(ValueSource.UnitLength), this.trie.get(ValueSource.stringMap.get(ValueSource.UnitLength)), "UnitLength string's data does not match in search test");

        assertThrows(IllegalArgumentException.class, ()->{
            this.trie.get(ValueSource.stringMap.get(ValueSource.Invalid));
        }, "Invalid string search did not throw an exception");
    }

    @Test
    @Order(9)
    @DisplayName("Get Null String Test")
    void getNullStringTest(){
        assertThrows(NullPointerException.class, ()->{
            this.trie.get(null);
        }, "Null string get test did not throw NullPointerException.");
    }

    @Test
    @Order(10)
    @DisplayName("Empty String Remove Test")
    void emptyStringGet() {
        assertNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Empty)), "Empty string get did not return null.");
    }

    @ParameterizedTest
    @Order(11)
    @DisplayName("Invalid String Remove Test")
    @EnumSource(TrieTraversal.class)
    void invalidStringRemoveTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        assertThrows(IllegalArgumentException.class, ()->{
            this.trie.remove(ValueSource.stringMap.get(ValueSource.Invalid));
        }, "Invalid string remove did not throw an exception");
        assertEquals(ValueSource.ValidStringCount, this.trie.size(), "String count not matching after invalid string remove");
    }

    @ParameterizedTest
    @Order(12)
    @DisplayName("Absent String Remove Test")
    @EnumSource(TrieTraversal.class)
    void absentStringRemoveTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        this.trie.remove(ValueSource.stringMap.get(ValueSource.Absent));
        assertEquals(ValueSource.ValidStringCount, this.trie.size(), "String count not matching after invalid string remove");
    }

    @Test
    @Order(13)
    @DisplayName("Empty String Remove Test")
    void emptyStringRemove() {
        assertNull(this.trie.remove(ValueSource.stringMap.get(ValueSource.Empty)), "Empty string remove did not return null.");
        assertEquals(ValueSource.ValidStringCount, this.trie.size(), "Trie size changed after empty string remove.");
    }

    @Test
    @Order(14)
    @DisplayName("Null String Remove Test")
    void removeNullString() {
        assertThrows(NullPointerException.class, ()->{
            this.trie.remove(null);
        }, "Null remove did not throw NullPointerException.");
        assertEquals(ValueSource.ValidStringCount, this.trie.size(), "Trie size changed after null string remove.");
    }

    @ParameterizedTest
    @Order(15)
    @DisplayName("Valid String Remove Test")
    @EnumSource(TrieTraversal.class)
    void validStringRemoveTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        assertEquals(ValueSource.dataMap.get(ValueSource.Valid), this.trie.remove(ValueSource.stringMap.get(ValueSource.Valid)), "String deletion returned valid data.");
        assertEquals(ValueSource.ValidStringCount-1, this.trie.size(), "String count not matching after valid string remove");
        assertNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Valid)), "Valid string's data not null after remove");
    }

    @ParameterizedTest
    @Order(16)
    @DisplayName("Sub-String Remove Test")
    @EnumSource(TrieTraversal.class)
    void subStringRemoveTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        this.trie.remove(ValueSource.stringMap.get(ValueSource.Sub));
        assertEquals(ValueSource.ValidStringCount-1, this.trie.size(), "String count not matching after sub-string remove");
        assertNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Sub)), "Sub-string's data not null after remove");

        // Super-string validation
        assertEquals(ValueSource.dataMap.get(ValueSource.Super), this.trie.get(ValueSource.stringMap.get(ValueSource.Super)), "Super-string not found after sub-string remove");
    }

    @ParameterizedTest
    @Order(17)
    @DisplayName("Super-String Remove Test")
    @EnumSource(TrieTraversal.class)
    void superStringRemoveTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        this.trie.remove(ValueSource.stringMap.get(ValueSource.Super));
        assertEquals(ValueSource.ValidStringCount-1, this.trie.size(), "String count not matching after super-string remove");
        assertNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Super)), "Super-string's data not null after remove");

        // Super-string validation
        assertEquals(ValueSource.dataMap.get(ValueSource.Sub), this.trie.get(ValueSource.stringMap.get(ValueSource.Sub)), "Sub-string not found after sub-string remove");
    }

    @ParameterizedTest
    @Order(18)
    @DisplayName("Common Sub-String Remove Test")
    @EnumSource(TrieTraversal.class)
    void commonSubStringRemoveTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        this.trie.remove(ValueSource.stringMap.get(ValueSource.Common));
        assertEquals(ValueSource.ValidStringCount-1, this.trie.size(), "String count not matching after common sub-string remove");
        assertNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Common)), "Common string's data not null after remove");

        // Super-string validation
        assertEquals(ValueSource.dataMap.get(ValueSource.Extended1), this.trie.get(ValueSource.stringMap.get(ValueSource.Extended1)), "Extended1 not found after sub-string remove");
        assertEquals(ValueSource.dataMap.get(ValueSource.Extended2), this.trie.get(ValueSource.stringMap.get(ValueSource.Extended2)), "Extended2 not found after sub-string remove");
    }

    @ParameterizedTest
    @Order(19)
    @DisplayName("Extended String Remove Test")
    @EnumSource(TrieTraversal.class)
    void extendedStringRemoveTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        this.trie.remove(ValueSource.stringMap.get(ValueSource.Extended1));
        assertEquals(ValueSource.ValidStringCount-1, this.trie.size(), "String count not matching after common extended1 string remove");
        assertNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Extended1)), "Extended1 string's data not null after remove");

        // Super-string validation
        assertEquals(ValueSource.dataMap.get(ValueSource.Common), this.trie.get(ValueSource.stringMap.get(ValueSource.Common)), "Common not found after sub-string remove");
        assertEquals(ValueSource.dataMap.get(ValueSource.Extended2), this.trie.get(ValueSource.stringMap.get(ValueSource.Extended2)), "Extended2 not found after sub-string remove");
    }

    @ParameterizedTest
    @Order(20)
    @DisplayName("Unit Length String Remove Test")
    @EnumSource(TrieTraversal.class)
    void unitStringRemoveTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        this.trie.remove(ValueSource.stringMap.get(ValueSource.UnitLength));
        assertEquals(ValueSource.ValidStringCount-1, this.trie.size(), "String count not matching after unit length string remove");
        assertNull(this.trie.get(ValueSource.stringMap.get(ValueSource.UnitLength)), "Unit length string's data not null after remove");
    }

    @ParameterizedTest
    @Order(21)
    @DisplayName("Key-Set Test")
    @EnumSource(TrieTraversal.class)
    void keySetTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);

        // Expected key-set
        Set<List<TrieCharacter>> expectedSet = new HashSet<>(ValueSource.stringMap.size());
        expectedSet.addAll(ValueSource.stringMap.values());
        Set<List<TrieCharacter>> actual = this.trie.keySet();

        assertEquals(ValueSource.ValidStringCount, actual.size(), "Expected key-set size not matching with actual size");
        for(List<TrieCharacter> string : actual) {
            assertTrue(expectedSet.contains(string), "");
        }
    }

    @ParameterizedTest
    @Order(22)
    @DisplayName("Values Test")
    @EnumSource(TrieTraversal.class)
    void valuesTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);

        // Expected key-set
        Collection<String> expected = new ArrayList<>(ValueSource.stringMap.size());
        Collection<String> actual = this.trie.values();
        expected.addAll(ValueSource.dataMap.values());
        assertEquals(ValueSource.ValidStringCount, actual.size(), "Expected values not matching with actual");
        for(String data : actual) {
            assertTrue(expected.contains(data), "Data [" + data + "] is not contained in original collection.");
        }
    }

    @ParameterizedTest
    @Order(23)
    @DisplayName("Entry-Set Test")
    @EnumSource(TrieTraversal.class)
    void entrySetTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);

        // Expected key-set
        Set<Map.Entry<List<TrieCharacter>, String>> expected = new HashSet<>(ValueSource.stringMap.size());
        Set<Map.Entry<List<TrieCharacter>, String>> actual = this.trie.entrySet();
        for(int i=0; i<ValueSource.ValidStringCount; i++) {
            expected.add(new AbstractMap.SimpleEntry<List<TrieCharacter>, String>(ValueSource.stringMap.get(i), ValueSource.dataMap.get(i)));
        }
        assertEquals(ValueSource.ValidStringCount, actual.size(), "Expected entry-set not matching with actual");
        for(Map.Entry<List<TrieCharacter>, String> entry : actual) {
            assertTrue(expected.contains(entry), "Entry-set not contained in expected entry-set.");
        }
    }

    @Test
    @DisplayName("Trie Clear Test")
    @Order(24)
    void trieClearTest() {
        this.trie.clear();
        assertAll(
            ()->{ assertEquals(0, this.trie.size(), "String count does not match"); }, 
            ()->{ assertNull(this.trie.get(ValueSource.stringMap.get(ValueSource.Valid)), "Valid string get returned a non-null value after trie clear."); },
            ()->{ assertFalse(this.trie.containsKey(ValueSource.stringMap.get(ValueSource.Valid)), "Valid string search returned true after trie clear."); },
            ()->{ assertFalse(this.trie.containsValue(ValueSource.dataMap.get(ValueSource.Valid)), "Valid data search returned true after trie clear."); },
            ()->{ assertTrue(this.trie.isEmpty(), "Is empty method returned false after trie clear.");},
            ()->{ assertEquals(0, this.trie.size(), "Trie size is not 0 after trie clear."); },
            ()->{ assertEquals(0, this.trie.keySet().size(), "Trie key-set size is not 0 after trie clear."); },
            ()->{ assertEquals(0, this.trie.values().size(), "Trie values collection size is not 0 after trie clear."); },
            ()->{ assertEquals(0, this.trie.entrySet().size(), "Trie entry-set size is not 0 after trie clear."); }
        );
    }

    @Test
    @DisplayName("Emptiness Test")
    @Order(25)
    void isEmptyTest() {
        assertFalse(this.trie.isEmpty(), "Emptiness test failed when trie contains data");
    }

    @ParameterizedTest
    @Order(26)
    @DisplayName("Put All Test")
    @EnumSource(TrieTraversal.class)
    void putAllTest(TrieTraversal traversal) {
        this.trie.clear();
        assertTrue(this.trie.isEmpty(), "Put all test failed because isEmpty failed");
        Map<List<TrieCharacter>, String> map = new HashMap<>(ValueSource.stringMap.size());
        for(int i=0; i<ValueSource.ValidStringCount; i++) {
            map.put(ValueSource.stringMap.get(i), ValueSource.dataMap.get(i));
        }
        this.trie.putAll(map);

        assertEquals(ValueSource.ValidStringCount, this.trie.size(), "Trie size not matching to valid strings count after put all");
        int randomIndex = RandomUtils.nextInt(0, ValueSource.ValidStringCount);
        assertEquals(ValueSource.dataMap.get(randomIndex), this.trie.get(ValueSource.stringMap.get(randomIndex)), "Random string search failed after put all");
    }

    @ParameterizedTest
    @Order(27)
    @DisplayName("Contains Value Test")
    @EnumSource(TrieTraversal.class)
    void containsValueTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        assertTrue(this.trie.containsValue(ValueSource.dataMap.get(RandomUtils.nextInt(0, ValueSource.ValidStringCount))), "Contains value test failed with random indexed string");
        assertFalse(this.trie.containsValue(ValueSource.dataMap.get(ValueSource.Absent)), "Contains value test failed with absent string");

        assertThrows(IllegalArgumentException.class, ()->{
            this.trie.containsKey(ValueSource.stringMap.get(ValueSource.Invalid));
        }, "Invalid string contains test did not throw an exception");
    }

    @ParameterizedTest
    @Order(28)
    @DisplayName("Overwrite Allowed Test")
    @EnumSource(TrieTraversal.class)
    void overwriteAllowedTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        this.trie.setOverwriteAllowed(true);
        int randomIndex = RandomUtils.nextInt(0, ValueSource.ValidStringCount);
        this.trie.put(ValueSource.stringMap.get(randomIndex), "");
        assertEquals("", this.trie.get(ValueSource.stringMap.get(randomIndex)),"String not overwritten when overwrite is allowed");
    }

    @ParameterizedTest
    @Order(29)
    @DisplayName("Overwrite Not Allowed Test")
    @EnumSource(TrieTraversal.class)
    void overwriteNotAllowedTest(TrieTraversal traversal) {
        this.trie.setTraversal(traversal);
        this.trie.setOverwriteAllowed(false);
        int randomIndex = RandomUtils.nextInt(0, ValueSource.ValidStringCount);
        this.trie.put(ValueSource.stringMap.get(randomIndex), "");
        assertEquals(ValueSource.dataMap.get(randomIndex), this.trie.get(ValueSource.stringMap.get(randomIndex)),"String overwritten when overwrite is not allowed");
    }

    /**
     * Converts a string to a list of TrieCharacter.
     * @param str The string to be processed.
     * @return Returns a new Trie string.
     */
    List<TrieCharacter> convertToTrieString(String str) {
        List<TrieCharacter> trieStr= new ArrayList<>(str.length());
        for(int i=0; i<str.length(); i++) {
            trieStr.add(new TestCharacter(str.charAt(i)));
        }
        return trieStr;
    }
}