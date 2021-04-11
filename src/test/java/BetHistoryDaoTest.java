import com.xylope.betriot.layer.domain.dao.BetHistoryDao;
import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.BetHistoryVO;
import com.xylope.betriot.layer.domain.vo.BetParticipantVO;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet.model.WinOrLose;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml", "/betRefactoringContext.xml", "/secretContext.xml"})
public class BetHistoryDaoTest {
    @Autowired
    BetHistoryDao betHistoryDao;
    @Autowired
    UserDao userDao;
    List<BetParticipantVO> betParticipants;

    @Before
    public void setUp() {
        betHistoryDao.removeAll();
        userDao.removeAll();

        userDao.add(new UserVO(1, "1", 1000000));
        userDao.add(new UserVO(3, "2", 1000000));
        userDao.add(new UserVO(7, "3", 1000000));
        userDao.add(new UserVO(2, "4", 1000000));
        userDao.add(new UserVO(8, "5", 1000000));
        userDao.add(new UserVO(1234, "6", 1000000));

        betParticipants = new ArrayList<>();;

        betParticipants.add(new BetParticipantVO(1, 1000, WinOrLose.WIN));
        betParticipants.add(new BetParticipantVO(3, 15000, WinOrLose.LOSE));
        betParticipants.add(new BetParticipantVO(7, 7000, WinOrLose.WIN));
        betParticipants.add(new BetParticipantVO(2, 3000, WinOrLose.LOSE));
        betParticipants.add(new BetParticipantVO(8, 7000, WinOrLose.WIN));
        betParticipants.add(new BetParticipantVO(1234, 9000, WinOrLose.WIN));
    }

    @Test
    public void testAdd() {
        BetHistoryVO betHistory = new BetHistoryVO(
                1, 1, new DateTime(), new DateTime(), WinOrLose.WIN, betParticipants
        );
        betHistoryDao.add(betHistory);
    }
}
