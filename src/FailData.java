import java.util.HashMap;

/**
 * Class to help collate reasons for a fail
 * @author Jordan Thomson - fjb19170
 */
public class FailData {


    private HashMap<String, Integer> failures;
    private int totalFails;

    /**
     * Basic constructor
     */
    public FailData() {
        failures = new HashMap<>();
        totalFails = 0;
    }

    /**
     * Adds a failure, if the failure already exists increments the count
     * @param failReason
     */
    public void addFailure(String failReason) {
        if (failures.containsKey(failReason)) {
            failures.replace(failReason,failures.get(failReason) + 1);
        } else {
            failures.put(failReason,1);
        }
        totalFails++;
    }

    /**
     * returns the hashmap of failures for this set of data
     * @return Hashmap of failures and their count
     */
    public HashMap<String, Integer> getFailures() {
        return failures;
    }

    /**
     * returns the total number of failures
     * @return the total number of failures
     */
    public int getTotalFails() {
        return totalFails;
    }
}
