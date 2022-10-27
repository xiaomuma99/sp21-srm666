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
                // TODO: handle the `init` command
                validateNumArgs(args, 1);
                Repository.initFolder();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateCWD();
                validateNumArgs(args, 2);
                Repository.addFile(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                //TODO: handle the 'commit [message]' command
                validateCWD();
                validateNumArgs(args, 2);
                Repository.submitCommit(args[1]);
                break;
            case "checkout":
                //TODO: handle the "checkout -- [file name]"
                //TODO: checkout "[commit id] -- [filename]"
                //TODO: also handle the 'checkout [branch name]'
                validateCWD();
                Repository.checkoutFile(args);
                break;
            case "rm":
                //TODO: handle the 'rm' command
                validateCWD();
                validateNumArgs(args, 1);
                Repository.unstageFile(args[1]);
            case "log":
                //TODO: handle the 'log' command
                validateCWD();
                validateNumArgs(args, 1);
                Repository.printoutLog();
                break;
            case "global-log":
                //TODO: handle the 'log' command
                validateCWD();
                validateNumArgs(args, 1);
                Repository.printoutGlobalLog();
                break;
            case "find":
                //TODO: handle the 'find' command
                validateCWD();
                validateNumArgs(args, 2);
                Repository.find(args[1]);
            case "status":
                //TODO: handle the 'status' command
                validateCWD();
                validateNumArgs(args, 1);
                Repository.printStatus();
            case "branch":
                //TODO: handle the 'branch' command
                validateCWD();
                validateNumArgs(args, 2);
                Repository.createBranch(args[1]);
            case "rm-branch":
                //TODO: handle the 'rm-branch' command
                validateCWD();
                validateNumArgs(args, 2);
                Repository.removeBranch(args[1]);
            case "reset":
                //TODO: handle the 'reset' command
                validateCWD();
                validateNumArgs(args, 2);
                Repository.reset(args[1]);
            case "merge":
                //TODO: handle the 'merge' command
                validateCWD();
                validateNumArgs(args, 2);
                Repository.mergeBranch(args[1]);
            default:
                Utils.exitWithError("No command with that name exists.");
        }
        return;
    }
    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments"));
        }
    }
    public static void validateCWD() {
        if (!Repository.GITLET_DIR.exists()) {
            Utils.exitWithError("Not in an initialized Gitlet directory.");
        }
    }
}
