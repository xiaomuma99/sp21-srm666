package gitlet;


import java.io.*;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author Morris Ma
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    /** The commit file folder. */
    public static final File COMMIT_FOLDER = Utils.join(Repository.GITLET_DIR, "/commits");
    /** The message of this Commit. */
    private final String message;
    /** The commit date of this Commit. */
    private final Date commitDate;
    /** A mapping of file names to blob references. */
    private HashMap<String, String> blobs;
    /** First parent reference, hash id of parent commit. */
    private final String firstParentID;
    /** Second parent reference, hash id of parent commit. */
    private final String secondParentID;
    /** Hashcode string of current commit. */
    private final String commitId;
    /**
     * Constructor
     */
    public Commit() {
        this.message = "initial commit";
        this.commitDate = new Date(0);
        blobs = new HashMap<>();
        this.firstParentID = null;
        this.secondParentID = null;
        this.commitId = calcHash(); //commitId = sha1(this) will crash the program;
    }
    public Commit(String message, String firstParentID, String secondParentID) {
        this.message = message;
        this.commitDate = new Date();

        this.firstParentID = firstParentID;
        this.secondParentID = secondParentID;
        commitId = calcHash();

        Commit firstParentCommit = getCommitById(firstParentID);
        blobs = new HashMap<>();
        blobs.putAll(firstParentCommit.blobs);
    }
    //I have no idea what this is
    private String calcHash() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return Utils.sha1((Object) bos.toByteArray());
    }
    /** Return message. */
    public String getMessage() {
        return this.message;
    }
    /** Return filename to BLobID */
    public HashMap<String, String> getBlobs() {
        return this.blobs;
    }
    /** Return commitId. */
    public String getCommitId() {
        return commitId;
    }
    /** Return first ParentID. */
    public String getFirstParentID() {
        return firstParentID;
    }
    /** Return second ParentID. */
    public String getSecondParentID() {
        return secondParentID;
    }

    public String getBlobId(String fileName) {
        return blobs.get(fileName);
    }
    /** Return Commit object by commitId. */
    public static Commit getCommitById(String commitId) {
        List<String> commitFileList = Utils.plainFilenamesIn(COMMIT_FOLDER);
        for (int i = 0; i < commitFileList.size(); i++) {
            if (commitFileList.get(i).equals(commitId)) {
                break;
            }
        }
        File outFile = join(COMMIT_FOLDER, commitId);
        if (!outFile.exists()) {
            return null;
        } else {
            return readObject(outFile, Commit.class);
        }
    }
    /** Save commit to disk file */
    public void save() {
        String s = this.commitId;
        File outFile = Utils.join(COMMIT_FOLDER, s);
        Utils.writeObject(outFile, this);
    }

//    /** Print out Log by Commit. */ cannot pass test
//    public void printCommit() {
//        System.out.println("===");
//        System.out.println("commit " + commitId);
//        System.out.println("Date: " + commitDate);
//        System.out.println(message);
//        System.out.println();
//    }

    /**Copy from Github. */
    public String toString() {
        String dummyString = "===\n";
        String commitString = String.format("commit %s\n", commitId);

        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en", "US"));
        String date = simpleDateFormat.format(commitDate);
        String dataString = String.format("Date: %s\n", date);

        String messageString = String.format("%s\n", message);
        return dummyString + commitString + dataString + messageString;
    }


}
