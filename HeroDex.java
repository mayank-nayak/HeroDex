import java.util.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class maintains a database of Superheroes and their abilities with various access and
 * modification features.
 * 
 * @author mayanknayak
 *
 */
public class HeroDex {
  // hash table to store each hero
  HashTableMap<String, Hero> heroDex;

  // holds heroes based on their rating
  PriorityQueue<Hero> ratingQueue = new PriorityQueue<>();

  // hold the names of heroes for easy access and comparison of traits and ratings
  ArrayList<String> names = new ArrayList<>();

  /**
   * Constructor initializes hash table to store heroes
   */
  public HeroDex() {
    heroDex = new HashTableMap<>();
  }

  /**
   * Reads from a text file to initialize HeroDex hash table that will store heroes. Inputs from
   * text file each Heroe's abilities and instantiates a hero class for each one.
   * 
   * @param fileName Name of file to work on.
   * @throws IOException
   */
  public void initializeHeroDex(String fileName) throws IOException {
    Scanner fileScanner;

    File file = new File(fileName);

    file.createNewFile();

    fileScanner = new Scanner(file);


    while (fileScanner.hasNextLine()) {

      String name = fileScanner.nextLine();

      String description = fileScanner.nextLine();

      int traitNumber = fileScanner.nextInt();

      Hero.Traits trait = Hero.Traits.values()[traitNumber];
      int rating = fileScanner.nextInt();

      fileScanner.nextLine();
      Hero addHero = new Hero(name, description, trait, traitNumber, rating);
      heroDex.put(name, addHero);
      ratingQueue.add(addHero);
      names.add(name);

    }

    fileScanner.close();
  }

  /**
   * This is the method that enables the user to interact with the HeroDex interface and database.
   * This method calls upon various other methods to enable functionality.
   * 
   * @param scnr     Scanner instance to read from
   * @param fileName Name of file to work on.
   * @throws IOException
   */
  public void runHeroDex(Scanner scnr, String fileName) throws IOException {

    initializeHeroDex(fileName);

    // Scanner scnr = new Scanner(inputStream);
    System.out.println("Welcome to HeroDex - an encyclopedia of all of your favorite heroes!");
    int input = 0;
    do {

      System.out
          .println("What would you like to do? Enter a number correpsonding to the list below:\n");
      System.out.println("1: Add a new hero!\n" + "2: Get a list of all the heroes in the HeroDex\n"
          + "3: Filter and sort heroes\n" + "4: Delete all data in the HeroDex\n"
          + "5: Compare two different heroes and see who would win in a fight\n6: Quit");

      boolean valid = false;
      do {
        if (scnr.hasNextInt()) {
          input = scnr.nextInt();
          if (input > 0 && input < 7) {
            valid = true;
          }
        }
        if (!valid) {
          System.out.println("Not a valid input, input again...");
        }
        scnr.nextLine();
      } while (!valid);


      if (input == (1)) {
        if (heroDex.size() < 101) {
          option1(scnr, fileName);
        } else {
          System.out.println("HeroDex\n");
        }
      } else if (input == 2) {
        option2(scnr);
      } else if (input == 3) {
        option3(scnr);
      } else if (input == 4) {
        System.out.println("Are you sure you want to delete all data? Enter corresponding number.");
        System.out.println("1: Yes\n2: No");
        boolean validAnswer = false;
        int answer = 0;
        do {
          if (scnr.hasNextInt()) {
            answer = scnr.nextInt();
            if (answer > 0 && answer < 3) {
              validAnswer = true;
            }
          }
          if (!validAnswer) {
            System.out.println("Not a valid input, input again...");
          }
          scnr.nextLine();
        } while (!validAnswer);

        if (answer == 1)
          option4(fileName);

      } else if (input == 5) {
        option5(scnr);
      }
    } while (input != 6);
    scnr.close();

    System.out.println("Thanks for using HeroDex!");
  }

  /**
   * Adds a new hero to the HeroDex hash table and updates the text file
   * 
   * @param scnr     Instance of Scanner to input from
   * @param fileName File to work on
   * @throws IOException
   */
  private void option1(Scanner scnr, String fileName) throws IOException {
    String name;
    String description;

    // makes sure valid name is input
    do {
      System.out.println("Please enter your hero's name");
      name = scnr.nextLine();
    } while (!validateName(name));
    // makes sure valid description is input
    do {
      System.out.println("Please enter a description for your hero that is more than 5 words");
      description = scnr.nextLine();
    } while (!validateDescription(description));
    addHero(name, description, scnr);
    // updates and writes to text file
    FileWriter file = new FileWriter(fileName, true);
    PrintWriter writer = new PrintWriter(file);
    Hero currentHero = heroDex.get(name);
    writer.println(name);
    writer.println(description);
    writer.println(currentHero.traitNumber);
    writer.println(currentHero.rating);

    writer.close();

    System.out.println("\nYou have successfully added a new hero to the HeroDex!");
  }

