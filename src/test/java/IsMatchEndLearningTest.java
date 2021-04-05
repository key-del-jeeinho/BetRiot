import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

public class IsMatchEndLearningTest {
    //매치종료여부를 계산할 방법에대한 Orianna API 학습테스트
    @Test
    public void testIsMatchEnd() {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("secretContext.xml");
        Orianna.setRiotAPIKey(ctx.getBean("riotApiKey", String.class));
        Orianna.setDefaultPlatform(Platform.KOREA);

        String testerName = "ReveinXI";
        Summoner summoner = Orianna.summonerNamed(testerName).get();
        CurrentMatch currentMatch = summoner.getCurrentMatch();
        Match match = Orianna.matchWithId(5111351780L).get();
        System.out.println(match.getCreationTime());
    }
}