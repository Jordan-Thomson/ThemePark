import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Test class for FailData
 * @author Jordan Thomson - fjb19170
 */

public class FailDataTest {

    FailData fd = new FailData();

    @Test
    public void addFailure() {
        assertEquals(0,fd.getTotalFails());
        fd.addFailure("Reason 1");
        fd.addFailure("Reason 2");
        assertEquals(2,fd.getTotalFails());
        fd.addFailure("Reason 3");
        assertEquals(3,fd.getTotalFails());
        fd.addFailure("Reason 1");
        assertEquals(4,fd.getTotalFails());
    }

    @Test
    public void getFailures() {
        addFailure();
        HashMap<String,Integer> fails = fd.getFailures();
        assertEquals(2,fails.get("Reason 1").intValue());
        assertEquals(1,fails.get("Reason 2").intValue());
        assertEquals(3,fails.size());

    }
}