package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
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
        if (!Utils.plainFilenamesIn(Repository.CWD).contains(".gitlet")) {
            Utils.exitWithError("Not in an initialized Gitlet directory.");
        }
        String firstArg = args[0];
        String commitID;
        String filename;
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                Repository.initFolder();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                Repository.addFile(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                //TODO: handle the 'commit [message]' command
                Repository.submitCommit(args[1]);
                break;
            case "checkout":
                //TODO: handle the "checkout -- [file name]"
                //TODO: checkout "[commit id] -- [filename]"
                //TODO: also handle the 'checkout [branch name]'
                commitID = args[1];
                filename = args[2];
                Repository.checkoutfile(commitID,filename);
                break;
            case "rm":
                //TODO: handle the 'rm' command
                Repository.unstageFile(args[1]);
            case "log":
                //TODO: handle the 'log' command
                Repository.printoutLog();
                break;
            default:
                Utils.exitWithError("No command with that name exists.");
        }
    }
}
