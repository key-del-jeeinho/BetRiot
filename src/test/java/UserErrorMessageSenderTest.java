import com.xylope.betriot.layer.service.user.message.UserErrorMessageSender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.internal.requests.restaction.MessageActionImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml", "/secretContext.xml"})
public class UserErrorMessageSenderTest {
    @Autowired
    UserErrorMessageSender sender;
    @Mock
    PrivateChannel pc;
    @Mock
    MessageAction messageAction;

    @Test
    public void testSendMessage() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(pc);

        when(pc.sendMessage(any(MessageEmbed.class))).thenReturn(messageAction);
        sender.sendMessage(pc, "@Test");
        verify(pc, atMost(1)).sendMessage(any(String.class));
    }
}