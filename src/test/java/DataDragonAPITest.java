import com.xylope.betriot.layer.dataaccess.DataDragonAPI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml", "/secretContext.xml"})
public class DataDragonAPITest {
    @Autowired
    DataDragonAPI dataDragonAPI;
    @Test
    public void testGetVersionLast() {
        assertEquals(dataDragonAPI.getVersionLast(), "11.4.1");
    }

    @Test
    public void testGetProfileIconURL() throws MalformedURLException {
        System.out.println(dataDragonAPI.getProfileIconURL(1).toString());
    }

    @Test
    public void testGetChampionIdByKey() {
        dataDragonAPI.getChampionIdByKey(255);
    }
}
