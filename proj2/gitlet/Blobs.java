package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.join;
import static gitlet.Utils.restrictedDelete;

public class Blobs implements Serializable {

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITLET_FOLDER = Utils.join(CWD,".gitlet");
    public static final File BLOBS_FOLDER = Utils.join(GITLET_FOLDER,"Blobs");
    public static final File STAGE_DIR = join(GITLET_FOLDER,"staging");
    public static final File STAGE_REMOVE_DIR = join(GITLET_FOLDER, "staging_remove");

    /**
     * file name, Sha1 hashcode of file;
     */
    public String filename;

    /**
     * Staging status
     */
    public String status;

    public Blobs (String filename) {
       this.filename = filename;
    }
    /**
     * Create a general reads in helper method
     * Reads in and deserializes a blob from a file with name FILENAME in given folder
     */
    public static Blobs fromFile(String filename, File DIR) {
        File inFile = Utils.join(DIR, filename);
        if (inFile.exists()) {
            Blobs uddablob = Utils.readObject(inFile, Blobs.class);
            return uddablob;
        }
        return null;
    }

    /**
     * Create a general write-in helper method
     * save file to given folder
     */
    public void saveBlob(File DIR) {
        String filenamehash = Utils.sha1(this);
        File outFile = Utils.join(DIR, filenamehash);
        Blobs uddablob = this;
        Utils.writeObject(outFile,uddablob);
    }

    /**
     * remove all file from Staging area
     */
    public static void clearBlobFromStage() {
        java.util.List<String> workFile = Utils.plainFilenamesIn(STAGE_DIR);
        for (int i = 0; i < workFile.size(); i++) {
            restrictedDelete(workFile.get(i));
        }
    }

    /**
     * Update Staging status
     */
    public void updateStatus(String s) {
        status = s;
    }
}
