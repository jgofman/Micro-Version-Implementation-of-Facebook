import java.util.Arrays;
import java.util.Scanner;
import java.util.Hashtable;

// Jacob Gofman
// Data Structures, Honors Section
// October 25th, 2011



public class MicroFB {
	// HashTable variables for directory of people and friendships
	public static Hashtable<String, Person> directory = new Hashtable<String, Person>();
	static Hashtable<String, Object> friendValues = new Hashtable<String, Object>();
	
	public static void main(String[] args) {
		boolean notDone = true;
		Scanner value = new Scanner(System.in);
		System.out.println("Welcome to JBook!");
		
		// Nav Menu Start
		while (notDone)
		{
			// Directions of possible commands
			System.out.println("\nP <name> - Create Person record with specified name.");
			System.out.println("F <name1> <name2> - Record that the 2 people are now friends.");
			System.out.println("U <name1> <name2> - Record that the 2 people are no longer friends.");
			System.out.println("L <name> - Print out friends of specified person.");
			System.out.println("Q <name1> <name2> - Check if the 2 people are friends.");
			System.out.println("X - Terminate the program.");
			System.out.println("\nWhat would you like to do? ");
			String input = value.nextLine();
			
			// Split input into words and capitalize them
			String[] commands = input.split("[\\s+]");
			commands = capitalize(commands);
			
			switch(commands[0].charAt(0))
			{
			case 'P':
				// Call method to create person if he/she doesn't already exist
				if (!checkExist(commands[1]))
				{
					P(commands[1]);
				} else {
					System.out.println("Record already exists.");
				}
				break;
			case 'F':
				// Call method to record friendship if both people exist, aren't the same person, and aren't already friends
				if(checkExist(commands[1]) && checkExist(commands[2]) && !commands[1].equals(commands[2]))
				{
					if (!Q(commands[1], commands[2]))
					{
						F(commands[1], commands[2]);
					} else {
						System.out.println(commands[1] + " and " + commands[2] + " are already friends.");
					}
				} else {
					System.out.println("\nCannot find people specified. Please create them in directory.");
				}
				break;
			case 'U':
				// Call method to record un-friendship if both people exist, aren't the same person, and are already friends
				if(checkExist(commands[1]) && checkExist(commands[2]) && !commands[1].equals(commands[2]))
				{
					if (Q(commands[1], commands[2]))
					{
						U(commands[1], commands[2]);
					} else {
						System.out.println(commands[1] + " and " + commands[2] + " are not yet friends.");
					}
				} else {
					System.out.println("\nCannot find people specified. Please create them in directory.");
				}
				break;
			case 'L': 
				// Call method to print the list of friends for the specified person, if he/she exists
				if(checkExist(commands[1]))
				{
					L(commands[1]);
				} else {
					System.out.println("\nCannot find person specified. Please create them in directory.");
				}
				break;
			case 'Q': 
				// Call method to check if the two people are friends if both people exist and aren't the same person
				if(checkExist(commands[1]) && checkExist(commands[2]) && (commands[1] != commands[2]))
				{
					if (Q(commands[1], commands[2]))
					{
						System.out.println(commands[1] + " and " + commands[2] + " are friends.");
					} else {
						System.out.println(commands[1] + " and " + commands[2] + " are not friends.");
					}
				} else {
					System.out.println("\nCannot find people specified. Please create them in directory.");
				}
				break;
			case 'X':
				// Terminate the program
				System.out.println("\nThank you for using JBook. Have a nice day!");
				System.exit(0);
			default: System.out.println("\nI'm sorry but that input was not recognized, please try again: ");
			}
		}
		// Nav Menu End
	}
	
	// Capitalizes strings to standardize commands to reduce code errors
	public static String[] capitalize(String[] s)
	{
		// Make the first character UpperCase in every word in the String Array
		for (int i = 0; i < s.length; i++)
		{
			s[i] = s[i].substring(0,1).toUpperCase() + s[i].substring(1).toLowerCase();
		}
		
		return s;
	}
	
	// Checks if Person exists in directory
	public static boolean checkExist(String name) { return directory.containsKey(name); }
	
