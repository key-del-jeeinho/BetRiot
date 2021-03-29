import com.xylope.betriot.layer.dataaccess.apis.riot.DataDragonAPI;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml", "/secretContext.xml"})
public class DataDragonAPITest {
    @Qualifier("main")
    @Autowired
    DataDragonAPI dataDragonAPI;
    public static final ApplicationContext CONTEXT = new GenericXmlApplicationContext("/applicationContext.xml", "/secretContext.xml");

    @BeforeClass
    public static void setUp() {
    }

    @Test
    public void testGetVersionLast() {
        assertEquals(dataDragonAPI.getVersionLast(), "11.6.1");
    }

    @Test
    public void testGetProfileIconURL() throws MalformedURLException {
        System.out.println(dataDragonAPI.getProfileIconURL(1));
    }

    @Test
    public void testGetChampionIdByKey() {
        assertEquals(dataDragonAPI.getChampionIdByKey(245), "Ekko");
    }
}
