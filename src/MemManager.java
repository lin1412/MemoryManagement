/**
 * 
 * @author Hang
 *
 */
public class MemManager{
	public MemoryPool pool; // the memory pool
	public RecordArray array; // the record array
	public int poolSize; //size of memory pool
	public int recordSize; // size of record array
	
	/**
	 * @param poolS size of the memory pool
	 * @param recordS size of the record array
	 */
	MemManager(int poolS, int recordS){
		poolSize = poolS;
		recordSize = recordS;
		pool = new MemoryPool(poolSize);
		array = new RecordArray(recordSize);
	}
	
	/**
	 * @param loc where in the record array to be inserted to
	 * @param str the string to be inserted
	 */
	public void insert(int loc, String str){
		int byteLength = (int)Math.ceil(str.length() / 4.0) + 2;
		//if location is out of bound
		if ( loc < 0 || loc > recordSize - 1){
			System.out.println("location is out of range.");
		}
		//if not enough space found
		if( pool.firstFit(byteLength) == -1){
			System.out.println("Unable to insert a message of " + byteLength + " bytes ( " +
					str.length() + " characters).");
		}
		else{
			if(array.getHandle(loc) != null){
				remove(loc);
			}
			int s = pool.add(str);
			array.insert(loc, s, byteLength, str);
			System.out.println("Successfully inserted message of " + byteLength + " bytes ("
					+ str.length() + " characters) starting at position " + s +".");
		}
		System.out.println();
	}
	
	/**
	 * @param loc the location in record array to be removed
	 */
	public void remove(int loc){
		//if location is out of bound
		if ( loc < 0 || loc > recordSize - 1){
			System.out.println("location is out of range.");
		}
		else{
			//where to start the freeing of space
			int s = array.getHandle(loc).getStart(); 
			//where to end the freeing of space
			int e = array.getHandle(loc).getStart() + array.getHandle(loc).getNum(); 
			pool.remove(s, e);
			System.out.println("Deleted old message of " + array.getHandle(loc).getNum() + 
					" bytes (" + array.getHandle(loc).getDNA().length() + " characters) at " +
							"position " + loc + ".");
			array.setNull(loc);
		}
	}
	
	/**
	 * @param loc print the information contained in this handle
	 */
	public void print(int loc){
		//if location is out of bound
		if ( loc < 0 || loc > recordSize - 1){
			System.out.println("location is out of range.");
		}
		else{
			if (array.getHandle(loc) != null){
				System.out.print(loc + ": ");
				array.getHandle(loc).print();
				System.out.print("  " + array.getLogicalSize(loc) + ": ");
				System.out.print(array.getHandle(loc).getDNA());
			}
			else{
				System.out.println(loc + ": NULL");
			}
		}
		System.out.println();
		System.out.println();
	}
	
	/**
	 * print out all handles in array and their information
	 */
	public void print(){
		System.out.println("Record Array:");
		for ( int i = 0; i < array.getSize(); i++){
			System.out.print(i + ": " );
			if (array.getHandle(i) != null){
				array.getHandle(i).print();
				System.out.print("  " + array.getLogicalSize(i) + ": ");
				System.out.print(array.getHandle(i).getDNA());
			}
			else{
				System.out.print("NULL");
			}
			System.out.println();
		}
		System.out.println();
		
		System.out.println("Freeblock List:");
		pool.printFreeList();
		System.out.println();
	}

}
