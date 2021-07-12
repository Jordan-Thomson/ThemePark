import java.util.HashMap;
import java.util.Map;

/**
 * Enum class to help identify ride types, allows getting textual reference by integer order.
 * @author Jordan - fjb19170
 */

public enum RideType {
    WHEELCHAIR("Wheelchair",0),
    HORROR("Horror",1),
    KIDS("Kids",2),
    ADRENALINE("Adrenaline",3),
    WATER("Water",4);

    private String desc;
    private int order;

    RideType(String description, int i) {
        this.desc = description;
        this.order = i;

    }

    public String getDesc() {
        return desc;
    }

    private static final Map<Integer,String> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(RideType rideType : RideType.values())
        {
            lookup.put(rideType.order, rideType.getDesc());
        }
    }

    //This method can be used for reverse lookup purpose
    public static String get(int i)
    {
        return lookup.get(i);
    }
}
