import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LoggerTest {
    @Test
    public void testLoggerMessage() {
        Logger logger = LogManager.getLogger(this.getClass());
        logger.log(Level.WARN, "warn");
    }
}
