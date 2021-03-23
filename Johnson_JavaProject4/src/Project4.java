
/*
 * Author: Blake Johnson
 * Creaded: 11/19/2020
 * 
 * 1-Make a random-access file
 * 2-Display a random-access file
 * 3-Build the index
 * 4-Display the index
 * 5-Retrieve a record
 * 6-Modify a record
 * 7-Add a new record
 * 8-Delete a record
 * 9-Exit
 * 
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Project4 {

	public static void main(String[] args) throws IOException {

		Scanner Kb = new Scanner(System.in);
		RandomAccessFile ranAccFile = null;
		AVLTree3<Pair<Integer>> pairTree = null;

		String menuCon = ""; // loop control
		int deleteNum = 1; // number of items deleted
		String fileName = null;

		// do-While loop to return the user to the main menu
		do {
			try {
				// Setting up interface
				System.out.println("1: Create a random-access file");
				System.out.println("2: Display Contents");
				System.out.println("3: Build the index");
				System.out.println("4: Display the index");
				System.out.println("5: Retrieve a record");
				System.out.println("6: Modify a record");
				System.out.println("7: Add a new record");
				System.out.println("8: Delete a record");
				System.out.println("9: Exit");
				System.out.print("Enter a number [1-9]: ");
				int menuSlect = Kb.nextInt();
				System.out.println();

				// Choosing option based on User input
				if (menuSlect == 1) {
					ranAccFile = createRAF();
				} else if (menuSlect == 2) {
					System.out.print("Enter random access file name: "); // asking user for file name
					fileName = Kb.next();
					ranAccFile = new RandomAccessFile(fileName, "rw"); // retrieving Random Access File
					display(ranAccFile); // Display Content
				} else if (menuSlect == 3) {
					System.out.print("Enter random access file name: "); // asking user for file name
					fileName = Kb.next();
					ranAccFile = new RandomAccessFile(fileName, "rw"); // retrieving Random Access File
					pairTree = buildIndex(ranAccFile); // building index
				} else if (menuSlect == 4) {
					ranAccFile = new RandomAccessFile(fileName, "rw"); // retrieving Random Access File
					displayIndex(pairTree, ranAccFile); // displays index
				} else if (menuSlect == 5) {
					ranAccFile = new RandomAccessFile(fileName, "rw"); // retrieving Random Access File
					retrieveRecord(pairTree, ranAccFile); // Retrieves a record
				} else if (menuSlect == 6) {
					ranAccFile = new RandomAccessFile(fileName, "rw"); // retrieving Random Access File
					modifyRecord(pairTree, ranAccFile); // modifies a record
				} else if (menuSlect == 7) {
					ranAccFile = new RandomAccessFile(fileName, "rw"); // retrieving Random Access File
					addRecord(pairTree, ranAccFile); // adds record
				} else if (menuSlect == 8) {
					ranAccFile = new RandomAccessFile(fileName, "rw"); // retrieving Random Access File
					deleteRecord(pairTree, ranAccFile); // deletes record
				} else if (menuSlect == 9) {
					menuCon = "Exit"; // Exits the loop
				} else if (menuSlect > 9) {
					System.out.println("The menu option you have choosen is not available! "); // when the user chooses an invalid option
					System.out.println();
				}
			} catch (InputMismatchException e) {
				System.out.println();
				System.out.println("Input Invaild! Try again.");
				String temp = Kb.next();
				System.out.println();
			} catch (NullPointerException ex) {
				System.out.println("Please make the random access file, or build the index.");
				System.out.println();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (!menuCon.equalsIgnoreCase("Exit"));

	}

	// Make a random-access file
	public static RandomAccessFile createRAF() {

		try {
			Scanner Kb = new Scanner(System.in);
			Student other = new Student(); // creates student object

			System.out.print("Enter input [text file] name: "); // prompting user for input file names
			String inputName = Kb.nextLine();
			Scanner input = new Scanner(new FileInputStream(inputName)); // reading text file

			System.out.print("Enter output [random access file] name: "); // prompting user for Output file names
			String OutputName = Kb.nextLine();

			// checks is file exists
			File file = new File(OutputName); // makes file with same name as output file
			if (file.exists()) { // checks if it exists
				file.delete(); // deletes
				System.out.println("File deleted!");
			}

			RandomAccessFile ranAccFile = new RandomAccessFile(OutputName, "rw"); // Creates Random Access File
			ranAccFile.setLength(0);

			// writes text file to random access file
			while (input.hasNext()) {
				other.readFromTextFile(input);
				other.writeToFile(ranAccFile);
			}
			System.out.println("File has been created!");
			System.out.println();
			return ranAccFile; // returns RandomAccessFile
		} catch (Exception e) {
			System.out.println("There was no data base found! Please try again. ");
			createRAF();
		}
		return null;
	}

	// Display a random-access file
	public static void display(RandomAccessFile ranAccFile) throws IOException {

		Scanner Kb = new Scanner(System.in);

		int i = 0; // counter
		Student other = new Student();

		// loop to go though the menu again
		while (true) {

			System.out.println("Return to main Menu [M]: ");
			System.out.println("Print the next 5 records [N]: ");
			System.out.println("Print all records [A]: ");
			String menuSlect = Kb.next();
			System.out.println();

			if (menuSlect.equalsIgnoreCase("M")) { // back to main menu
				break;
			} else if (menuSlect.equalsIgnoreCase("N")) { // printing 5 at a time
				// Formating
				System.out.println();
				System.out.println("First               " + "Last                " + "IDNum\t" + "GPA\t");
				System.out.println("--------------------------------------------------------");
				for (int j = 0; j < 5; j++) {
					try {
						ranAccFile.seek(i * 92); // moves pointer
						other.readFromFile(ranAccFile); // Reads line
						if (!other.getFirst().equalsIgnoreCase(Student.pad("delete", 20))) { // first name not equal to
																								// delete
							System.out.println(
									other.getFirst() + other.getLast() + other.getID() + "\t" + other.getGPA());
							System.out.println();
						}
						i++; // goes to the next file
					} catch (IOException e) {
						System.out.println();
						System.out.println("There are no more records in this file! ");
						System.out.println();
						i = 0; // goes back to first file
						break;
					}
				}
			} else if (menuSlect.equalsIgnoreCase("A")) { // printing all files
				i = 0;

				// Formating
				System.out.println();
				System.out.println("First               " + "Last                " + "IDNum\t" + "GPA\t");
				System.out.println("--------------------------------------------------------");
				while (true) { // iterate though the files
					try {
						ranAccFile.seek(i * 92); // moves pointer
						other.readFromFile(ranAccFile); // Reads line
						if (!other.getFirst().equalsIgnoreCase(Student.pad("delete", 20))) { // first name not equal to
																								// delete
							System.out.println(
									other.getFirst() + other.getLast() + other.getID() + "\t" + other.getGPA());
							System.out.println();
						}
						i++; // goes to next file
					} catch (IOException e) {
						i = 0; // goes back to first file
						break; // Breaks out of while loop
					}
				}
			}

		}
	}

	// Build the index
	public static AVLTree3<Pair<Integer>> buildIndex(RandomAccessFile ranAccFile) throws IOException {
		AVLTree3<Pair<Integer>> pairTree = new AVLTree3<>();
		Student other = new Student();

		//checks length of the RAF
		if (ranAccFile.length() == 0) {
			System.out.println("This file is Empty! ");
			System.out.println();
		} else {
			// writes text file to random access file
			for (int i = 0; i < (ranAccFile.length() / 92); i++) { // runs through the Random Access File
				ranAccFile.seek(i * 92);
				other.readFromFile(ranAccFile); // reads in a line
				int id = other.getID(); // gets the ID for the Record
				pairTree.add(new Pair<Integer>(id, i)); // puts a new element into the AVL tree
			}

			System.out.println("Index Created!"); // Prints out the to string to show it was made
			System.out.println();
		}
		return pairTree; //returns the AVLtree

	}

	// Display the index
	public static void displayIndex(AVLTree3<Pair<Integer>> pairTree, RandomAccessFile ranAccFile) throws IOException {

		Scanner Kb = new Scanner(System.in);
		String menuSlect = null;
		Student other = new Student();
		Pair<Integer> temp;
		do {
			try {
				// Menu
				System.out.println("Return to main Menu [M]: ");
				System.out.println("Start at an index [N]: ");
				System.out.println("print all Index's [A]: ");
				menuSlect = Kb.next();
				System.out.println();

				if (menuSlect.equalsIgnoreCase("M")) { // If they want to return to the main menu
					menuSlect = "q"; // breaks out of loop and goes to main menu
				} else if (menuSlect.equalsIgnoreCase("N")) { // if the want to display a part of the menu
					System.out.print("Enter the KEY to start from: ");
					int indexSatrt = Kb.nextInt(); // Give me the key
					temp = new Pair<>(indexSatrt, 0); //make a pair to pass to the AVL tree class
					pairTree.levelorder(pairTree.findNode(temp)); //Finding the node to start from based on the temp

				} else if (menuSlect.equalsIgnoreCase("A")) { // if they want to display the whole index
					pairTree.levelorder(pairTree.getRoot()); //printing the whole tree
				}
			} catch(NullPointerException ex){
				System.out.println();
				System.out.println("There is no Record with that KEY!");
				System.out.println();
			} catch (NoSuchElementException e) { // if they enter an ID that isn't in the Records
				System.out.println("There is no Student with that KEY!");
				System.out.println();
			}
		} while (!menuSlect.equalsIgnoreCase("q")); // loop to keep going

	}

	// Retrieve a record
	public static void retrieveRecord(AVLTree3<Pair<Integer>> pairTree, RandomAccessFile ranAccFile)
			throws IOException {
		Scanner Kb = new Scanner(System.in);
		Student other = new Student();
		int key = 0;
		String menuSlect = "y";
		Pair<Integer> temp = null; // making temper pair

		do {
			if (menuSlect.equalsIgnoreCase("y")) {
				try {

					System.out.print("Enter the KEY you want to find: ");
					System.out.println();
					key = Kb.nextInt(); //getting the key/ ID of the student
					temp = new Pair<>(key, 0); //making a Pair object based of the key 
					temp = pairTree.find(temp); //finding the the corresponding pair object in the AVL tree
					if (temp.getFirst() == key) { // checks if the temp.first is equal to key
						ranAccFile.seek((temp.getSecond() * 92)); // moves the file pointer to the record number
						other.readFromFile(ranAccFile); // reads in record
						System.out.println("First               " + "Last                " + "IDNum\t" + "GPA\t");
						System.out.println("--------------------------------------------------------");
						System.out.println(other.getFirst() + other.getLast() + other.getID() + "\t" + other.getGPA());
						System.out.println();
						break; // breaks out of the loop that is running through the list
					}
				}catch(NullPointerException ex){
					System.out.println();
					System.out.println("There is no Record with that KEY!");
					System.out.println();
				}
				catch (InputMismatchException e) { // if they input a wrong data type

					System.out.print("This is not an ID! please try again: ");
				}
			}
			System.out.println("Would you like to retreve another file[y/n]: ");
			menuSlect = Kb.next(); // if they want to retrieve another record
			System.out.println();
		} while (!menuSlect.equalsIgnoreCase("n"));
	}

	// Modify a record
	public static void modifyRecord(AVLTree3<Pair<Integer>> pairTree, RandomAccessFile ranAccFile) throws IOException {

		Scanner Kb = new Scanner(System.in);
		Student other = new Student();
		int key = 0;
		String menuSlect = "y";
		Pair<Integer> temp = null; // making a temporary pair

		do {

			try {
				System.out.print("Enter the KEY you want to find: ");
				key = Kb.nextInt(); //getting the Key/Id of the student
				temp = new Pair<>(key, 0); //making a pair object based off the Key
				temp = pairTree.find(temp); //finding the corresponding pair element in the AVL tree
				if (temp.getFirst() == key) { // seeing if the key in the temp pair is equal to the key were looking for

					System.out.print("What would you like to modify [F,L,GPA]: ");
					String mod = Kb.next();

					// Picks what part of that record they would like to modify
					if (mod.equalsIgnoreCase("F")) {
						System.out.print("Enter new First name: ");
						String newF = Kb.next();
						newF = Student.pad(newF, 20); // Padding
						ranAccFile.seek((temp.getSecond() * 92));
						other.readFromFile(ranAccFile); // Read in File
						other.setData(newF, other.getLast(), other.getID(), other.getGPA()); // Make changes
						ranAccFile.seek((temp.getSecond() * 92)); // Move pointer to start of record
						other.writeToFile(ranAccFile); // Write the new Information

					} else if (mod.equalsIgnoreCase("L")) {
						System.out.print("Enter new Lase name: ");
						String newL = Kb.next();
						newL = Student.pad(newL, 20);
						ranAccFile.seek((temp.getSecond() * 92));
						other.readFromFile(ranAccFile); // Read in File
						other.setData(other.getFirst(), newL, other.getID(), other.getGPA()); // Make changes
						ranAccFile.seek((temp.getSecond() * 92)); // Move pointer to start of record
						other.writeToFile(ranAccFile); // Write the new Information

					} else if (mod.equalsIgnoreCase("GPA")) {
						System.out.print("Enter new GPA name: ");
						double newGPA = Kb.nextDouble();
						ranAccFile.seek((temp.getSecond() * 92));
						other.readFromFile(ranAccFile); // Read in File
						other.setData(other.getFirst(), other.getLast(), other.getID(), newGPA); // Make changes
						ranAccFile.seek((temp.getSecond() * 92)); // Move pointer to start of record
						other.writeToFile(ranAccFile); // Write the new Information
					} else {

						System.out.println("This is not a Vaild data field! ");
					}
				}
				System.out.println("Would you like to Modify another file[y/n]: ");
				menuSlect = Kb.next(); // asking if they would like to modify another file
				System.out.println();
			} catch(NullPointerException ex){
				System.out.println();
				System.out.println("There is no Record with that KEY!");
				System.out.println();
			} catch (InputMismatchException e) { // if they enter in the wrong input

				System.out.println("This is not an ID! Would you like to try again [y/n]: ");
				menuSlect = Kb.next();
				menuSlect = Kb.next();
			}
		} while (!menuSlect.equalsIgnoreCase("n")); // lets them modify multiple things
	}

	// Add a new record
	public static void addRecord(AVLTree3<Pair<Integer>> pairTree, RandomAccessFile ranAccFile) throws IOException {

		try {
			Student other = new Student();
			ranAccFile.seek(ranAccFile.length()); // going to the end of the random access file

			Scanner Kb = new Scanner(System.in);
			System.out.println("Enter student info: first name, last name, Id, and GPA");

			other.readFromTextFile(Kb); // Reading form a file/Keyboard
			other.writeToFile(ranAccFile); // adding to the RandomAccessFile

			System.out.println("File was added!");
			System.out.println();

			int key = other.getID(); // getting the key of the new file
			int adress = (int) (ranAccFile.length() / 92) - 1; // getting the record number of the new file

			pairTree.add(new Pair<Integer>(key, adress)); // puts a new element into the AVL tree
		} catch (InputMismatchException e) { // if they enter in the wrong kinds of input for any fields
			System.out.println();
			System.out.println("Input Invaild! Try again.");
		}
	}

	// Delete Record
	public static void deleteRecord(AVLTree3<Pair<Integer>> pairTree, RandomAccessFile ranAccFile) throws IOException {

		Scanner Kb = new Scanner(System.in);
		Student other = new Student();
		Pair<Integer> temp = null; // making a temporary pair
		try {
			System.out.println("Enter the KEY of the record you wish to delete: ");
			int delKey = Kb.nextInt(); // reading the key that they want to delete
			System.out.println();
			temp = new Pair<>(delKey, 0); //making a pair based of the delKey
			temp = pairTree.find(temp); // finding the corresponding pair in the AVL tree
				if (temp.getFirst() == delKey) { // checking if the temp key is equal to the delete key
					ranAccFile.seek(temp.getSecond() * 92);
					String newF = "delete"; //setting the new name to to delete
					newF = Student.pad(newF, 20); // Padding

					other.readFromFile(ranAccFile); // Read in File
					other.setData(newF, other.getLast(), other.getID(), other.getGPA()); // Make changes

					ranAccFile.seek(temp.getSecond() * 92); // Move pointer to start of record
					other.writeToFile(ranAccFile); // Write the new Information

					pairTree.delete(temp); // removing the node in the linked list based on the index of temp
					System.out.println("Record number " + temp.getSecond() + " deleted!"); // telling the user what record was deleted
					System.out.println();
				}
		} catch(NullPointerException ex){
			System.out.println();
			System.out.println("There is no Record with that KEY!");
			System.out.println();
		}catch (InputMismatchException e) { // if they enter in the wrong inputs
			System.out.println("Input Invaild! Try again.");
			System.out.println();

		}
	}
}
