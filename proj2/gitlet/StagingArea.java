package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Utils.*;

public class StagingArea implements Serializable {

    public static final File STAGE_FILE = join(Repository.GITLET_DIR, "/staging");

    private HashMap<String, String> addition;//Use Map to store key-pairs <filename, blobID>
    private HashSet<String> removal;
    /** Constructor. */
    public StagingArea() {
        addition = new HashMap<>();
        removal = new HashSet<>();
    }
    /**Recover from file to StageingArea object.*/
    public static StagingArea load() {
        return Utils.readObject(STAGE_FILE, StagingArea.class);
    }
    /**Write into file and saved it to Stage folder.*/
    public void save() {
        Utils.writeObject(STAGE_FILE, this);
    }
    /**Return all added files name-BlobID key pairs. */
    public HashMap<String, String> getAddition() {
        return addition;
    }
    public HashSet<String> getRemoval() {
        return removal;
    }
    /**Clear StagingArea. */
    public void clear() {
        addition.clear();
        removal.clear();
    }
}
