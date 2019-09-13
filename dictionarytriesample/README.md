# Dictionary Implementation

This demo project demonstrates the usage of the Trie library as a dictoinary (an actual dictionary). The words in the dictionary will be in the form of a list of `Char` objects and `WordMetaData` will be the value object when a word is searched in the trie. The `WordMetaData` object contains the word in `String` and the parst of speech and the meaning of the word.

The data in the trie is initially loaded with some meta-data from the `source.csv` file which contains some words along with their parts of speech and meaning. Then some words are searched from the trie and the `WordMetaData` result is displayed in the console.

The code should be self-explanatory and the comments in the code should be sufficient to elaborate the steps within the code.

To view the console output, run the `DictionaryImplementation` file which contains the `main` method. 