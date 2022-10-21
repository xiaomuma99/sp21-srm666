package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.util.*;

import static gitlet.Utils.join;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File BLOBS_FOLDER = Utils.join(".gitlet","Blobs");
    public static final File STAGE_DIR = join(".gitlet","staging");

    /** The message of this Commit. */

    private class commitNode {
        private String message;
        /** Current date and time of this Commit*/
        private String commitDate;
        public String parentID;
        private commitNode parent;
        private Hashtable<String, String> referenceOfBlobInNode;
        /**
         * Constructor
         */
        public commitNode(String s, commitNode n) {
            message = s;
            parent = n;
        }

    }

    public commitNode sentinel;
    public commitNode master;
    public commitNode head;
    public Hashtable<String, String> referenceofBlob;

    /* TODO: fill in the rest of this class. */
    /**
     * Constructors
     * Initializes an empty Commit
     */
    public Commit() {
        String message = "initial commit";
        sentinel = new commitNode(message, null);
        sentinel.commitDate = "00:00:00 UTC, Thursday, 1 January 1970";
        head = sentinel;
        master = sentinel;
        referenceofBlob.put(null,null);
    }
    /**
     * Create a new commitNode and add it to the head of commitNode SSList
     */
    public void addFirst(String message, List<String> filelist) {
        commitNode p = new commitNode(message,null);
        p.parent = sentinel.parent;
        sentinel.parent = p;
        p.referenceOfBlobInNode = (Hashtable<String, String>) this.referenceofBlob.clone();
        String commitVersion = Utils.sha1(sentinel.parent);
        for (int i=0; i < filelist.size(); i++) {
            p.referenceOfBlobInNode.put(filelist.get(i),commitVersion);
            this.referenceofBlob.put(filelist.get(i),commitVersion);
        }
        p.commitDate = getCurrentDate();
        head = p;
        master = p;
    }

    /**
     * Print out Log
     * Starting at the current head commit,
     * display information about each commit backwards
     * along the commit tree until the initial commit
     */
    public void printLog() {
        commitNode p = head;
        while (p!=null) {
            System.out.println("===");
            System.out.println("commit  " + Utils.sha1(p));
            System.out.println(p.commitDate);
            System.out.println(p.message);
            System.out.println();
            p = p.parent;
        }
        System.out.println("===");
        System.out.println("commit  " + Utils.sha1(sentinel));
        System.out.println(sentinel.commitDate);
        System.out.println(sentinel.message);
        System.out.println();
    }
    /**
     * find
     * Prints out the ids of all commits that have the given commit message
     */
    public void findmessage(String message) {
        commitNode p = sentinel;
        while (p!=null) {
            if (p.message.equals(message)) {
                System.out.println(Utils.sha1(p));
                p = p.parent;
            } else {
                Utils.exitWithError("Found no commit with that message.");
            }
        }
    }
    public commitNode findNode(String commitID) {
        commitNode p = head;
        while (p != null) {
            String s = Utils.sha1(p);
            if (s.equals(commitID)) {
                return p;
            }
            p = p.parent;
        }
        return null;
    }
    /**
     * checkout file
     * checkout -- [file name]
     * checkout [commit id] -- [file name]
     */
    public void checkoutfile(String commitID, String filename) {
        java.util.List<String> workFile = Utils.plainFilenamesIn(CWD);
        commitNode p = (commitID== null) ? head : findNode(commitID);
        for (Map.Entry<String, String> e : p.referenceOfBlobInNode.entrySet()) {
            if(e.getKey().equals(filename)) {
                Blobs uddablob = Blobs.fromFile(filename, BLOBS_FOLDER);
                if (workFile.contains(filename)) {
                    uddablob.saveBlob(Blobs.STAGE_DIR);
                    uddablob.saveBlob(Blobs.CWD);
                }
                uddablob.saveBlob(CWD);
            }
        }
    }

    /**
     * return current date
     * @return
     */
    private String getCurrentDate() {
        Date date = new Date();
        return date.toString();
    }



}
