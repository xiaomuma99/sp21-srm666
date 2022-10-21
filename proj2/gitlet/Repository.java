package gitlet;

import java.io.File;
import java.util.Map;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    /**
     * Create a root commit tree;
     */
    public static Commit tree;
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /**
     * The Staging area directory
     */
    public static final File STAGE_DIR = join(GITLET_DIR,"staging");
    public static final File BLOBS_FOLDER = Utils.join(".gitlet","Blobs");
    /* TODO: fill in the rest of this class. */
    /**
     * Execute init command
     * Create a .gitlet folder
     * Create a commit object
     * commit message ("initial commit")
     * point to master
     * timestamp: 00:00:00 UTC, Thursday, 1 January 1970
     */
    public static void initFolder() {
        if (Utils.plainFilenamesIn(Repository.CWD).contains(".gitlet")) {
            Utils.exitWithError("A Gitlet version-control system already exists in the current directory.");
        }
        GITLET_DIR.mkdir();
        tree = new Commit();
        tree.master = tree.sentinel;
    }
    /**
     * Execute add command
     * Adds a copy of the file as it currently exists to the staging area
     */
    public static void addFile(String filename) {
        if (!Utils.plainFilenamesIn(Repository.CWD).contains(filename)) {
            Utils.exitWithError("File does not exist.");
        }
        Blobs uddablob = Blobs.fromFile(filename, CWD);
        uddablob.saveBlob(STAGE_DIR);
    }
    /**
     * Execute commit command
     * Saves a snapshot of tracked files in the current commit and
     * staging area so they can be restored at a later time, creating a new commit.
     */
    public static void submitCommit(String message) {
        if (message == null) {
            Utils.exitWithError("Please enter a commit message.");
        }
        java.util.List<String> stageFile = Utils.plainFilenamesIn(STAGE_DIR);
        if (stageFile.size() == 0) {
            Utils.exitWithError("No changes added to the commit.");
        }
        for (int i = 0; i < stageFile.size(); i ++) {
            String filename = stageFile.get(i);
            Blobs uddablob = Blobs.fromFile(filename, STAGE_DIR); //read file from Stage folder
            uddablob.saveBlob(BLOBS_FOLDER);//write a copy file to Blobs folder
        }
        tree.addFirst(message,stageFile);//create a new commit Node, update Hashtable;
        Blobs.clearBlobFromStage();

    }
    /**
     * Execute checkout -- [filename] command
     * Execute checkout [commit id] -- [filename] command
     */
    public static void checkoutfile(String commitID, String filename) {
        tree.checkoutfile(commitID, filename);
    }
    /**
     * Unstage the file if it is currently staged for addition
     */
    public static void unstageFile(String filename) {
        Blobs uddablob = Blobs.fromFile(filename, STAGE_DIR);
        if (uddablob == null) {
            exitWithError("No reason to remove the file.");
        }
        uddablob.saveBlob(Blobs.STAGE_REMOVE_DIR);
        restrictedDelete(filename);
    }
    /**
     * Execute Log command
     */
    public static void printoutLog() {
        tree.printLog();
    }
    /**
     * Execute find command
     */
    public void find(String message) {
        tree.findmessage(message);
    }


}
