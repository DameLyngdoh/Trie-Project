# Trie for Java

This project is a **Trie Data Structure** implementation in Java. The trie data-structure extends the `Map` abstract class. This is because the data-structure is viewed as a dictionary where the string is looked up and some data associated with the string is returned. But this implementation is not restricted to only strings of ASCII characters or even characters at all. Rather, this implementation can be a string of any object which extends the `TrieCharacter` abstract class. The purpose for this abstract class is so that the two methods used for comparison of any two objects, `equals` and `hashCode` must be defined by the developer according to her/his requirements. The strings can be sequence of actual alphabet objects or numeric objects or paths to name a few use cases.

## References
To get a comprehensive elaboration on Tries, you can refer to these links below:
+ [https://www.hackerearth.com/practice/data-structures/advanced-data-structures/trie-keyword-tree/tutorial/](https://www.hackerearth.com/practice/data-structures/advanced-data-structures/trie-keyword-tree/tutorial/)
+ [https://www.topcoder.com/community/competitive-programming/tutorials/using-tries/](https://www.topcoder.com/community/competitive-programming/tutorials/using-tries/)
+ [https://www.interviewcake.com/concept/java/trie](https://www.interviewcake.com/concept/java/trie)
+ [https://www.toptal.com/java/the-trie-a-neglected-data-structure](https://www.toptal.com/java/the-trie-a-neglected-data-structure)

## Build and Install
### Requirements
+ JDK 1.8 (or higher)
+ Maven

### Building
```sh
git clone https://github.com/DameLyngdoh/Trie-Project
cd Trie-Project/trie
mvn install
```
Installs the package to your local maven repository.

### Install
+ Maven

```xml
<dependency>
	<groupId>io.damelyngdoh.java.trie</groupId>
	<artifactId>trie</artifactId>
	<version>1.0.0</version>
	<scope>compile</scope>
</dependency>
```

+ Gradle

```
dependencies {
	compile group: 'io.damelyngdoh.java', name: 'trie', version: '1.0.0'
}
```

## Documentation
The documentation was generated using [JavaDoc](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html) and can be found in `doc` directory. Open the `index.html` in a web-browser to view the documentation.

## Demo
Check out the [Dictionary Implementaion](https://github.com/DameLyngdoh/Trie-Project/tree/master/dictionarytriesample). 