	// Creates Person from specified name and inputs Person into directory
	public static void P(String name)
	{		
		Person person1 = new Person(name);
		directory.put(name, person1);
		System.out.println("Record created for " + name + ".");
	}

	// Records that the two people are friends
	public static void F(String name1, String name2)
	{
		// Create two-person key
		String[] names = {name1, name2};
		Arrays.sort(names);
		String twoPersonKey = names[0] + "*" + names[1];
		
		// Get Person objects from the directory (alphabetical order)
		Person person1 = directory.get(names[0]);
		Person person2 = directory.get(names[1]);
		
		// Add each person to top of each others' friend lists
		person1.addFriend(person2);
		person2.addFriend(person1);	
		TwoNamePointer obj = new TwoNamePointer(person1.getFriends().getFirst(), 
												person2.getFriends().getFirst());
		
		// Add friendship key to two-name HashTable
		friendValues.put(twoPersonKey, obj);
		
		System.out.println(name1 + " and " + name2 + " are now friends.");
	}
	
	// Records that the two people are no longer friends
	public static void U(String name1, String name2)
	{
		// Create two-person key
		String[] names = {name1, name2};
		Arrays.sort(names);
		String twoPersonKey = names[0] + "*" + names[1];
		
		// Get Link<Person> objects from the two-field object stored in friendValues under twoPersonKey
		Link<Person> nodeP1 = ((TwoNamePointer) friendValues.get(twoPersonKey)).getName1();
		Link<Person> nodeP2 = ((TwoNamePointer) friendValues.get(twoPersonKey)).getName2();
		
		// Get Person objects from the directory (alphabetical order)
		Person person1 = directory.get(nodeP1.getValue().getName());
		Person person2 = directory.get(nodeP2.getValue().getName());
		
		// Delete each Person from each other's friend list
		person1.removeFriend(nodeP2);
		person2.removeFriend(nodeP1);
		
		// Delete friendship key from two-name HashTable
		friendValues.remove(twoPersonKey);
		
		System.out.println(name1 + " and " + name2 + " are no longer friends.");
	}
	
	// Prints out the friends of the specified Person
	public static void L(String name)
	{
		// Get Person object from directory
		Person person1 = directory.get(name);
		// Create local variable with Person's list of friends
		FriendsList<Person> list = person1.getFriends();
		// Set variable friend to be the FIRST Link in the list
		Link<Person> friend = list.getFirst();
		// Initialize output
		String output = name + "'s Friends: ";

		if (friend == null)
		{ // If no person is FIRST, then list is empty
			output += "No friends";
		} else { 
			// Add name of FIRST Person on friends list to output
			output += friend.getValue().getName();
			friend = friend.getNext();
		}
		
		// Run through list of friends and add FRIEND's name to output
		while (friend != null)
		{
			output += (", " + friend.getValue().getName());
			friend = friend.getNext();
		}
		
		// Print list of friends for person1
		System.out.println("\n" + output + ".");
	}
	
	// Checks friendValues to see if the two people are friends
	public static boolean Q(String name1, String name2)
	{
		// Get Person objects from directory
		Person person1 = directory.get(name1);
		Person person2 = directory.get(name2);
		
		// Create a two person key
		String[] names = {person1.getName(), person2.getName()};
		Arrays.sort(names);
		String twoPersonKey = names[0] + "*" + names[1];
		
		// return boolean value representing the friendship
		return friendValues.containsKey(twoPersonKey);
	}
}

class TwoNamePointer // Class with 2 fields for input into friendValues
{
	// Variables for the Links of the 2 people
	private Link<Person> name1;
	private Link<Person> name2;
	
	// Constructor Method
	public TwoNamePointer(Link<Person> A, Link<Person> B)
	{
		name1 = A;
		name2 = B;
	}
	
	// Returns 1st Person's Link
	public Link<Person> getName1() { return name1; }
	
	// Sets 1st Person's Link to P
	public Link<Person> setName1(Link<Person> P) { return name1; }
	
	// Returns 2nd Person's Link
	public Link<Person> getName2() { return name2; }
	
	// Sets 2nd Person's Link to P
	public Link<Person> setName2(Link<Person> P) { return name2; }	
}

