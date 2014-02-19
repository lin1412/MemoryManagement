import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.
// Hang Lin

//Used eclipse Indigo, running on window 7 (64it), completed on 9/22/12

//This program runs like an memory allocation process which we can test easily. It stores Strings
//representing DNAs, that will be converted into bytes. The Record Array can be use to check on
//information otherwise unable to check. It uses an circular first fit algorithm to check for free
//spaces within a pool of size x. 
public class memman {

	/**
	 * @param args the parameter that was entered at command prompt
	 */
	public static void main(String[] args) {
		int poolSize = Integer.parseInt(args[0]); //size of the memory pool
		int recordSize = Integer.parseInt(args[1]); //size of the record array
		Scanner scanner = null;
		String command = "";
		try {
			scanner = new Scanner ( new File (args[2]));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MemManager manager = new MemManager(poolSize, recordSize);
		
		while(scanner.hasNext()){
			command = scanner.next();
			if ( command.equals("print")){
				if(scanner.hasNextInt()){
					manager.print(scanner.nextInt());
				}
				else{
					manager.print();
				}
			}
			if ( command.equals("insert")){
				int locA = scanner.nextInt();
				String DNA = scanner.next();
				if ( locA >= recordSize){
					System.out.println("Unable to insert using record array position "+ locA);
				}
				else{
					manager.insert(locA, DNA);
				}
				
			}
			if ( command.equals("remove")){
				int locR = scanner.nextInt();
				manager.remove(locR);
			}
		}
		System.out.println("End program.");
	}

}
