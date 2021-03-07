import com.xylope.betriot.layer.service.command.trigger.CommandTrigger;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapLearningTest {
    @Test
    public void testMapKey() {
        Map<CommandTrigger, String> map = new HashMap<>();
        map.put(new CommandTrigger("hello"), "1");
        map.put(new CommandTrigger("world"), "2");
        map.put(new CommandTrigger("hello"), "1copy");
        map.put(new CommandTrigger("groot"), "3");
        for(CommandTrigger trigger : map.keySet()) {
            System.out.println(map.get(trigger));
        }
    }
}
