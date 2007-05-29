import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class CornerBoardTest {
	
	@Test(timeout = 5000)
	public void testIterative() {
		
		testHelper(6,0,false);	
		testHelper(5,8,false);
		testHelper(7,2*8,false);
		testHelper(8,2*8,false);
		testHelper(9,14*8,false); // Up from 2 to 14 unique!
				
		testHelper(6,0,true);	
		testHelper(5,8,true);
		testHelper(7,2*8,true);
		testHelper(8,2*8,true);
		testHelper(9,14*8,true); // Up from 2 to 14 unique!
	}
	
	void testHelper(int size,int expectedSolutions,boolean recursive) {
		System.out.println("SIZE: " + size);		
		CornerBoard cboard = new CornerBoard(size);
		cboard.setRecursive(recursive);
		
		int total = 0;		
		for (Board2 b : cboard.init()) {			
			b.backtrack();
			total += b.getTotal();							
		}
		assertEquals(expectedSolutions,total);
	}
	
	
	
}
