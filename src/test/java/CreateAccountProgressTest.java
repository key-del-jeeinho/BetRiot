import com.xylope.betriot.layer.service.user.account.create.CreateAccountProgress;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CreateAccountProgressTest {
    List<CreateAccountProgress> progressList;

    @Before
    public void setUp() {
        progressList = new ArrayList<>();
        progressList.addAll(Arrays.asList(CreateAccountProgress.values()));
    }

    @Test
    public void testGetNextStep() {
        for(int i = 0; i < progressList.size()-1; i++) {
            assertEquals(progressList.get(i).getNextStep(), progressList.get(i+1));
        }
    }
}
