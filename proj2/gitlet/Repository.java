package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Morris Ma
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File BLOBS_FOLDER = join(GITLET_DIR, "Blobs");

    /** The head pointer, always point to current commit.*/
    //public static String headCommitId; //this does not work, it not stay if we excute other program

    /* TODO: fill in the rest of this class. */
    /**

    /**
     * Execute init command
     * Create a .gitlet folder
     * Create a commit object
     * commit message ("initial commit")
     * point to master
     * timestamp: 00:00:00 UTC, Thursday, 1 January 1970
     */

    public static void initFolder() {
        if (GITLET_DIR.exists()) {
            Utils.exitWithError("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        GITLET_DIR.mkdir();
        Commit.COMMIT_FOLDER.mkdir();
        BLOBS_FOLDER.mkdir();

        Branch.BRANCHE_DIR.mkdir();

        Commit initialCommit = new Commit();
        initialCommit.save();

        StagingArea stagingArea = new StagingArea();
        stagingArea.save();

        String CommitId = initialCommit.getCommitId();
        Branch.setCommitId("master", CommitId);
        HEAD.setBranchName("master");

    }
    /**
     * Execute add command
     * Adds a copy of the file as it currently exists to the staging area
     */
    public static void addFile(String filename) {
        File newFile = Utils.join(CWD, filename);
        if (!newFile.exists()) {
            Utils.exitWithError("File does not exist.");
        }
        byte[] tempContents = readContents(newFile);
        Blobs uddablob = new Blobs(tempContents);
        // If the current working version of the file is identical to
        // the version in the current commit, do not stage it to be added

        Commit currentCommit = Commit.getCommitById(Branch.getCommitId(HEAD.getBranchName()));
        String BlobId = uddablob.getBlobId();
        StagingArea stagingArea = StagingArea.load();
        if (currentCommit.getBlobs().containsKey(BlobId)) {
            stagingArea.clear();
            stagingArea.save();
            return;
        }
        //remove it from the staging area if it is already there(as can happen when
        // a file is changed, added, and then changed back to it’s original version).
        if (stagingArea.getAddition().containsKey(BlobId)) {
            stagingArea.getAddition().remove(filename);
        }
        //otherwise add file into stagingArea
        stagingArea.getAddition().put(filename, BlobId);
        uddablob.save();
        stagingArea.save();
    }

    /**
     * Execute commit command
     * Saves a snapshot of tracked files in the current commit and
     * staging area so they can be restored at a later time, creating a new commit.
     */
    public static void submitCommit(String message) {
        String currentCommitId = Branch.getCommitId(HEAD.getBranchName());
        submitCommit(message, currentCommitId, null);
    }

    private static void submitCommit(String message, String currentCommitId, String mergedCommitId) {
        StagingArea stagingArea = StagingArea.load();
        if (message.isEmpty()) {
            Utils.exitWithError("Please enter a commit message.");
        }

        if (stagingArea.getAddition().isEmpty() && stagingArea.getRemoval().isEmpty()) {
            Utils.exitWithError("No changes added to the commit.");
        }
        Commit newCommit = new Commit(message,currentCommitId,mergedCommitId);
        //A commit will only update the contents of files it is tracking that have been staged for addition at the time of commit
        for (Map.Entry<String, String> entry : stagingArea.getAddition().entrySet() ) {
            String fileName = entry.getKey();
            String blobId = entry.getValue();
            newCommit.getBlobs().put(fileName, blobId);
        }
        for (String fileName : stagingArea.getRemoval()) {
            newCommit.getBlobs().remove(fileName);
        }
        String commitId = newCommit.getCommitId();
        Branch.setCommitId(HEAD.getBranchName(), commitId);
        newCommit.save();
        stagingArea.clear();
        stagingArea.save();
    }

    /**
     * Unstage the file if it is currently staged for addition
     */
    public static void unstageFile(String filename) {
        Blobs uddablob = Blobs.fromFile(filename, StagingArea.STAGE_FILE);
        if (uddablob == null) {
            exitWithError("No reason to remove the file.");
        }
        uddablob.save();
        restrictedDelete(filename);
    }
    /** Execute Log command. */
    public static void printoutLog() {

        String commitId = Branch.getCommitId(HEAD.getBranchName());
        while (commitId != null) {
            Commit commit = Commit.getCommitById(commitId);
            System.out.println(commit);
            commitId = commit.getFirstParentID();
        }
    }
    /** Execute Global Log command. */
    public static void printoutGlobalLog() {
        List<String> commitIdList = Utils.plainFilenamesIn(Commit.COMMIT_FOLDER);
        for (String commitId : commitIdList) {
            Commit commit = Commit.getCommitById(commitId);
            System.out.println(commit);
        }
    }
    /** Execute find command. */
    public static void find(String message) {
        List<String> commitIdList = Utils.plainFilenamesIn(Commit.COMMIT_FOLDER);
        for (String commitId : commitIdList) {
            Commit commit = Commit.getCommitById(commitId);
            if (commit.getMessage().equals(message)) {
                System.out.println(commitId);
            }
        }
    }
    /**
     * Execute checkout -- [filename] command
     * Execute checkout [commit id] -- [filename] command
     */
    public static void checkoutFile(String[] args) {
        //case1: checkout -- [filename] command
        if (args.length == 3) {
            if (!args[1].equals("--")) {
                exitWithError("Incorrect operands");
            }
            checkoutFile(Branch.getCommitId(HEAD.getBranchName()), args[2]);
            return;
        }
        //case2: checkout [commit id] -- [filename] command
        if (args.length == 4) {
            if (!args[2].equals("--") ) {
                exitWithError("Incorrect operands");
            }
            checkoutFile(args[1], args[3]);
        }
        //case3: checkout [branch name]

    }
    private static void checkoutFile(String commitId, String filename) {
        Commit commit = Commit.getCommitById(commitId);
        if (commit == null) {
            exitWithError("No commit with that id exist");
            return;
        }
        String blobId = commit.getBlobs().get(filename);
        if (blobId == null) {
            exitWithError("File does not exist in that commit");
            return;
        }
        byte[] contents = readContents(join(BLOBS_FOLDER, blobId));
        writeContents(join(CWD, filename), contents);
    }
    private static void checkoutFile(Commit commit) {

    }

}