  /**
   * Validates user input name
   * 
   * @param name
   * @return
   */
  private boolean validateName(String name) {
    // checks if hash table already contains a certain name
    if (heroDex.containsKey(name)) {
      System.out.println("Sorry but another hero already has that name, " + "identity theft "
          + "isn't a joke...\nEnter another one!");
      return false;
    }
    return true;
  }

  /**
   * Validates user input description
   * 
   * @param description User input description
   * @return
   */
  private boolean validateDescription(String description) {

    // makes sure description is not null and is longer than 5 words
    if (description == null || description.split(" ").length < 5) {
      System.out
          .println("Your description doesn't quite do your hero justice, " + "enter a longer one!");
      return false;
    }

    return true;
  }

  /**
   * Simple method that add new Hero instance to the data structures used in this class
   * 
   * @param name        User input name
   * @param description User input description
   * @param scnr        Instance of Scanner to use
   */
  public void addHero(String name, String description, Scanner scnr) {
    Hero newHero = new Hero(name, description, scnr);
    heroDex.put(name, newHero);
    ratingQueue.add(newHero);
    names.add(newHero.name);
  }


  /**
   * Prints out the list of all heroes stored in the HeroDex by calling the hash table's toString()
   * method.
   * 
   * @param scnr Instance of Scanner to use
   */
  private void option2(Scanner scnr) {
    if (getHeroesStored() == 1) {
      System.out.println("There is " + getHeroesStored() + " hero in the HeroDex!\n");
    } else {
      System.out.println("There are " + getHeroesStored() + " heroes in the HeroDex!\n");
    }
    System.out.println(heroDex.toString());
  }

  /**
   * Enables the user to sort Heroes by rating, filter by specific Trait, or search for a specific
   * Hero and get their details.
   * 
   * @param scnr
   * @return Return specific Hero that user desires, null if not found
   */
  private Hero option3(Scanner scnr) {
    boolean valid = false;
    int input = 0;

    System.out.println("Please choose an option below (Enter a number):\n1: Sort heroes by rating\n"
        + "2: Filter heroes by Trait\n3: Get details about a specific hero"
        + "\n4: Get a list of heroes above a certain rating");
    // validate correct user input
    do {
      if (scnr.hasNextInt()) {
        input = scnr.nextInt();
        if (input > 0 && input < 5) {
          valid = true;
        }
      }
      if (!valid) {
        System.out.println("Not a valid input, input again...");
      }
      scnr.nextLine();
    } while (!valid);

    // option 1
    if (input == 1) {
      if (ratingQueue.size() == 1) {
        System.out.println("There is " + getHeroesStored() + " hero in the HeroDex.\n");
      } else {
        System.out.println("There are " + getHeroesStored() + " heroes in the HeroDex.\n");
      }
      // output heroes in ascending order with regard to rating
      if (!ratingQueue.isEmpty()) {
        while (!ratingQueue.isEmpty()) {
          System.out.println(ratingQueue.remove().toString());
        }

        for (String heroName : names) {
          ratingQueue.add(getHero(heroName));
        }
      }


    // option 2
    } else if (input == 2) {
      System.out.println("Choose the trait (1 - 5) you would like to filter by:");
      int i = 1;
      for (Hero.Traits trait : Hero.Traits.values()) {
        System.out.println("" + i++ + ": " + trait);
      }

      boolean valid1 = false;
      int traitInput = 0;
      
      // validate user input
      do {
        if (scnr.hasNextInt()) {
          traitInput = scnr.nextInt();
          if (traitInput > 0 && traitInput < 6) {
            valid1 = true;
          }
        }
        if (!valid1) {
          System.out.println("Not a valid input, try again...");
        }
        scnr.nextLine();
      } while (!valid1);
      // get hash table of heroes matching the selected trait
      HashTableMap<String, Hero> heroesWithTrait = findMatchingTrait(traitInput);
      System.out.println(heroesWithTrait.toString());


      if (heroesWithTrait.size() == 1) {
        System.out
            .println(heroesWithTrait.size() + " hero was found " + "with a matching trait...\n");
      } else {
        System.out.println(
            heroesWithTrait.size() + " hero(es) were found " + "with a matching trait...\n");
      }
      
    // option 3
    } else if (input == 3) {
      System.out.println("Please enter hero name exactly and accurately...");
      String findHero = scnr.nextLine();
      // validate user input
      if (heroDex.containsKey(findHero)) {
        System.out.println(getHero(findHero).toString());
        return getHero(findHero);
      } else {
        System.out.println("There is no Hero matching that name.");
        return null;
      }
      
    // option 4
    } else if (input == 4) {
      System.out.println("Please enter minimum rating to filter by (50-99):");
      int ratingInput = -1;
      boolean ratingValid = false;
      // validate user input
      do {
        if (scnr.hasNextInt()) {
          ratingInput = scnr.nextInt();
          if (ratingInput > 49 && ratingInput < 100) {
            ratingValid = true;
          }
        }
        if (!ratingValid) {
          System.out.println("Not a valid input, try again...");
        }
        scnr.nextLine();
      } while (!ratingValid);

      int heroesFound = 0;
      // go through all heroes stored to check which heroes have a rating matching the criteria
      for (String name : names) {
        Hero currentHero = heroDex.get(name);
        if (currentHero.rating >= ratingInput) {
          heroesFound++;
          System.out.println(currentHero);
        }
      }
      if (heroesFound == 1) {
        System.out.println(heroesFound + " hero was found above " + ratingInput + " rating.\n");
      } else {
        System.out
            .println(heroesFound + " heroes were found above " + ratingInput + " rating.\n");
      }
    }
    return null;
  }

