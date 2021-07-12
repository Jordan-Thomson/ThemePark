/**
 * Class to represent a ride at a themepark
 * @author Jordan - fjb19170
 */
public class Ride  {

    private String name;
    private String theme;
    private String[] types;
    private double[] height;
    private int[] group;
    private int xPos;
    private int yPos;
    private int wait;
    private int rideTime;

    public Ride(String name, double minHeight, double maxHeight, int minGroup, int maxGroup, String wheelchair,
                String horror, String adrenaline, String kids, String water, String theme, int xPos, int yPos, int wait,
                int rideTime) {
        this.name = name;
        types = new String[] {wheelchair, horror,kids,adrenaline,water};
        height = new double[] {minHeight,maxHeight};
        group = new int[] {minGroup,maxGroup};
        this.theme = theme;
        this.xPos = xPos;
        this.yPos = yPos;
        this.wait = wait;
        this.rideTime = rideTime;
    }

    public String getSpecificType(int i) {
        return types[i];
    }

    public double getMinHeight() {
        return height[0];
    }

    public double getMaxHeight() {
        return height[1];
    }

    public int getMinGroup() {
        return group[0];
    }

    public int getMaxGroup() {
        return group[1];
    }

    public String getWheelchair() {
        return types[0];
    }

    public String getAdrenaline() {
        return types[3];
    }

    public String getKids() {
        return types[2];
    }

    public String getWater() {
        return types[4];
    }

    public String getHorror() {
        return types[1];
    }

    public String getTheme() {
        return theme;
    }

    public String getName() {
        return name;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getWait() {
        return wait;
    }

    public int getRideTime() { return rideTime; }

}
