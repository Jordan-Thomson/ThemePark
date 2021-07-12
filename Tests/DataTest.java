import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for Data
 * @author Jordan Thomson - fjb19170
 */
public class DataTest {

    private Data data1 = new Data("Horror");
    private Data data2 = new Data(1.1,1.9);
    private Data data3 = new Data(2,5);
    Ride ride = new Ride("RideName",0.8,1.8,2,4,"N","Y","Y","N","N","ThemeName",10,20,30,40);
    private Data data4 = new Data(ride);

    @Test
    public void getQuestion() {
        assertEquals("Horror",data1.getQuestion());
        assertEquals("Height",data2.getQuestion());
        assertEquals("Group",data3.getQuestion());
        assertEquals(ride.getName(),data4.getQuestion());
    }

    @Test
    public void getRide() {
        assertNull(data1.getRide());
        assertNull(data2.getRide());
        assertNull(data3.getRide());
        assertEquals(ride,data4.getRide());
    }

    @Test
    public void getType() {
        assertEquals(DataType.YESNO,data1.getType());
        assertEquals(DataType.HEIGHT,data2.getType());
        assertEquals(DataType.GROUP,data3.getType());
        assertEquals(DataType.RIDE,data4.getType());
    }

    @Test
    public void isTrue() {
        assertFalse(data1.isTrue(2.4));
        assertFalse(data1.isTrue(0));
        assertFalse(data1.isTrue(-3));
        assertTrue(data2.isTrue(1.5));
        assertFalse(data2.isTrue(0.3));
        assertFalse(data2.isTrue(9));
        assertTrue(data3.isTrue(3));
        assertTrue(data3.isTrue(2));
        assertFalse(data3.isTrue(1));
        assertFalse(data3.isTrue(-3));
        assertFalse(data3.isTrue(9));
        assertFalse(data4.isTrue(2));
        assertFalse(data4.isTrue(0));
        assertFalse(data4.isTrue(-3));
    }
}