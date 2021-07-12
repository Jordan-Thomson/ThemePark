import org.junit.Test;

import static org.junit.Assert.*;

public class MenuTest {

    private String title = "Test Menu";
    private String options[] = new String[] {"1. option 1","2. option 2","3. option 3"};
    private Menu testMenu = new Menu(title,options);

    @Test
    public void testMenu() {
        assertEquals(3,testMenu.getNumOptions());
    }

}