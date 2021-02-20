import com.xylope.betriot.layer.logic.dao.UserDao;
import com.xylope.betriot.layer.logic.vo.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml", "/secretContext.xml"})
public class UserDaoTest {
    @Autowired
    UserDao userDao;
    List<User> users;

    @Before
    public void setUp() {
        users = new ArrayList<>();
        users.add(new User(553155577228951552L, "asdfasdf", 0));
        users.add(new User(153155576228951552L, "agdfadfg", 0));
        users.add(new User(366087289190875137L, "jdfcys", 538900));
        users.add(new User(716555267604480001L, "euyhtrjhryfd", 538901));
    }

    @Test
    public void testGetByDiscordId() {
        userDao.removeAll();
        userDao.add(users.get(0));
        assertEquals(userDao.get(users.get(0).getDiscordId()),
                users.get(0));
        userDao.removeAll();
    }
    @Test
    public void testGetAll() {
        userDao.removeAll();

        for (User user : users)
            userDao.add(user);
        for(int i = 0; i < users.size(); i++) {
            List<User> usersToGetAll = userDao.getAll();
            usersToGetAll.retainAll(users); //usersToGetALl 과 users 간의 교집합을 usersToGetAll 에 저장
            //만약 두 리스트 내의 요소값이 동일하다면 해당 요청 실행후 두 리스트의 크기는 동일함
            assertEquals(usersToGetAll.size(), users.size());
        }

        userDao.removeAll();
    }
    @Test
    public void testGetAllOrderByMoney() {

    }
    @Test
    public void testAdd() {

    }
    @Test
    public void testRemove() {

    }
    @Test
    public void testRemoveAll() {

    }
    @Test
    public void testUpdate() {

    }
    @Test
    public void testGetCount() {

    }
    @Test
    public void testIsUserExist() {

    }
}