  /**
   * Finds all heroes matching a certain trait
   * 
   * @param traitInput Trait to match
   * @return Hash table containing all heroes with matching trait
   */
  private HashTableMap<String, Hero> findMatchingTrait(int traitInput) {
    HashTableMap<String, Hero> hashTable = new HashTableMap<>();

    // Goes through array list to find which Heroes possess a certain trait
    for (String name : names) {
      Hero hero = heroDex.get(name);
      if (hero.traitNumber == (traitInput - 1)) {
        hashTable.put(name, hero);
      }
    }

    return hashTable;
  }

  /**
   * Deletes all data related to the HeroDex
   * 
   * @param fileName
   */
  private void option4(String fileName) {
    clearAllData(fileName);
  }

  /**
   * Helps the user compare two different Heroes based on rating
   * 
   * @param scnr Instance of Scanner to use
   */
  private void option5(Scanner scnr) {
    String firstHeroName;
    String secondHeroName;
    System.out.println("Please enter first hero's name:");
    boolean valid = false;
    // makes sure valid hero name is input
    do {
      firstHeroName = scnr.nextLine();
      if (heroDex.containsKey(firstHeroName)) {
        valid = true;
      }
      if (!valid) {
        System.out.println("Not a valid input, try again...");
      }

    } while (!valid);

    valid = false;
    System.out.println("Please enter second hero's name:");
    // makes sure valid hero name is input
    do {
      secondHeroName = scnr.nextLine();
      if (heroDex.containsKey(secondHeroName) && !secondHeroName.equals(firstHeroName)) {
        valid = true;
      }
      if (!valid) {
        System.out.println("Not a valid input, try again...");
      }

    } while (!valid);

    // outputs result of comparison
    if (getHero(firstHeroName).compareTo(getHero(secondHeroName)) < 0) {
      System.out.println(firstHeroName + " would win this battle!");
    } else if (getHero(firstHeroName).compareTo(getHero(secondHeroName)) > 0) {
      System.out.println(secondHeroName + " would win this battle!");

    } else {
      System.out.println("This battle would be a tie!");
    }

    System.out.println(firstHeroName + " has a rating of " + getHero(firstHeroName).rating);
    System.out
        .println(secondHeroName + " has a rating of " + getHero(secondHeroName).rating + "\n");
  }

  /**
   * Returns hero matching a certain name
   * 
   * @param name User input name
   * @return
   */
  public Hero getHero(String name) {
    return heroDex.get(name);
  }

  /**
   * Returns number of heroes stored in the HeroDex hash table
   * 
   * @return Number of heroes stored
   */
  public int getHeroesStored() {
    return heroDex.size();
  }

  /**
   * Deletes associated hash table, priority queue, and ArrayList
   */
  private void clearHeroDex() {
    heroDex.clear();
    ratingQueue.clear();
    names.clear();
  }

