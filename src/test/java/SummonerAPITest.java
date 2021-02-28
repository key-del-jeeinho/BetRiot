import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import com.xylope.betriot.layer.dataaccess.SummonerAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml", "/secretContext.xml"})
public class SummonerAPITest {
    @Autowired
    SummonerAPI summonerAPI;
    SummonerDto testSummoner;

    @Before
    public void setUp() {
        testSummoner = SummonerDto.builder()
                .id("zYB6KP19JlRLGII2035PPhMxmqugF9ZfIqkHCmbPefMHbEg")
                .accountId("D9vN4A3-YFyW6CiNlWnBsSzckOQE-DBS59kKqn4PpeaZyLO-NbUNx3s8")
                .puuid("mMo9n9ZnK48C8XyKZ3AweGOtWpa7qE-dhnd-ON4sEyexpdPAZeLpzFRKV7MVhsOp8ystb3H4JY3Jaw")
                .name("엄준식사하셧나요")
                .profileIconId(4777)
                .revisionDate(1613475547000L)
                .summonerLevel(66)
                .build();
    }

    @Test
    public void testGetByAccountId() {
        SummonerDto summoner = summonerAPI.getByAccountId(testSummoner.getAccountId());
        assertEquals(summoner.getAccountId(), testSummoner.getAccountId());
        assertEquals(summoner.getId(), testSummoner.getId());
        assertEquals(summoner.getPuuid(), testSummoner.getPuuid());
    }

    @Test
    public void testGetByName() {
        SummonerDto summoner = summonerAPI.getByName(testSummoner.getName());
        assertEquals(summoner.getAccountId(), testSummoner.getAccountId());
        assertEquals(summoner.getId(), testSummoner.getId());
        assertEquals(summoner.getPuuid(), testSummoner.getPuuid());
    }

    @Test
    public void testGetByPuuid() {
        SummonerDto summoner = summonerAPI.getByPuuid(testSummoner.getPuuid());
        assertEquals(summoner.getAccountId(), testSummoner.getAccountId());
        assertEquals(summoner.getId(), testSummoner.getId());
        assertEquals(summoner.getPuuid(), testSummoner.getPuuid());
    }

    @Test
    public void testGetById() {
        SummonerDto summoner = summonerAPI.getById(testSummoner.getId());
        assertEquals(summoner.getAccountId(), testSummoner.getAccountId());
        assertEquals(summoner.getId(), testSummoner.getId());
        assertEquals(summoner.getPuuid(), testSummoner.getPuuid());
    }
}
