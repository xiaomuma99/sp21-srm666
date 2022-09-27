package capers;

import java.io.File;
import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers");
    static final File DOG_FOLDER = Utils.join(CAPERS_FOLDER, "dogs");
    // TODO Hint: look at the `join`
                                            //      function in Utils

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        // TODO
        CAPERS_FOLDER.mkdir();
        DOG_FOLDER.mkdir();
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     * In the beginning, I don't know how to append a string to another.
     * Step1 read existing file, step2 plus content and write
     */
    public static void writeStory(String text) {
        // TODO
        File outFile = join(CAPERS_FOLDER,"story");
        String tmpStory;
        if (!outFile.exists()) {
            tmpStory = text;
        } else {
            tmpStory = readContentsAsString(outFile);
            tmpStory = tmpStory + "\n" + text;
        }
        writeContents(outFile, tmpStory);
        System.out.println(tmpStory);

    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        // TODO
        Dog uddadog = new Dog(name, breed, age);
        uddadog.saveDog();
        System.out.println(uddadog.toString());

    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        // TODO
        Dog uddadog = Dog.fromFile(name);
        if (uddadog != null) {
            uddadog.haveBirthday();
            uddadog.saveDog();
        }
    }
}
