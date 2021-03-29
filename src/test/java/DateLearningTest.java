import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

public class DateLearningTest {
    @Test
    public void test() {
        Date date = new Date();
        System.out.println(date.getTime());
        System.out.println(new DateTime().isAfter(date.getTime()));
        while (true) System.out.println(new Date().getTime());
    }
}
