
/**
 * Used like an node to keep track of important information
 * @author Hang
 *
 */
public class Handle {
	private int startByte = 0;
	private int numByte = 0;
	private String DNA;
	
	/**
	 * @param start the staring position in pool
	 * @param num number of bytes
	 * @param str the String
	 */
	Handle(int start, int num, String str){
		startByte = start;
		numByte = num;
		DNA = str;
	}
	
	/**
	 * @return staring position in pool
	 */
	public int getStart(){
		return startByte;
	}
	
	/**
	 * @return number of bytes
	 */
	public int getNum(){
		return numByte;
	}
	
	/**
	 * @return the DNA string
	 */
	public String getDNA(){
		return DNA;
	}
	
	/**
	 * print the starting position in pool and number of byte of each handle
	 */
	public void print(){
		System.out.print(startByte + ":" +  numByte + ",");
	}

}
