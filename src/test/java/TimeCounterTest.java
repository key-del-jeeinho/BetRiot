import com.xylope.betriot.layer.domain.event.OnMinuteEvent;
import com.xylope.betriot.layer.domain.event.OnSecondEvent;
import com.xylope.betriot.manager.TimeCounter;
import com.xylope.betriot.manager.TimeListenerAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

public class TimeCounterTest {
    private TimeCounter timeCounter;

    @Before
    public void setUp() {
        timeCounter = new TimeCounter();
    }

    @Test
    public void testTimeEventCall() throws InterruptedException {
        timeCounter.addTimeListener(new TimeListenerAdapter() {
            @Override
            public void onTimeSecond(OnSecondEvent e) {
                System.out.println(e.getCount() + "초");
            }

            @Override
            public void onTimeMinute(OnMinuteEvent e) {
                System.out.println(e.getCount() + "분");
            }
        });
        assertEquals(timeCounter.getListenerCount(), 1);
        System.out.println(timeCounter.getListenerCount());
        timeCounter.run();
        Thread.sleep(70000);
    }

    @Test
    public void testRunning() throws InterruptedException {
        timeCounter.addTimeListener(
                new TimeListenerAdapter() {
                    @Override
                    public void onTimeSecond(OnSecondEvent e) {
                        System.out.println("running");
                    }
                });
        timeCounter.run();

        Thread.sleep(1000 * 3);
        timeCounter.setRunning(false);
        Thread.sleep(1000 * 3);
        timeCounter.setRunning(true);
        Thread.sleep(1000 * 3);
    }

    @Test
    public void testCount() throws InterruptedException {
        timeCounter.addTimeListener(
                new TimeListenerAdapter() {
                    int count = 0;
                    @Override
                    public void onTimeSecond(OnSecondEvent e) {
                        count++;
                        if(count % 3 == 0) {
                            System.out.println("count = " + count);
                        }
                    }
                }
        );
        timeCounter.run();
        Thread.sleep(1000 * 20);
    }
}
