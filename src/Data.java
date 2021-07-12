/**
 * Data structure to be used as the decision tree element, allowing tree to hold varying data
 * types and return true/false statements.
 * @author Jordan - fjb19170
 */
public class Data {

    private DataType t;
    private double minHeight;
    private double maxHeight;
    private int minGroup;
    private int maxGroup;
    private String question;
    private Ride ride;

    /**
     * Constructor for height type data (doubles)
     * @param minHeight the minimum height of a ride
     * @param maxHeight the maximum height of a ride
     */
    public Data(double minHeight, double maxHeight) {
        t = DataType.HEIGHT;
        question = "Height";
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        ride = null;
    }

    /**
     * Constructor for group type data (ints)
     * @param minGroup the minimym group size for a ride
     * @param maxGroup the maximum group size for a ride
     */
    public Data(int minGroup, int maxGroup) {
        t = DataType.GROUP;
        question = "Group";
        this.minGroup = minGroup;
        this.maxGroup = maxGroup;
        ride = null;
    }

    /**
     * String type data for yes no answers and to hold
     * information about leaf nodes
     * @param question
     */
    public Data(String question) {
        t = DataType.YESNO;
        this.question = question;
        ride = null;
    }

    /**
     * Constructor for a Data type of ride, should only be used
     * as a leaf node.
     * @param ride
     */
    public Data(Ride ride) {
        t = DataType.RIDE;
        this.ride = ride;
        question = ride.getName();
    }

    /**
     * Method to get a yes/no style answer for tree traversal
     * @param height value to be checked
     * @return true if the supplied value is between the min and max
     */
    public boolean isTrue(double height) {
        if (minHeight == 0 && maxHeight == 0) return false;
        return (height <= maxHeight && height >= minHeight);
    }

    /**
     * Method to get a yes/no style answer for tree traversal
     * @param group value to be checked
     * @return true if the supplied value is between the min and max
     */
    public boolean isTrue(int group) {
        if (minGroup == 0 && maxGroup == 0) return false;
        return (group <= maxGroup && group >= minGroup);
    }


    public String getQuestion() {
        return question;
    }

    public Ride getRide() {
        return ride;
    }

    public DataType getType() {
        return t;
    }

}
