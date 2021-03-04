import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.xylope.betriot.BetRiotApplication;
import com.xylope.betriot.layer.dataaccess.DataDragonAPI;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @BeforeClass
    public static void setUp() {
        Orianna.setRiotAPIKey(BetRiotApplication.getContext().getBean(String.class));
        Orianna.setDefaultPlatform(Platform.KOREA);
    }

    @Test
    public void testGetVersionLast() {
        assertEquals(dataDragonAPI.getVersionLast(), "11.5.1");
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
