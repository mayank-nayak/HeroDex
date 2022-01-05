import java.util.*;

/**
 * This class stores details about a specific Hero
 * @author mayanknayak
 *
 */
class Hero implements Comparable<Hero> {
  Scanner scnr;
  Random randInt = new Random();
  String name;
  Traits trait;
  int traitNumber;
  String description;
  int rating;

  /**
   * Constructor for newly created Heroes with user input
   * @param name
   * @param description
   * @param scnr
   */
  public Hero(String name, String description, Scanner scnr) {
    this.name = name;
    this.description = description;
    this.scnr = scnr;
    calculateRating();
    System.out.println(chooseTrait());
  }
  /**
   * Constructor for existing heroes already in HeroDex text file database
   * @param name
   * @param description
   * @param trait
   * @param traitNumber
   * @param rating
   */
  public Hero(String name, String description, Traits trait, int traitNumber, int rating) {
    this.name = name;
    this.description = description;
    this.trait = trait;
    this.traitNumber = traitNumber;
    this.rating = rating;
  }


  /**
   * Enumeration of all possible traits a hero may possess
   * @author mayanknayak
   *
   */
  public enum Traits {
    Strength, Intelligence, Speed, Stealth, Magic
  }

  /**
   * Assigns randomized rating to each hero
   */
  protected void calculateRating() {
    this.rating = randInt.nextInt(50) + 50;
  }

  /**
   * Enables the user to pick a certain trait for each Hero
   * @return Message to output
   */
  protected String chooseTrait() {
    System.out.println("Please enter desired number (1 - 5) to choose trait:");
    int n = 1;
    for (Traits trait : Traits.values()) {

      System.out.println(n++ + ": " + trait);
    }

    boolean validInput = false;
    int input = 0;
    
    // makes sure user inputs a valid input
    while (!validInput) {

      if (scnr.hasNextInt()) {
        input = scnr.nextInt();
      }

      if (input >= 1 && input <= 5) {
        validInput = true;
      }

      if (!validInput) {
        System.out.println("Please enter valid number between 1 to 5");
      }
    }
    // assigns appropriate trait to hero based on user input
    this.trait = Traits.values()[input - 1];
    this.traitNumber = input - 1;
    return "You chose " + Traits.values()[input - 1];
  }
  
  /**
   * Compares and assigns natural ordering for heroes
   */
  public int compareTo(Hero otherHero) {
    int compareValue = otherHero.rating - this.rating  ;

    return compareValue;

  }

  @Override
  /**
   * Converts the details of a hero to a formatted String
   */
  public String toString() {
    String returnString = "";
    returnString += "| Name: " + this.name + " | Trait: " + this.trait + " | Rating: " + this.rating
        + " |\n| Description: " + this.description + " |\n";
    return returnString;
  }
}
