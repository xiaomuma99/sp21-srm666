package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Morris Ma
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            Utils.exitWithError("Please enter a command");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs(args, 1);
                Repository.initFolder();
                break;
            case "add":
                validateCWD();
                validateNumArgs(args, 2);
                Repository.addFile(args[1]);
                break;
            case "commit":
                validateCWD();
                validateNumArgs(args, 2);
                Repository.submitCommit(args[1]);
                break;
            case "checkout":
                validateCWD();
                Repository.checkoutFile(args);
                break;
            case "rm":
                validateCWD();
                validateNumArgs(args, 2);
                Repository.rmCommand(args[1]);
            case "log":
                validateCWD();
                validateNumArgs(args, 1);
                Repository.printoutLog();
                break;
            case "global-log":
                validateCWD();
                validateNumArgs(args, 1);
                Repository.printoutGlobalLog();
                break;
            case "find":
                validateCWD();
                validateNumArgs(args, 2);
                Repository.find(args[1]);
            case "status":
                validateCWD();
                validateNumArgs(args, 1);
                Repository.printStatus();
            case "branch":
                validateCWD();
                validateNumArgs(args, 2);
                Repository.createBranch(args[1]);
            case "rm-branch":
                validateCWD();
                validateNumArgs(args, 2);
                Repository.removeBranch(args[1]);
            case "reset":
                validateCWD();
                validateNumArgs(args, 2);
                Repository.reset(args[1]);
            case "merge":
                validateCWD();
                validateNumArgs(args, 2);
                Repository.mergeBranch(args[1]);
            default:
                validateCWD();
                Utils.exitWithError("No command with that name exists.");
        }
    }
    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            Utils.exitWithError("Incorrect operands. ");
        }
    }
    public static void validateCWD() {
        if (!Repository.GITLET_DIR.exists()) {
            Utils.exitWithError("Not in an initialized Gitlet directory.");
        }
    }
}
