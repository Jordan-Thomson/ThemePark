import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test class for ThemePark
 * @author Jordan Thomson - fjb19170
 */
public class ThemeParkTest {

    private ThemePark tp = new ThemePark();


    @Test
    public void setQuit() {
        tp.setQuit(true);
        assertTrue(tp.isQuit());
    }

    @Test
    public void isQuit() {
        assertFalse(tp.isQuit());
        setQuit();
        assertTrue(tp.isQuit());
    }

    @Test
    public void createParty() {
        tp.createParty("Jordan");
    }

    @Test
    public void getParty() {
        createParty();
        Party p = tp.getParty();
        assertEquals("Jordan",p.getName());
    }

    @Test
    public void displayDetails() {
        tp.setAllRide(false);
        tp.setTestRide();
        tp.createParty("Jordan");
        tp.getParty().setSize(3);
        String[][] prefs = new String[][] {{"Y","Y","N","Y","Y","1.7"},
                                           {"N","N","N","N","Y","1.5"},
                                           {"N","Y","N","N","Y","1.3"}};
        String[][] prefs2 = new String[][] {{"N","N","Y","N","N","2.4"},
                                            {"N","Y","N","Y","Y","0.5"},
                                            {"Y","N","N","Y","Y","1.4"}};
        for (int i = 0; i < 3; i++) {
            for (int j= 0; j < 6; j++) {
                if (j == 5) {
                    tp.setPartyPreference("Height",prefs[i][j],i);
                } else {
                    tp.setPartyPreference(RideType.get(j), prefs[i][j], i);
                }
            }
        }
        tp.initRecommendations();
        tp.displayDetails();
        assertEquals(1,tp.getSuccesses().size());
        //assertEquals(2,tp.getFails().size());
        assertEquals(2,tp.getFailsForRide(tp.getSingleRide()).size());

        for (int i = 0; i < 3; i++) {
            for (int j= 0; j < 6; j++) {
                if (j == 5) {
                    tp.setPartyPreference("Height",prefs2[i][j],i);
                } else {
                    tp.setPartyPreference(RideType.get(j), prefs2[i][j], i);
                }
            }
        }
        tp.initRecommendations();
        tp.displayDetails();
        assertEquals(0,tp.getSuccesses().size());
        assertEquals(2,tp.getFailsForRide(tp.getSingleRide()).size());
        assertEquals((Integer)2,(Integer)tp.getFailsForRide(tp.getSingleRide()).get("Doesn't want Horror attractions"));
    }

    @Test
    public void getAllDetails() {
        tp.setAllRide(true);
        tp.createParty("Jordan");
        tp.getParty().setSize(3);
        String[][] prefs = new String[][] {{"Y","Y","N","Y","Y","1.7"},
                {"N","N","N","N","Y","1.5"},
                {"N","Y","N","N","Y","1.3"}};
        String[][] prefs2 = new String[][] {{"N","N","Y","Y","Y","2.4"},
                {"N","Y","N","Y","Y","0.5"},
                {"Y","N","N","Y","Y","1.4"}};
        for (int i = 0; i < 3; i++) {
            for (int j= 0; j < 6; j++) {
                if (j == 5) {
                    tp.setPartyPreference("Height",prefs[i][j],i);
                } else {
                    tp.setPartyPreference(RideType.get(j), prefs[i][j], i);
                }
            }
        }
        tp.initRecommendations();
        tp.displayDetails();
        assertEquals(1,tp.getSuccesses().size());
        for (Ride r : tp.getSuccesses().keySet()) {
            assertEquals("Tower of Terror",r.getName());
        }

        for (int i = 0; i < 3; i++) {
            for (int j= 0; j < 6; j++) {
                if (j == 5) {
                    tp.setPartyPreference("Height",prefs2[i][j],i);
                } else {
                    tp.setPartyPreference(RideType.get(j), prefs2[i][j], i);
                }
            }
        }
        tp.initRecommendations();
        tp.displayDetails();
        assertEquals(1,tp.getSuccesses().size()); // SauroPods, Iron Jaws, Hall O Mirrors.
        ArrayList<String> rides = new ArrayList<>();
        //rides.add("The Iron Jaws");
        rides.add("Hall O Mirrors");
        //rides.add("SauroPods");
        for (Ride r : tp.getSuccesses().keySet()) {
            assertTrue(rides.contains(r.getName()));
        }
    }

    @Test
    public void testNumRides() {
        assertEquals(20,tp.numRides());
    }

    @Test
    public void testSetRide() {
        tp.setRide(0);
        assertNull(tp.getSingleRide());
        tp.setRide(20);
        assertEquals(tp.getSingleRide().getName(),"The Descent");
        tp.setRide(21);
        // no change
        assertEquals(tp.getSingleRide().getName(),"The Descent");
        tp.setRide(-1);
        // no change
        assertEquals(tp.getSingleRide().getName(),"The Descent");
        tp.setRide(11);
        assertEquals(tp.getSingleRide().getName(),"Rex Rampage");
    }

}