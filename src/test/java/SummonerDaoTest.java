import com.xylope.betriot.data.riotdata.SummonerDto;
import com.xylope.betriot.layer.dataaccess.SummonerDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml", "/secretContext.xml"})
public class SummonerDaoTest {
    @Autowired
    SummonerDao summonerDao;
    SummonerDto testSummoner;

    @Before
    public void setUp() {
        testSummoner = SummonerDto.builder()
                .id("nXAQ7K_nuISCFYM-4FwAbz-FOBVANB9lICXpE3I6lbRC28o")
                .accountId("fmK-bR76A5c_J9PlX7Kmr0m9wNIY-KTKs9kmZuBJBC9grPE4UVJpuMQV")
                .puuid("yUZtK3XtKhyiCxX-D8zsBR95egK7QidscxwmhhXsV33vWtERKJBvMwySi-g72hPPCUc5ykjuCOJu7w")
                .name("엄준식사하셧나요")
                .profileIconId(4777)
                .revisionDate(1613475547000L)
                .summonerLevel(66)
                .build();
    }

    @Test
    public void testGetByAccountId() {
        SummonerDto summoner = summonerDao.getByAccountId(testSummoner.getAccountId());
        assertEquals(summoner.getAccountId(), testSummoner.getAccountId());
        assertEquals(summoner.getId(), testSummoner.getId());
        assertEquals(summoner.getPuuid(), testSummoner.getPuuid());
    }

    @Test
    public void testGetByName() {
        SummonerDto summoner = summonerDao.getByName(testSummoner.getName());
        assertEquals(summoner.getAccountId(), testSummoner.getAccountId());
        assertEquals(summoner.getId(), testSummoner.getId());
        assertEquals(summoner.getPuuid(), testSummoner.getPuuid());
    }

    @Test
    public void testGetByPuuid() {
        SummonerDto summoner = summonerDao.getByPuuid(testSummoner.getPuuid());
        assertEquals(summoner.getAccountId(), testSummoner.getAccountId());
        assertEquals(summoner.getId(), testSummoner.getId());
        assertEquals(summoner.getPuuid(), testSummoner.getPuuid());
    }

    @Test
    public void testGetById() {
        SummonerDto summoner = summonerDao.getById(testSummoner.getId());
        assertEquals(summoner.getAccountId(), testSummoner.getAccountId());
        assertEquals(summoner.getId(), testSummoner.getId());
        assertEquals(summoner.getPuuid(), testSummoner.getPuuid());
    }
}
