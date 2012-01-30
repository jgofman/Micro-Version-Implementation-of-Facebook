// Jacob Gofman
// Data Structures, Honors Section
// October 25th, 2011

public class Person
{
	// Variables for Person's name and list of friends
	private String name = "";
	private FriendsList<Person> friends;

	// Constructor Method
	public Person(String n)
	{
		this.name = n;
		// Instantiates doubly linked list of type Person, containing Person objects
		this.friends = new FriendsList<Person>();
	}

	// Returns Person's name
	public String getName() { return name; }

	// Sets Person's name to n
	public void setName(String n) {	this.name = n; }

	// Returns doubly linked list of friends
	public FriendsList<Person> getFriends() { return friends; }

	// Adds friend P to the top of this Person's list of friends
	public void addFriend(Person P) { friends.addFirst(P); }

	// Removes friend P from this Person's list of friends (Linear time - Regular Assignment)
	public void removeFriend(Person P) { friends.delete(P); }

	// Removes friend P from this Person's list of friends (Constant time - Honors Assignment)
	public void removeFriend(Link<Person> P) { friends.delete(P); }
}

class FriendsList<T>
{
	// Variables for first and last Links in doubly linked list
	private Link<T> first;
	private Link<T> last;

	// Constructor Method
	public FriendsList()
	{
		first = null;
		last = null;
	}

	// Checks if linked list is empty
	public boolean isEmpty() { return (first == null); }

	// Returns FIRST Link in linked list
	public Link<T> getFirst() { return first; }

	// Sets FIRST Link pointer in linked list to P
	public void setFirst(Link<T> P) { first = P; }

	// Returns LAST Link in linked list
	public Link<T> getLast() { return last; }

	// Sets LAST Link pointer in linked list to P
	public void setLast(Link<T> P) { last = P; }

	// Add AnyType P to the front of the linked list
	public void addFirst(T P)
	{
		Link<T> friend = new Link<T>(P);

		if (this.isEmpty())
		{ // If list is empty, set LAST pointer to FRIEND
			last = friend;
		} else { // Otherwise, set FRIEND to FIRST's "prev" field, and set Link in FIRST position to FRIEND's "next" field
			friend.setNext(first);
			first.setPrev(friend);
		}
		
		// Set Link FIRST pointer to FRIEND
		first = friend;
	}

	// Remove Link with the value=P from the linked list (Linear time - Regular Assignment)
	public void delete(T P)
	{ // I got the general idea from this and made it my own:
	  // http://www.java-forums.org/java-lang/7609-doubly-linked-list-data-structure.html
		
		// Create FRIEND of type Link with a pointer to the FIRST Link in the list
		Link<T> friend = first;

		// Run through friends to find a match for the Link specified for deletion
		while (friend.getValue() != P) 
		{
			friend = friend.getNext();
			if (friend == null) break; // Stops if it reaches the end
		}

		if (friend.getValue().equals(this.getFirst().getValue()))
		{ // If Person in Link FRIEND matches Person in Link FIRST position,
		  // set FRIEND in the "next" Link position to the FIRST position instead.
			first = friend.getNext();
		} else { // Otherwise, point previous FRIEND's "next" field to the 
				 // original FRIEND's next FRIEND.
			friend.getPrev().setNext(friend.getNext());
		}

		if (friend.getValue().equals(this.getLast().getValue()))
		{ // If Person in Link FRIEND matches Person in Link LAST position,
		  // set FRIEND in the "prev" Link position to the LAST position instead.
			last = friend.getPrev();
		} else { // Otherwise, point next FRIEND's "prev" field to the 
			 	 // original FRIEND's previous FRIEND.
			friend.getNext().setPrev(friend.getPrev());
		}		
	}

	// Remove Link P from the linked list (Constant time - Honors Assignment)
	public void delete(Link<T> P)
	{ // I got the general idea from MyList2.java posted on the class page
		
		if (P == first)
		{ // If Link P matches Link FIRST, set next Link to the FIRST position instead.
			first = P.getNext();
		} else { // Otherwise, point Link P's previous Link's "next" field to the Link P's next Link.
			P.getPrev().setNext(P.getNext());
		}

		if (P == last)
		{ // If Link P matches Link LAST, set previous Link to the LAST position instead.
			last = P.getPrev();
		} else { // Otherwise, point Link P's next Link's "prev" field to the Link P's previous Link.
			P.getNext().setPrev(P.getPrev());
		}
	}
}

class Link<T>
{
	// Variables for "next" and "prev" Links
	private Link<T> next;
	private Link<T> prev;
	private T value;

	// Constructor Method
	public Link(T P) 
	{
		this.next = null;
		this.prev = null;
		this.value = P;
	}

	// Returns value field of Link
	public T getValue() { return value; }

	// Sets value to X
	public T setValue(T X) { return value = X; }

	// Returns next Link
	public Link<T> getNext() { return next; }

	// Sets next Link to P
	public Link<T> setNext(Link<T> P) { return next = P; }

	// Returns previous Link
	public Link<T> getPrev() { return prev; }

	// Sets previous Link to P
	public Link<T> setPrev(Link<T> P) { return prev = P; }
}


