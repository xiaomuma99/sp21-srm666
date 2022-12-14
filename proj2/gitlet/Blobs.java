package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Repository.BLOBS_FOLDER;
import static gitlet.Utils.*;

public class Blobs implements Serializable {

    /**
     * The current working directory.
     */

    private String blobId; //file name, Sha1 hashcode of file;
    private final byte[] contents; //byte[] contents


    public Blobs(byte[] contents) {
        this.contents = contents;
        this.blobId = calcHash();
    }
    /**
     * Get blobID
     */
    public String getBlobId() {
        return blobId;
    }
    /**
     * Save Blob to file
     */
    public void save() {
        File outFile = join(BLOBS_FOLDER, blobId);
        Utils.writeContents(outFile, contents);
    }

    private String calcHash() {
        return sha1(this.contents);
    }
}
