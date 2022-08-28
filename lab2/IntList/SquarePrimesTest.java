package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
//        IntList lst = IntList.of(14, 15, 16, 17, 18);
//        boolean changed = IntListExercises.squarePrimes(lst);
//        IntList lst2 = IntList.of(5, 7, 8, 10, 12);
//        boolean changed = IntListExercises.squarePrimes(lst2);
        IntList lst3 = IntList.of(4, 6, 8, 10, 13);
        boolean changed = IntListExercises.squarePrimes(lst3);

//        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertEquals("4 -> 6 -> 8 -> 10 -> 169", lst3.toString());
        assertTrue(changed);
    }
}
