import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Platform;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.*;

public class IsMatchEndLearningTest {
    //매치종료여부를 계산할 방법에대한 Orianna API 학습테스트
    @Test
    public void testIsMatchEnd() throws InterruptedException {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("secretContext.xml");
        Orianna.setRiotAPIKey(ctx.getBean("riotApiKey", String.class));
        Orianna.setDefaultPlatform(Platform.KOREA);

        String testerName = "ReveinXI";
        Summoner summoner = Orianna.summonerNamed(testerName).get();
        CurrentMatch currentMatch = summoner.getCurrentMatch();
        Match match = Orianna.matchWithId(currentMatch.getId()).get();
        assertTrue(currentMatch.exists()); //게임이 실행중인가
        assertFalse(match.exists()); //테스트가 성공했는가
        final int[] cnt = {0};
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(cnt[0]%60 == 0) {
                    System.out.println("hello" + cnt[0]);
                    //리퀘스트 수 제한떄문에 1분마다 한번씩 실행
                    if(!summoner.getCurrentMatch().exists()) { //현재 플레이어가 게임을 진행하고있지 않을경우
                        Match testMatch = Orianna.matchWithId(currentMatch.getId()).get(); //이전에 진행중이던 게임의 아이디를 통해 매치를 찾는다
                        assertTrue(testMatch.exists()); //해당 매치가 있는지 검사한다
                        System.exit(700);
                    }
                }
                cnt[0]++;
            }
        }, 0L, 1000L);
        while (true);
    }
}