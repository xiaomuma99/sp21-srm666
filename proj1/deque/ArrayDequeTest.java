package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){

        ArrayDeque<Integer> L1 = new ArrayDeque<>();
        L1.addLast(2);
        L1.addFirst(3);
        L1.addFirst(7);
        L1.addFirst(4);
        L1.addFirst(10);
        L1.addFirst(11);
        L1.addLast(6);
        L1.addLast(8);
        L1.addLast(9);
        L1.addFirst(13);
        L1.addFirst(14);
        L1.removeFirst();
        L1.removeLast();
        L1.removeLast();
        L1.removeFirst();
        L1.removeLast();
        L1.removeLast();
        L1.removeFirst();
        L1.removeLast();
        L1.removeFirst();
        L1.removeLast();
        L1.removeFirst();
        L1.addFirst(11);
        System.out.println(L1.get(1));
        System.out.println(L1.get(2));
        System.out.println(L1.get(3));
        System.out.println(L1.get(4));
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
