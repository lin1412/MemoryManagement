import java.util.ArrayList;

/**
 * 
 * @author Hang
 *
 */
public class MemoryPool {
	public byte[] pool; //an array of bytes
	public LList<Boolean> freeList = new LList<Boolean>(); //keep tracks of the free spaces in pool
	public byte length1 = 0; //use to store size of record length
	public byte length2 = 0; //use to store size of record length
	public int current = 0; //the current position in freeList
	
	/**
	 * @param size the size of the pool
	 */
	MemoryPool(int size){
		pool = new byte[size];
		for( int i = 0; i < size; i++){
			freeList.append(false);
		}
		freeList.moveToStart();
	}
	
	/**
	 * @param str the string to be added
	 * @return the starting location in pool
	 */
	public int add(String str){
		//how many bytes the String take
		int strByte = (int)Math.ceil(str.length() / 4.0);
		//a temporary array to hold substring of 4 letter each if possible
		String[] tempString = new String[strByte]; 
		//how many byte total we need to insert to pool
		int byteLengthTotal = 2 + strByte;
		//where to begin the insertion to the pool
		int start = firstFit(byteLengthTotal);
		lengthByte(str);
		
		if ( str.length()%4 == 0){
			//split the string into 4 letter each and add to an array
			for ( int i = 0; i < strByte;i++){
				tempString[i] = str.substring(i*4, i*4+4);
			}
		}
		
		// if the string is not a multiple of 4
		else{
			//how many FULL byte we need
			int normalByte = str.length()/4; 
			//the position of where the extra letters start
			int extraLetter = str.length() - (str.length()%4);
			
			for ( int i = 0; i < normalByte;i++){
				tempString[i] = str.substring(i*4, i*4+4);
			}
			tempString[normalByte]=str.substring(extraLetter,str.length());
		}
		
		//add the fist 2 byte representing the string length in to pool
		freeList.moveToPos(start);
		pool[start] = length1;
		freeList.setValue(true);
		freeList.next();
		pool[start+1] = length2;
		freeList.setValue(true);
		freeList.next();
		//where to begin adding the bytes representing the strings
		int strStart = start + 2;
		// add the byte representing the string in pool, and set the freeList in those space as full
		for ( int i = 0; i < strByte; i++){
			pool[strStart + i] = charByte(tempString[i]);
			freeList.setValue(true);
			freeList.next();
		}
		current = freeList.currPos();
		return start;
	}
	
	/**
	 * @param byteLengthTotal The total number of bytes need to be added into pool
	 * @return starting location of a chain of empty space in pool big enough to fit
	 */
	public int firstFit(int byteLengthTotal){
		int start = freeList.currPos(); //the current position on pool
		int sizeNeeded = byteLengthTotal; //how many bytes we need
		int sizeScanned = 0; //how many have we found in a chain
		freeList.moveToStart();
		int head = freeList.currPos();//position of top of the pool
		freeList.moveToEnd();
		int tail = freeList.currPos();//position of bottom of pool
		freeList.moveToPos(start);
		
		while(sizeScanned < sizeNeeded){
			//if space is empty, add to chains
			if (freeList.getValue() == false){
				sizeScanned += 1;
			}
			
			//if space is full, reset chain
			else if (freeList.getValue() == true){
				sizeScanned = 0;
			}
			
			freeList.next();
			
			//if the loop hits the end, go to front.
			if(freeList.currPos() == tail){
				freeList.moveToPos(head);
				sizeScanned = 0;
			}
			
			//if the loop goes back to where it first started, stop.
			if(freeList.currPos() == start){
				return -1;
			}
		}
		//the starting location of the chain of spaces big enough to hold the bytes
		int temp = freeList.currPos() - sizeNeeded;
		freeList.moveToPos(start);
		return temp;
	}
	
	/**
	 * @param start the starting location in pool
	 * @param end the end location in pool
	 */
	public void remove(int start, int end){
		//the current position
		int tempPos = freeList.currPos();
		freeList.moveToPos(start);
		for ( int i = start; i < end ; i++){
			pool[i] = 0;
			freeList.setValue(false);
			freeList.next();
		}
		freeList.moveToPos(tempPos);
		
	}
	
