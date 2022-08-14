/** Class that prints the Collatz sequence starting from a given number.
 *  @author YOUR NAME HERE
 */
public class Collatz {

    /** Buggy implementation of nextNumber! */
//    public static int nextNumber(int n) {
//        if (n == 128) {
//            return 1;
//        } else if (n == 5) {
//            return 3 * n + 1;
//        } else {
//            return n * 2;
//        }
//    }
    /** create a new function nextNumber to return next Number follow Collatz sequence*/
    public static int nextNumber(int n) {
        if (n % 2 == 0) {
            return n/2;
        } else {
            return 3 * n + 1;
        }
    }

    public static void main(String[] args) {
        int n = 27;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

