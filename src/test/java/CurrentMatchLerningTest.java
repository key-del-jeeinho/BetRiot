import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml", "/secretContext.xml"})
public class CurrentMatchLerningTest {
    @Test
    public void testCurrentMatch() {
        CurrentMatch match = Orianna.currentMatchForSummoner(Orianna.summonerNamed("쌤이콜록").get()).get();
        System.out.println(match.exists());
    }
}