  /**
   * Deletes all data including text file database
   * 
   * @param fileName
   */
  private void clearAllData(String fileName) {
    File file = new File(fileName);
    file.delete();
    clearHeroDex();
  }

  @Test
  /**
   * Verifies that HeroDex hashTable reads all data from the database text file correctly and
   * accurately.
   */
  public void jUnitTest1() {
    System.out.println("Start of Test 1\n---------------");

    HeroDex heroDex = new HeroDex();

    try {
      heroDex.initializeHeroDex("testFile2.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }

    assertEquals(heroDex.heroDex.size(), 9);
    assertEquals(heroDex.heroDex.containsKey("Normal man"), true);
    assertEquals(heroDex.heroDex.get("Normal man").description,
        "Just an ordinary Human, nothing too special here.");

    System.out.println("End of Test 1\n---------------\n");
  }

  @Test
  /**
   * Test to check if Hero is correctly added into the HeroDex hash table and checks if HeroDex is
   * correctly cleared of all data.
   * 
   * @throws FileNotFoundException
   */
  public void jUnitTest2() throws FileNotFoundException {
    System.out.println("Start of Test 2\n---------------");

    HeroDex heroDex = new HeroDex();
    heroDex.clearAllData("testFile.txt");
    Scanner scnr =
        new Scanner("1\nTest man\nThis is a hero that is being added for a test\n3\n6\n");
    try {
      heroDex.runHeroDex(scnr, "testFile.txt");

    } catch (IOException e) {

      System.out.println("Unexpected error");
    }

    assertEquals(true, heroDex.heroDex.containsKey("Test man"));

    assertEquals(heroDex.names.get(0), "Test man");

    assertEquals(heroDex.ratingQueue.remove().name, "Test man");

    heroDex.clearAllData("testFile.txt");

    assertEquals(new File("testFile.txt").exists(), false);
    assertEquals(heroDex.heroDex.size(), 0);
    assertEquals(heroDex.names.isEmpty(), true);
    assertEquals(heroDex.ratingQueue.isEmpty(), true);

    System.out.println("End of Test 2\n---------------\n");
  }

  @Test
  /**
   * Test to check if duplicate hero name inputs are correctly handled. Specifically checks if
   * validateName() method correctly identifies duplicate names/keys.
   */
  public void jUnitTest3() {
    System.out.println("Start of Test 3\n---------------");

    HeroDex heroDex = new HeroDex();
    try {
      heroDex.initializeHeroDex("testFile2.txt");
    } catch (IOException e) {
      e.printStackTrace();

    }


    assertEquals(heroDex.validateName("Superman"), false);

    System.out.println("End of Test 3\n---------------\n");
  }

  @Test
  /**
   * Checks if the correct heroes are filtered and output based on user input of Trait
   */
  public void jUnitTest4() {
    System.out.println("Start of Test 4\n---------------");

    HeroDex heroDex = new HeroDex();
    HashTableMap<String, Hero> traitTable = null;
    try {
      heroDex.initializeHeroDex("testFile2.txt");
      traitTable = heroDex.findMatchingTrait(1);

    } catch (Exception e) {
      e.printStackTrace();
    }

    assertEquals(traitTable.size(), 3);
    assertEquals(traitTable.containsKey("Superman"), true);
    assertEquals(traitTable.containsKey("Roidman"), true);
    assertEquals(traitTable.containsKey("Big Basketball Dude"), true);
    assertEquals(traitTable.containsKey("Batman"), false);

    System.out.println("End of Test 4\n---------------\n");
  }

  @Test
  /**
   * Checks if the correct Hero is retrieved when user wants details about a specific hero
   * 
   * @param args
   */
  public void jUnitTest5() {
    System.out.println("Start of Test 5\n---------------");

    HeroDex heroDex = new HeroDex();
    Scanner scnr = new Scanner("3\nSuperman\n");
    Hero foundHero = null;
    try {
      heroDex.initializeHeroDex("testFile2.txt");
      foundHero = heroDex.option3(scnr);
    } catch (Exception e) {
      e.printStackTrace();
    }

    assertEquals(foundHero.name, "Superman");
    assertEquals(foundHero.rating, 54);

    System.out.println("End of Test 5\n---------------\n");
  }


  /**
   * Calls runHeroDex() method for user to interact with the program
   * 
   * @param args
   */
  public static void main(String[] args) {
    Scanner scnr = new Scanner(System.in);
    HeroDex heroDex = new HeroDex();
    try {
      heroDex.runHeroDex(scnr, "heroDex.txt");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
