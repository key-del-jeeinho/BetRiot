import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.dao.UserDaoJdbc;
import com.xylope.betriot.layer.domain.vo.UserVO;
import lombok.NonNull;
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
    List<UserVO> users;

    @Before
    public void setUp() {
        users = new ArrayList<>();
        users.add(new UserVO(553155577228951552L, "asdfasdf", 0, UserVO.Permission.GOLD));
        users.add(new UserVO(153155576228951552L, "agdfadfg", 0, UserVO.Permission.GOLD));
        users.add(new UserVO(366087289190875137L, "jdfcys", 538900, UserVO.Permission.IRON));
        users.add(new UserVO(716555267604480001L, "euyhtrjhryfd", 538901, UserVO.Permission.CHALLENGER));
        users.add(new UserVO(22635577228951552L, "rjth", 0, UserVO.Permission.GOLD));
        users.add(new UserVO(987375576228951552L, "xzc", 0, UserVO.Permission.GOLD));
        users.add(new UserVO(489227289190875137L, "erw", 0, UserVO.Permission.DIAMOND));
        users.add(new UserVO(112835267604480001L, "asd", 0, UserVO.Permission.CHALLENGER));
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

        for (UserVO user : users)
            userDao.add(user);
        for(int i = 0; i < users.size(); i++) {
            List<UserVO> usersToGetAll = userDao.getAll();
            usersToGetAll.retainAll(users); //usersToGetALl 과 users 간의 교집합을 usersToGetAll 에 저장
            //만약 두 리스트 내의 요소값이 동일하다면 해당 요청 실행후 두 리스트의 크기는 동일함
            assertEquals(usersToGetAll.size(), users.size());
        }

        userDao.removeAll();
    }
    @Test
    public void testGetAllOrderByMoney() {
        userDao.removeAll();

        userDao.add(users.toArray(new UserVO[0]));

        List<UserVO> usersOrderByMoney = userDao.getAllOrderByMoney();
        List<UserVO> compareData = new ArrayList<>(users);

        compareData.sort(Comparator.comparingInt(UserVO::getMoney));

        int size = users.size();

        for(int i = 0; i < size; i++) {
            assertEquals(usersOrderByMoney.get(i).getMoney(), compareData.get(size - i - 1).getMoney());
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
    public void testAddAll() {
        userDao.removeAll();

        userDao.add(users.toArray(new UserVO[0]));

        userDao.add();
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

        userDao.add(users.toArray(new UserVO[0]));
        assertEquals(userDao.getCount(), users.size());

        userDao.removeAll();
        assertEquals(userDao.getCount(), 0);
    }
    @Test
    public void testUpdate() {
        userDao.removeAll();

        userDao.add(users.get(0));
        UserVO user = new UserVO(
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
        for(UserVO user : users) {
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
    @Test
    public void testGetByPermission() { //Todo 테스트 구현
        userDao.removeAll();
        //userDao = new TestUserDao();

        userDao.add(users.toArray(new UserVO[0]));
        userDao.removeAll();

    }
    @Test
    public void testGetAllOrderByPermission() {
        userDao.removeAll();

        userDao.add(users.toArray(new UserVO[0]));
        List<UserVO> sortedUsers =userDao.getAllOrderByPermission();

        //sorting 알고리즘을 활용한 정렬여부 검사
        for(int i = 0; i < sortedUsers.size()-1; i++) {
            int prev = sortedUsers.get(i).getPermission().getId();
            int next = sortedUsers.get(i+1).getPermission().getId();
            assertTrue(prev >= next);
        }
    }
    @Test
    public void testIsPermission() {
        userDao.removeAll();

        UserVO testUser = users.get(0);
        userDao.add(testUser);

        testUser.setPermission(UserVO.Permission.GOLD);
        UserVO.Permission permission = testUser.getPermission();
        assertTrue(userDao.isPermission(testUser.getDiscordId(), permission));
        //UserVO.Permission = userDao
        userDao.removeAll();
    }

    @Test
    public void testCheckPermission() {
        UserVO testUser = users.get(0);
        UserVO.Permission permission = testUser.getPermission();
        UserVO.Permission permission2 = UserVO.Permission.getById(permission.getId() - 1); //Silver
        UserVO.Permission permission3 = UserVO.Permission.getById(permission.getId() + 1); //Platinum

        userDao.removeAll();

        userDao.add(testUser);
        assertTrue(userDao.checkPermission(testUser.getDiscordId(), permission)); //Gold 인 유저가 Gold 이상 사용할수 있는 컨텐츠에 접근할 수 있는지
        assertTrue(userDao.checkPermission(testUser.getDiscordId(), permission2)); //Gold 인 유저가 Silver 이상 사용할수 있는 컨텐츠에 접근할 수 있는지
        assertFalse(userDao.checkPermission(testUser.getDiscordId(), permission3)); //Gold 인 유저가 Platinum 이상 사용할수 있는 컨텐츠에 접근할 수 있는지
    }

    private static class TestUserDao extends UserDaoJdbc {
        @Override
        public void add(UserVO user) {
            throw new RuntimeException();
        }
    }
}
