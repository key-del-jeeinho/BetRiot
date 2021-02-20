import com.xylope.betriot.BetRiotApplication;

import javax.security.auth.login.LoginException;

public class ApplicationBooter {
    public static void main(String[] args) throws LoginException, InterruptedException {
        new BetRiotApplication(args[0]).start();
    }
}
