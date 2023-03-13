import java.util.Arrays;
import java.util.Objects;

public class Utils {
    public static final String ROOT = "C:/Users/dhruv/Desktop/cs251p3/startercode/src";

    public static void checkEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual))
            throw new AssertionError(String.format("%s!\n - Expected: %s\n - Actual: %s", message, expected, actual));
    }

    public static void checkAlmostEquals(double expected, double actual, double eps, String message) {
        if (Math.abs(actual - expected) > eps)
            throw new AssertionError(String.format("%s!\n - Expected: %s\n - Actual: %s", message, expected, actual));
    }

    public static void checkArrayEquals(Object[] expected, Object[] actual, String message) {
        if (!Arrays.deepEquals(expected, actual))
            throw new AssertionError(String.format("%s!\n - Expected: %s\n - Actual: %s",
                    message, Arrays.deepToString(expected), Arrays.deepToString(actual)));
    }
}
