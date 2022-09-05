package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){

        ArrayDeque<Integer> L1 = new ArrayDeque<>();
        ArrayDeque<Integer> L2 = new ArrayDeque<>();
        L1.addLast(4);
        L1.addLast(5);
        L1.addLast(6);
        L2.addLast(4);
        L2.addLast(5);
        L2.addLast(6);
        L1.removeLast();
        L2.removeLast();
        for(int i = 0; i < L1.size(); i += 1){
            assertEquals(L1.get(i),L2.get(i));
        }
        L1.removeLast();
        L2.removeLast();
        for(int i = 0; i < L1.size(); i += 1){
            assertEquals(L1.get(i),L2.get(i));
        }
        L1.removeLast();
        L2.removeLast();
        for(int i = 0; i < L1.size(); i += 1){
            assertEquals(L1.get(i),L2.get(i));
        }
    }
//    @Test
//    public void randomizedTest(){
//        ArrayDeque<Integer> L = new ArrayDeque<>();
//        ArrayDeque<Integer> L2 = new ArrayDeque<>();
//
//        int N = 5000;
//        for (int i = 0; i < N; i += 1) {
//            int operationNumber = StdRandom.uniform(0,  4);
//            if (operationNumber == 0) {
//                // addLast
//                int randVal = StdRandom.uniform(0, 100);
//                L.addLast(randVal);
//                L2.addLast(randVal);
//                System.out.println("addLast(" + randVal + ")");
//                System.out.println("L2 addLast(" + randVal + ")");
//            } else if (operationNumber == 1) {
//                // size
//                int size = L.size();
//                int size_L2 = L2.size();
//                System.out.println("size: " + size);
//                assertEquals(size, size_L2);
//            } else if (operationNumber == 2) {
//                //getLast
//                if(L.size()>0 && L2.size()>0){
//                    int result = L.getLast();
//                    int result_L2 = L2.getLast();
//                    System.out.println("getLast:(" + result+")");
//                    System.out.println("L2 getLast:(" + result_L2+")");
//                    assertEquals(result, result_L2);
//                }
//
//            } else if (operationNumber == 3) {
//                //removeLast
//                if(L.size()>0 && L2.size() >0){
//                    int result = L.removeLast();
//                    int result_L2 = L2.removeLast();
//                    System.out.println("removeLast:(" + result+")");
//                    System.out.println("L2 removeLast:(" + result_L2+")");
//                    assertEquals(result, result_L2);
//                }
//            }
//        }
//    }
}
