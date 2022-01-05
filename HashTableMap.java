// --== CS400 File Header Information ==--
// Name: Mayank Nayak
// Email: mnayak2@wisc.edu
// Notes to Grader: <optional extra notes>


import java.util.NoSuchElementException;
import java.util.LinkedList;

/**
 * Stores key-value pair
 * 
 * @author Mayank Nayak
 *
 * @param <T>
 * @param <S>
 */
class Pair<T, S> {
  // store KeyType and ValueType data from HashTableMap
  protected T type1;
  protected S type2;

  public Pair(T type1, S type2) {
    this.type1 = type1;
    this.type2 = type2;

  }
}


/**
 * Implements the Hash Table data structure with associated methods
 * 
 * @author Mayank Nayak
 *
 * @param <KeyType>
 * @param <ValueType>
 */
public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

  private int capacity;
  private int size = 0;
  // stores current load of hashTable
  private double loadFactor;
  @SuppressWarnings("rawtypes")
  private LinkedList[] hashTable;

  /**
   * Constructor with specified capacity for hashTable array
   * 
   * @param capacity
   */
  public HashTableMap(int capacity) {
    this.capacity = capacity;
    hashTable = new LinkedList[capacity];
  }

  /**
   * Constructor with default capacity 10 for hashTable array
   */
  public HashTableMap() {
    this.capacity = 10;
    hashTable = new LinkedList[10];
  }


  @SuppressWarnings("unchecked")
  @Override
  /**
   * Returns true if key-value pair is successfully added to hashTable array
   * 
   * @return true if add is successful, false otherwise
   */
  public boolean put(KeyType key, ValueType value) {
    if (key == null || containsKey(key)) {
      return false;
    }

    loadFactor = (double) (size() + 1) / capacity;
    // checks if current loadFactor is greater than threshold
    if (Double.compare(loadFactor, 0.85) >= 0) {
      resize();
    }

    int index = Math.abs(key.hashCode()) % capacity;

    // adds LinkedList element containing key-value pair to hashTable
    LinkedList<Pair<KeyType, ValueType>> list;
    // first checks if current index is null
    if (hashTable[index] == null) {
      // if null, then create new linked list for that index and insert new key-value pair
      list = new LinkedList<Pair<KeyType, ValueType>>();
      hashTable[index] = list;
      list.addFirst(new Pair<KeyType, ValueType>(key, value));
    } else {
      // otherwise just add new key-value pair in the linked list at the calculated index
      list = (LinkedList<Pair<KeyType, ValueType>>) hashTable[index];
      list.addFirst(new Pair<KeyType, ValueType>(key, value));
    }
    size++;
    
    return true;
  }

  @SuppressWarnings("unchecked")
  /**
   * Double capacity of hashTable and rehashes when loadFactor threshold is met
   */
  private void resize() {
    @SuppressWarnings("rawtypes")
    // new array to store key-value pairs
    LinkedList[] newTable = new LinkedList[capacity * 2];

    // rehashing by going through old hashTable array
    for (int i = 0; i < capacity; ++i) {
      if (hashTable[i] != null) {
        for (int j = 0; j < hashTable[i].size(); ++j) {
          // extracts all key-value pairs from old array to put into new array
          Pair<KeyType, ValueType> pair = (Pair<KeyType, ValueType>) hashTable[i].get(j);
          KeyType key = pair.type1;
          ValueType value = pair.type2;
          int index = Math.abs(key.hashCode()) % (capacity * 2);

          // exact same process to input into hash table as the put() method
          LinkedList<Pair<KeyType, ValueType>> list;
          if (newTable[index] == null) {
            list = new LinkedList<Pair<KeyType, ValueType>>();
            newTable[index] = list;
            list.addFirst(new Pair<KeyType, ValueType>(key, value));

          } else {
            list = (LinkedList<Pair<KeyType, ValueType>>) newTable[index];
            list.addFirst(new Pair<KeyType, ValueType>(key, value));
          }
        }
      }
    }
    capacity *= 2;
    hashTable = newTable;

  }

  @Override
  /**
   * Returns value associated with a unique key in the hashTable
   * 
   * @return ValueType value - associated with key
   */
  public ValueType get(KeyType key) throws NoSuchElementException {
    if (containsKey(key)) {
      int index = Math.abs(key.hashCode()) % capacity;
      for (int j = 0; j < hashTable[index].size(); ++j) {
        @SuppressWarnings("unchecked")
        // gets key-value pair
        Pair<KeyType, ValueType> pair = (Pair<KeyType, ValueType>) hashTable[index].get(j);
        // if user provided key and key in hashTable match, return the value
        if (pair.type1.equals(key)) {
          return pair.type2;
        }
      }
    }
    throw new NoSuchElementException();
  }

  @Override
  /**
   * Returns current number of objects/elements in the hash table
   * 
   * @return int size - elements in hash table
   */
  public int size() {
    return size;
  }

  @Override
  /**
   * Checks if the hashTable array contains a certain key
   * 
   * @return true if key is found, false otherwise
   */
  public boolean containsKey(KeyType key) {
    int index = Math.abs(key.hashCode()) % capacity;
    // iterates through the linked list associated with the key's index and checks if key is found
    if (hashTable[index] != null) {
      for (int j = 0; j < hashTable[index].size(); ++j) {
        @SuppressWarnings("unchecked")
        Pair<KeyType, ValueType> pair = (Pair<KeyType, ValueType>) hashTable[index].get(j);
        if (pair.type1.equals(key)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  /**
   * Removes key-value pair from hashTable array
   */
  public ValueType remove(KeyType key) {
    if (containsKey(key)) {
      int index = Math.abs(key.hashCode()) % capacity;
      // iterates through the linked list at the index associated with key
      for (int j = 0; j < hashTable[index].size(); ++j) {
        @SuppressWarnings("unchecked")
        Pair<KeyType, ValueType> pair = (Pair<KeyType, ValueType>) hashTable[index].get(j);
        // compares key values
        if (pair.type1.equals(key)) {
          // removes key-value pair at a particular index in the linked list
          hashTable[index].remove(j);
          size--;
          return pair.type2;
        }
      }
    }
    return null;
  }



  @Override
  /**
   * Clears all key-value pairs stored in this collection
   */
  public void clear() {
    // creates new hash table array
    hashTable = new LinkedList[capacity];
    size = 0;

  }

  @SuppressWarnings("unchecked")
  @Override
  /**
   * Converts objects stored in hash table to formatted String
   * @return String formatted String of hash table
   */
  public String toString() {
    if (size() == 0) {
      return "";
    }

    String returnString = "";
    int index = 0;

    while (index < hashTable.length) {
      if (hashTable[index] != null) {
        LinkedList<Pair<KeyType, ValueType>> currentList = hashTable[index];
        for (Pair<KeyType, ValueType> pair : currentList) {
          returnString += pair.type2.toString() + "\n";
        }
      }
      index++;
    }

    

    return returnString;
  }




}
