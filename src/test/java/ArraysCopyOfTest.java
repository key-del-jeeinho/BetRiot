import org.junit.Test;

import java.util.Arrays;

public class ArraysCopyOfTest {
    @Test
    public void test() {
        String[] args = {
                "가나다라",
                "1234",
                "ABCD",
                "abcd"
        };

        args = Arrays.copyOfRange(args, 1, args.length);
        for(String arg : args)
            System.out.println(arg);
    }
}
