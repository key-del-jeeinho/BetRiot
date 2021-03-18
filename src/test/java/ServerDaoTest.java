import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.ServerVO;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml", "/secretContext.xml"})
public class ServerDaoTest {
    @Autowired
    UserDao userDao;
    List<ServerVO> servers;

    @Before
    public void setUp() {
        servers = new ArrayList<>();
        servers.add(new ServerVO(1, 750638095824846938L, 543218096034431045L, ServerVO.Premium_Level.BASIC.getId()));
        servers.add(new ServerVO(2, 345638095824846938L, 123458096034431045L, ServerVO.Premium_Level.BASIC_PLUS.getId()));
        servers.add(new ServerVO(3, 176638095824846938L, 321548096034431045L, ServerVO.Premium_Level.SPECIAL.getId()));
        servers.add(new ServerVO(4, 999638095824846938L, 765438096034431045L, ServerVO.Premium_Level.BASIC.getId()));
    }

    public void testAdd() {

    }
}
