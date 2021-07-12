import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to represent a group of people visiting a themepark
 * @author Jordan - fjb19170
 */
public class Party {

    private int size;
    private String name;
    private HashMap<String,String>[] preferences;
    private String email;
    private Ride firstRide;
    private Ride lastRide;
    private ArrayList<Ride> repeatRides;

    /**
     * Constructor for a party at a theme park
     * @param name String name of the head of the party
     */
    public Party(String name) {
        this.name = name;
        email = "";
        firstRide = null;
        lastRide = null;
        repeatRides = new ArrayList<>();
    }

    /**
     * Sets the size of the party, uses value to init size of
     * preferences array.
     * if size is < 0 does nothing.
     * @param size the size to be set.
     */
    public void setSize(int size) {
        if (size >= 0) {
            this.size = size;
            preferences = new HashMap[size];
            for (int i = 0; i < size; i++) {
                preferences[i] = new HashMap<>();
            }
        }
    }


    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    /**
     * returns the value of a preference for a specific person
     * @param type the type of preference to return
     * @param person the person whose preference is to be returned
     * @return the value of the preference.
     */
    public String getPreference(String type, int person) {
        if (size > person) {
            if (preferences[person].get(type) != null) {
                return preferences[person].get(type);
            }
        }
        return null;
    }

    /**
     * Sets a preference of a specific member of the party
     * @param type The type of preference
     * @param value The value of the preference
     * @param person the person in the group.
     */
    public void setPreference(String type, String value, int person) {
        if (person > size || size == 0) {
            throw new IllegalArgumentException("No person defined");
        } else {
            preferences[person].put(type, value);
        }
    }

    /**
     * Verifies email format
     * Regex credit: https://mailtrap.io/blog/java-email-validation/
     * @param email String email address to be verified for format
     * @return email address if verified otherwise an empty string.
     */
    public String setEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            this.email = email;
            return email;
        }
        else {
            return "";
        }
    }

    public String getEmail() {
        return email;
    }

    public void setFirstRide(Ride ride) {
        firstRide = ride;
    }

    public void setLastRide(Ride ride) {
        lastRide = ride;
    }

    public void addRepeatRide(Ride ride) {
        repeatRides.add(ride);
    }

    public Ride getFirstRide() {
        return firstRide;
    }

    public Ride getLastRide() {
        return lastRide;
    }

    public ArrayList<Ride> getRepeatRides() {
        return repeatRides;
    }
}
