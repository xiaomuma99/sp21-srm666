package gitlet;


import java.io.File;
import java.util.*;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author Morris Ma
 */
public class Repository {
    /**
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
    //public static String headCommitId;
    // this does not work, it not stay if we excute other program
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
            Utils.exitWithError("A Gitlet version-control system "
                    + "already exists in the current directory.");
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

        String commitId = initialCommit.getCommitId();
        Branch.setCommitId("master", commitId);
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
        String blobId = uddablob.getBlobId();
        StagingArea stagingArea = StagingArea.load();
        if (currentCommit.getBlobs().containsKey(blobId)) {
            stagingArea.clear();
            stagingArea.save();
            return;
        }
        //remove it from the staging area if it is already there(as can happen when
        // a file is changed, added, and then changed back to it’s original version).
        if (stagingArea.getAddition().containsKey(blobId)) {
            stagingArea.getAddition().remove(filename);
        }
        //otherwise add file into stagingArea
        stagingArea.getAddition().put(filename, blobId);
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

    private static void submitCommit(String message, String currentCommitId,
                                     String mergedCommitId) {
        StagingArea stagingArea = StagingArea.load();
        if (message.isEmpty()) {
            Utils.exitWithError("Please enter a commit message.");
        }

        if (stagingArea.getAddition().isEmpty() && stagingArea.getRemoval().isEmpty()) {
            Utils.exitWithError("No changes added to the commit.");
        }
        Commit newCommit = new Commit(message, currentCommitId, mergedCommitId);
        //A commit will only update the contents of files it is tracking
        // that have been staged for addition at the time of commit
        for (Map.Entry<String, String> entry : stagingArea.getAddition().entrySet()) {
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
     * rm command
     */
    public static void rmCommand(String filename) {
        StagingArea stagingArea = StagingArea.load();
        Commit currentCommit = Commit.getCommitById(Branch.getCommitId(HEAD.getBranchName()));
        File file = join(CWD, filename);
        if (stagingArea.getAddition().containsKey(filename)) {
            stagingArea.getAddition().remove(filename);
            stagingArea.save();
            return;

        }
        if (currentCommit.getBlobs().containsKey(filename)) {
            stagingArea.getRemoval().add(filename);
            restrictedDelete(file);
            stagingArea.save();
            return;
        }
        exitWithError("No reason to remove the file");
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
     * Execute status command.
     * Displays what branches currently exist, and marks the current branch with a *.
     * Also displays what files have been staged for addition or removal.
     */
    public static void printStatus() {

        //print out branch files
        String branchName = HEAD.getBranchName();
        List<String> branches = plainFilenamesIn(Branch.BRANCHE_DIR);
        System.out.println("=== Branches ===");
        System.out.println("*" + branchName);
        Collections.sort(branches);
        for (String branch : branches) {
            if (!branch.equals(branchName)) {
                System.out.println(branch);
            }
        }
        System.out.println();

        //print out Staged File
        StagingArea stagingArea = StagingArea.load();
        System.out.println("=== Staged Files ===");
        List<String> stagedFiles = new ArrayList<>(stagingArea.getAddition().keySet());
        printListString(stagedFiles);

        //print out removed File
        System.out.println("=== Removed Files ===");
        List<String> removedFiles = new ArrayList<>(stagingArea.getRemoval());
        printListString(removedFiles);

        //print out Modification Not Staged For Commit file
        System.out.println("=== Modification Not Staged For Commit ===");
        Commit currentCommit = Commit.getCommitById(
                Objects.requireNonNull(Branch.getCommitId(HEAD.getBranchName())));
        List<String> cwdFileNames = plainFilenamesIn(CWD);
        List<String> modifiedNotStagedForCommit = getModifiedNotStagedForCommit(
                stagingArea, currentCommit, cwdFileNames);
        printListString(modifiedNotStagedForCommit);

        //print out Untracked file
        System.out.println("=== Untracked Files ===");
        List<String> untrackedFiles = getUntrackedFiles(
                stagingArea, currentCommit, cwdFileNames);
        printListString(untrackedFiles);
    }
    private static void printListString(List<String> stringList) {
        for (String string : stringList) {
            System.out.println(string);
        }
        System.out.println();
    }
    /** Return a list of Untracked files. */
    private static List<String> getUntrackedFiles(StagingArea stagingArea,
                                                  Commit currentCommit,
                                                  List<String> cwdFileNames) {
        List<String> result = new ArrayList<>();
        for (String fileName : cwdFileNames) {
            boolean tracked = currentCommit.getBlobs().containsKey(fileName);
            boolean staged = stagingArea.getAddition().containsKey(fileName);
            //untracked files
            if (!staged && !tracked) {
                result.add(fileName);
            }
        }
        Collections.sort(result);
        return result;
    }
    /** Return a list of Modified but not Staged for Commit files. */
    private static List<String> getModifiedNotStagedForCommit(StagingArea stagingArea,
                                                               Commit currentCommit,
                                                              List<String> cwdFileNames) {
        List<String> result = new ArrayList<>();
        for (String fileName : cwdFileNames) {
            File file = join(CWD, fileName);
            Blobs blob = new Blobs(readContents(file));
            //case1: Tracked in current commit, changed in working directory
            //but not staged
            boolean tracked = currentCommit.getBlobs().containsKey(fileName);
            boolean changed = !blob.getBlobId().equals(currentCommit.getBlobs().get(fileName));
            boolean staged = stagingArea.getAddition().containsKey(fileName);
            if (tracked && changed && !staged) {
                result.add(fileName + " (modified)");
                continue;
            }
            //case2: Staged for addition, but with different contents than in the working
            //directory;
            changed = !blob.getBlobId().equals(stagingArea.getAddition().get(fileName));
            if (staged && changed) {
                result.add(fileName + " (modified)");
            }
        }
        // case3: Staged for addition, but deleted in the working directory;
        for (String fileName : stagingArea.getAddition().keySet()) {
            if (!cwdFileNames.contains(fileName)) {
                result.add(fileName + " (deleted)");
            }
        }

        //case4: Not staged for removal, but tracked in the current commit and
        //deleted from the working directory
        for (String fileName : currentCommit.getBlobs().keySet()) {
            boolean stagedForRemoval = stagingArea.getRemoval().contains(fileName);
            boolean cwdContains = cwdFileNames.contains(fileName);
            if (!stagedForRemoval && !cwdContains) {
                result.add(fileName + " (deleted)");
            }
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Execute checkout -- [filename] command
     * Execute checkout [commit id] -- [filename] command
     */
    public static void checkoutFile(String[] args) {
        //case1: checkout -- [filename] command
        if (args.length == 3) {
            if (!args[1].equals("--")) {
                exitWithError("Incorrect operands. ");
            }
            checkoutFile(Branch.getCommitId(HEAD.getBranchName()), args[2]);
            return;
        }
        //case2: checkout [commit id] -- [filename] command
        if (args.length == 4) {
            if (!args[2].equals("--")) {
                exitWithError("Incorrect operands. ");
            }
            checkoutFile(args[1], args[3]);
            return;
        }
        //case3: checkout [branch name]
        if (args.length == 2) {
            String commitId = Branch.getCommitId(args[1]);
            if (commitId == null) {
                exitWithError("No commit with that id exists. ");
                return;
            }
            if (args[1].equals(HEAD.getBranchName())) {
                exitWithError("No need to checkout the current branch. ");
                return;
            }
//            if (isUntrackedFiles()) {
//                exitWithError("There is an untracked file in the way; delete it, " +
//                        "or add and commit it first. ");
//            }
            Commit commit = Commit.getCommitById(commitId);
            checkoutFile(commit);
            HEAD.setBranchName(args[1]);

        }

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
        for (Map.Entry<String, String> entry : commit.getBlobs().entrySet()) {
            String blobId = entry.getKey();
            String filename = entry.getValue();
            byte[] contents = readContents(join(BLOBS_FOLDER, blobId));
            writeContents(join(CWD, filename), contents);
        }

    }

    private static boolean isUntrackedFiles() {
        List<String> fileList = plainFilenamesIn(CWD);
        StagingArea stagingArea = StagingArea.load();
        for (String filename : fileList) {
            if (stagingArea.getAddition().containsKey(filename)
                    || stagingArea.getRemoval().contains(filename)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Execute branch [branch Name] command
     * Creates a new branch with the given name, and points it at the current head commit.
     */
    public static void createBranch(String branchName) {
        if (join(Branch.BRANCHE_DIR, branchName).exists()) {
            exitWithError("A branch with that name already exists. ");
        }
        String currentCommitId = Branch.getCommitId(HEAD.getBranchName());
        Branch.setCommitId(branchName, currentCommitId);

    }
    /**
     * Execute rm-branch [branch Name] command
     * Deletes the branch with the given name.
     * This only means to delete the pointer associated with the branch;
     * it does not mean to delete all commits that were
     * created under the branch, or anything like that.
     */
    public static void removeBranch(String branchName) {
        File findBranchFile = join(Branch.BRANCHE_DIR, branchName);
        if (!join(Branch.BRANCHE_DIR, branchName).exists()) {
            exitWithError("A branch with that name does not exist. ");
            return;
        }
        if (HEAD.getBranchName().equals(branchName)) {
            exitWithError("Cannot remove the current branch. ");
            return;
        }
//        List<String> branches = plainFilenamesIn(Branch.BRANCHE_DIR);
//        for (String branch : branches) {
//            if (branch.equals(branchName)) {
//                File file = join(Branch.BRANCHE_DIR, branchName);
//                restrictedDelete(file);
//            }
//        }
        findBranchFile.delete();

    }
    /**
     * Execute reset command
     * Checks out all the files tracked by the given commit.
     * Removes tracked files that are not present in that commit.
     * Also moves the current branch’s head to that commit node.
     */
    public static void reset(String commitId) {
        Commit commit = Commit.getCommitById(commitId);
        if (commit == null) {
            exitWithError("No commit with that id exists. ");
            return;
        }
        checkoutFile(commit);
        Branch.setCommitId(HEAD.getBranchName(), commit.getCommitId());
    }
    /**
     * Excute merge command
     * Merges files from the given branch into the current branch.
     */
    public static void mergeBranch(String branchName) {
        return;
    }
}
