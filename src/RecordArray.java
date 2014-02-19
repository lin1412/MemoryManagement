/**
 * // Use for testing purpose only, holds information that the memory pool doesn't.
 * @author Hang
 *
 */
public class RecordArray {
	public Handle[] handle; //the handle node
	public int arraySize; //size of the record array
	public int logicalSize = 0; //the size of the message
	
	/**
	 * @param size size of the array
	 */
	RecordArray(int size){
		arraySize = size;
		handle = new Handle[size];
	}
	
	/**
	 * @return size of array
	 */
	public int getSize(){
		return arraySize;
	}
	/**
	 * @param loc location to be inserted to
	 * @param start starting position in pool
	 * @param num number of bytes
	 * @param str the String
	 */
	public void insert(int loc, int start, int num, String str){
		handle[loc] = new Handle(start, num, str);
	}
	
	/**
	 * @param loc the location of handle
	 * @return the handle
	 */
	public Handle getHandle(int loc){
		return handle[loc];
	}
	/**
	 * @param loc the location of handle to delete
	 */
	public void setNull(int loc){
		handle[loc] = null;
	}
	
	/**
	 * @param loc the location of handle
	 * @return the actual string length
	 */
	public int getLogicalSize(int loc){
		return handle[loc].getDNA().length();
	}

}
