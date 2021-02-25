import com.xylope.betriot.layer.service.discord.listener.EventRepeater;
import com.xylope.betriot.layer.service.discord.listener.RepeatListener;
import net.dv8tion.jda.api.events.GenericEvent;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class EventRepeaterTest {
    @Test
    public void testEventRepeater() {
        GenericEvent event = mock(GenericEvent.class);
        MockEventRepeater<GenericEvent> eventRepeater = new MockEventRepeater<>();
        AtomicInteger[] testNum = {new AtomicInteger(), new AtomicInteger(), new AtomicInteger(), new AtomicInteger()};
        eventRepeater.addListener((e)-> testNum[0].set(1));
        eventRepeater.addListener((e)-> testNum[1].set(2));
        eventRepeater.addListener((e)-> testNum[2].set(3));
        eventRepeater.addListener((e)-> testNum[3].set(4));
        eventRepeater.repeatEvent();
        for (int i = 0; i < 4; i++)
            assertEquals(testNum[i].get(), i+1);
    }

    private static class MockEventRepeater<T extends GenericEvent> implements EventRepeater<T> {
        private final List<RepeatListener<T>> listeners;

        public MockEventRepeater() {
            listeners = new ArrayList<>();
        }

        public void repeatEvent() {
            repeatEvent(listeners, null);
        }

        @Override
        public void addListener(RepeatListener<T> listener) {
            listeners.add(listener);
        }
    }
}