	/**
	 * @param str the String to be convert in byte based on it's length
	 */
	public void lengthByte(String str){
		int length = str.length(); // the string length
        int result = 0; // binary number of the string length
        int multiplier = 1; //used to keep track of number position

        while (length > 0)
    	{
        	//number still need to be work on
    		int residue = length%2;
            length = length/2;
            result = result +residue*multiplier;
            multiplier = multiplier * 10;
    	}
        
        //if only 1 byte is need to store  length of record
        if ( length <= 255){
        	length2 += result;
        }
        //if both byte are needed to store length of record
        else
        {
        	length1 = (byte) (result/100000000);
        	int mod = 0; //number that will be added into length2 depending on the digit position
        	int tempMulti = 1;//used to keep track of number position
        	for (int i = 0; i < 8; i++){
        		mod = result%10 * tempMulti;
        		tempMulti = tempMulti * 10;
        		length2 += mod;
        	}
        }
	}

	/**
	 * @param str The string that's going to be converted into byte
	 * @return the byte representation of the string
	 */
	public byte charByte(String str){
		//Temporary variable to be use to get byte representing the letters
		byte temp = 0;
		String s = "ACGT";
		for ( int i = 0; i < str.length(); i++){
			//look for A's
			if (str.charAt(i) == s.charAt(0)){
				if (i == 0){
					temp += 0000000;
				}
				if (i == 1){
					temp += 000000;
				}
				if (i == 2){
					temp += 0000;
				}
				if (i == 3){
					temp += 00;
				}
			}
			//look for C's
			if (str.charAt(i) == s.charAt(1)){
				if (i == 0){
					temp += 01000000;
				}
				if (i == 1){
					temp += 110000;
				}
				if (i == 2){
					temp += 1100;
				}
				if (i == 3){
					temp += 01;
				}
			}
			//look for G's
			if (str.charAt(i) == s.charAt(2)){
				if (i == 0){
					temp += 10000000;
				}
				if (i == 1){
					temp += 100000;
				}
				if (i == 2){
					temp += 1000;
				}
				if (i == 3){
					temp += 10;
				}
			}
			//look for T's
			if (str.charAt(i) == s.charAt(3)){
				if (i == 0){
					temp += 11000000;
				}
				if (i == 1){
					temp += 110000;
				}
				if (i == 2){
					temp += 1100;
				}
				if (i == 3){
					temp += 11;
				}
			}
		}
		
		return (byte) (temp & 0xFF);
	}
	
	/**
	 * print the freeList in following format
	 * starting location of free space : how many free space are chained together
	 */
	public void printFreeList(){
		//list holding the starting position of each set of free spaces
		ArrayList<Integer> start = new ArrayList<Integer>(); 
		//list holding the ending position of each set of free spaces
		ArrayList<Integer> free = new ArrayList<Integer>();
		//number of free spaces in a chain
		int num = 0;
		//current position
		int begin = freeList.currPos();
		freeList.moveToStart();
		if ( freeList.getValue() == false){
			start.add(freeList.currPos());
			num+=1;
		}
		freeList.next();
		for (int i = 0; i < freeList.length() - 1; i++){
			// if the current position is open, 
			// check if the previous is close, if it is add it to list
			// add 1 to number of open spaces
			if(freeList.getValue() == false){
				int s = freeList.currPos();
				freeList.prev();
				if (freeList.getValue() == true){
					start.add(s);
				}
				freeList.next();
				num+=1;
			}
			// if the current position is close, check if the previous is open.
			if(freeList.getValue() == true || freeList.currPos() == freeList.length()-1){
				freeList.prev();
				if (freeList.getValue() == false){
					free.add(num);
					num= 0;
				}
				freeList.next();
			}
			freeList.next();
		}
		
		freeList.moveToPos(begin);
		
		//print out the sets of free spaces in pool
		for ( int j = 0; j < start.size(); j ++){
			if (start.get(j) != freeList.currPos()){
				System.out.print(start.get(j) + ":" + free.get(j) + ", ");
			}
			else{
				System.out.print("*" + start.get(j) + ":" + free.get(j) + ", ");
			}
		}
		System.out.println();
	}
}
