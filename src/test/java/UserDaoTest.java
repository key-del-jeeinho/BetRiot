import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

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
        userDao.add(users.get(1));
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
        userDao.removeAll();

        for (User user : users)
            userDao.add(user);

        List<User> usersOrderByMoney = userDao.getAllOrderByMoney();
        List<User> compareData = new ArrayList<>(users);

        compareData.sort(Comparator.comparingInt(User::getMoney));

        for(int i = 0; i < users.size(); i++) {
            assertEquals(usersOrderByMoney.get(i), compareData.get(i));
        }

        userDao.removeAll();
    }
    @Test
    public void testAdd() {
        userDao.removeAll();

        userDao.add(users.get(0));
        userDao.add(users.get(1));
        assertEquals(userDao.getCount(), 2);

        assertEquals(userDao.get(users.get(0).getDiscordId()),
                users.get(0));
        assertEquals(userDao.get(users.get(1).getDiscordId()),
                users.get(1));

        userDao.removeAll();
    }
    @Test
    public void testRemove() {
        userDao.removeAll();
        userDao.add(users.get(0));
        userDao.remove(users.get(0).getDiscordId());
        userDao.removeAll();
    }
    @Test
    public void testRemoveAll() {
        userDao.removeAll();
        assertEquals(userDao.getCount(), 0);

        for (User user : users)
            userDao.add(user);
        assertEquals(userDao.getCount(), users.size());

        userDao.removeAll();
        assertEquals(userDao.getCount(), 0);
    }
    @Test
    public void testUpdate() {
        userDao.removeAll();

        userDao.add(users.get(0));
        User user = new User(
                users.get(0).getDiscordId(),
                users.get(0).getRiotId(),
                users.get(0).getMoney() +1000
        );
        userDao.update(user);
        assertEquals(
                userDao.get(users.get(0).getDiscordId()).getMoney(),
                users.get(0).getMoney() +1000
        );

        userDao.removeAll();
    }
    @Test
    public void testGetCount() {
        userDao.removeAll();
        assertEquals(userDao.getCount(), 0);
        int i = 0;
        for(User user : users) {
            userDao.add(user);
            assertEquals(userDao.getCount(), ++i);
        }
        userDao.removeAll();
        assertEquals(userDao.getCount(), 0);
    }
    @Test
    public void testIsUserExist() {
        userDao.removeAll();
        userDao.add(users.get(0));
        assertTrue(userDao.isUserExist(users.get(0).getDiscordId()));
        assertFalse(userDao.isUserExist(users.get(1).getDiscordId()));
        userDao.removeAll();
    }
}
