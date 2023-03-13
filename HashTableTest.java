import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;



/*


Below, we provide pertinent remarks on the test cases. Cases 1 through 12 are insertion tests and can be passed without implementing contains(). Cases 13 and 14 are containment tests and require correct implementations of both insert() and contains(). DO NOT EXPECT THIS FOR UPCOMING PROJECTS!

Small case for visualization. No duplicates, overflow, collisions, or cycles.
This test case uses a capacity-12 hash table with crc32 hashing. The small capacity is well-suited for visualizing or hand-calculating state changes. Each of the 12 unique input strings hashes to a different index, so this case can be passed without implementing open addressing or numCollisions().


Prime table capacity. May have collisions. No duplicates, overflow, or cycles.
This test case checks the hash table’s behaviour when the capacity is a prime number. We use a capacity-59 hash table with crc32 hashing. Some of the 59 unique input strings may hash to the same index, so open addressing must be correctly implemented to pass this and all subsequent cases. However, no infinite loops will occur when using double hashing, so this case can be passed without implementing an infinite loop detector.


Composite table capacity. May have collisions. No duplicates, overflow, or cycles.
Similar to test case 2, but uses a capacity-60 hash table to check behaviour when the capacity is a composite number with many divisors.


Overflow. May have collisions. No duplicates or cycles.
This is the first test case with more input strings than the capacity of the hash table. Insertions on a full hash table must be rejected to pass this case.


Duplicates. May have collisions. No overflow or cycles.
This is the first test case with duplicate strings in the input file. Duplicate-handling logic must be implemented to pass this case.


Force collisions. No duplicates, overflow, or cycles.
This case stress-tests open addressing by inserting unique strings that all hash to the same index. However, no infinite loops will occur when using double hashing.


Force cycles. No duplicates or overflow.
This case uses crc32-dh hashing and stress-tests infinite loop detection by inserting unique strings resulting in probe sequences which cycle through a strict subset of the hash table’s indices. Thus, some strings cannot be inserted, even though the hash table never becomes full. An infinite loop detector must be implemented to pass this case.


Force trivial cycles. No duplicates or overflow.
Inserts unique strings whose hash2 value is a multiple of the hash table capacity, resulting in open addressing cycling through the same index repeatedly. Only one string can ever be inserted.


Medium-scale insertion tests covering all hash types. No duplicates.
General test case inserting various unique strings into hash tables of capacity 1000 (a composite number), 1009 (a prime number), and 1024 (a power of two) to holistically test all aspects of the implementation. Each of the eight hash types is tested separately.


Medium-scale insertion tests covering all hash types. Contains duplicates.
Similar to test case 9, but with duplicate strings in the input files.


Medium-scale cycle-forcing test case.
Similar to test case 7, but with a larger table capacity.


Medium-scale trivial-cycle-forcing test case.
Similar to test case 8, but with a larger table capacity.


Medium-scale containment tests, without duplicate inserts.
Holistic containment test on hash tables of capacity 1000, 1009, and 1024. Each of the eight hash types is tested separately.


Medium-scale containment tests, with duplicate inserts.
Similar to test case 13, but with duplicate strings in the input files.

*/



public class HashTableTest {
    private static final List<Integer> MEDIUM_CAPACITIES = Arrays.asList(1000, 1009, 1024);
    private static final List<String> HASH_TYPES = Arrays.asList(
            "crc32-lp", "crc32-dh", "adler32-lp", "adler32-dh", "murmur3-lp", "murmur3-dh", "poly-lp", "poly-dh");

    @Test
    @DisplayName("1. Small case for visualization. No duplicates, overflow, collisions, or cycles.")
    public void testCase1() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            new InsertTest("small_basic", 12, "crc32-lp", "small_basic_lp").run();
            new InsertTest("small_basic", 12, "crc32-dh", "small_basic_dh").run();
        });
    }

    @Test
    @DisplayName("2. Prime table capacity. May have collisions. No duplicates, overflow, or cycles.")
    public void testCase2() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            new InsertTest("small_prime", 59, "crc32-lp", "small_prime_lp").run();
            new InsertTest("small_prime", 59, "crc32-dh", "small_prime_dh").run();
        });
    }

    @Test
    @DisplayName("3. Composite table capacity. May have collisions. No duplicates, overflow, or cycles.")
    public void testCase3() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            new InsertTest("small_composite", 60, "crc32-lp", "small_composite_lp").run();
            new InsertTest("small_composite", 60, "crc32-dh", "small_composite_dh").run();
        });
    }

    @Test
    @DisplayName("4. Overflow. May have collisions. No duplicates or cycles.")
    public void testCase4() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            new InsertTest("small_overflow", 50, "crc32-lp", "small_overflow_lp").run();
            new InsertTest("small_overflow", 50, "crc32-dh", "small_overflow_dh").run();
        });
    }

    @Test
    @DisplayName("5. Duplicates. May have collisions. No overflow or cycles.")
    public void testCase5() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            new InsertTest("small_duplicates", 100, "crc32-lp", "small_duplicates_lp").run();
            new InsertTest("small_duplicates", 100, "crc32-dh", "small_duplicates_dh").run();
        });
    }

    @Test
    @DisplayName("6. Force collisions. No duplicates, overflow, or cycles.")
    public void testCase6() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            new InsertTest("small_collisions", 28, "crc32-lp", "small_collisions_lp").run();
            new InsertTest("small_collisions", 28, "crc32-dh", "small_collisions_dh").run();
        });
    }

    @Test
    @DisplayName("7. Force cycles. No duplicates or overflow.")
    public void testCase7() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            new InsertTest("small_cycles", 14, "crc32-dh", "small_cycles").run();
        });
    }

    @Test
    @DisplayName("8. Force trivial cycles. No duplicates or overflow.")
    public void testCase8() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            new InsertTest("small_trivial_cycles", 14, "crc32-dh", "small_trivial_cycles").run();
        });
    }

    @Test
    @DisplayName("9. Medium-scale insertion tests covering all hash types. No duplicates.")
    public void testCase9() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            for (int capacity : MEDIUM_CAPACITIES)
                for (String hashType : HASH_TYPES)
                    new InsertTest(String.format("medium-%s_unique", capacity), capacity, hashType,
                            String.format("medium-%s_unique_%s", capacity, hashType)).run();
        });
    }

    @Test
    @DisplayName("10. Medium-scale insertion tests covering all hash types. Contains duplicates.")
    public void testCase10() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            for (int capacity : MEDIUM_CAPACITIES)
                for (String hashType : HASH_TYPES)
                    new InsertTest(String.format("medium-%s_duplicates", capacity), capacity, hashType,
                            String.format("medium-%s_duplicates_%s", capacity, hashType)).run();
        });
    }

    @Test
    @DisplayName("11. Medium-scale cycle-forcing test case.")
    public void testCase11() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            new InsertTest("medium_cycles", 300, "crc32-dh", "medium_cycles").run();
        });
    }

    @Test
    @DisplayName("12. Medium-scale trivial-cycle-forcing test case.")
    public void testCase12() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            new InsertTest("medium_trivial_cycles", 300, "crc32-dh", "medium_trivial_cycles").run();
        });
    }

    @Test
    @DisplayName("13. Medium-scale containment tests, without duplicate inserts.")
    public void testCase13() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            for (int capacity : MEDIUM_CAPACITIES)
                for (String hashType : HASH_TYPES)
                    new ContainsTest(String.format("medium-%s_unique", capacity), capacity, hashType,
                            String.format("medium-%s_unique_%s", capacity, hashType)).run();
        });
    }

    @Test
    @DisplayName("14. Medium-scale containment tests, with duplicate inserts.")
    public void testCase14() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            for (int capacity : MEDIUM_CAPACITIES)
                for (String hashType : HASH_TYPES)
                    new ContainsTest(String.format("medium-%s_duplicates", capacity), capacity, hashType,
                            String.format("medium-%s_duplicates_%s", capacity, hashType)).run();
        });
    }

    private static abstract class BaseTest {
        String inName;
        int capacity;
        String hashType;
        String outName;

        BaseTest(String inName, int capacity, String hashType, String outName) {
            this.inName = inName;
            this.capacity = capacity;
            this.hashType = hashType;
            this.outName = outName;
        }

        void run() throws IOException {
            System.out.println("Running test: " + this);

            HashTable hashTable = new HashTable(capacity, hashType);
            Utils.checkArrayEquals(new String[capacity], hashTable.table, "Table state is incorrect");
            Utils.checkEquals(capacity, hashTable.capacity(), "capacity() is incorrect");
            Utils.checkEquals(0, hashTable.size(), "size() is incorrect");
            Utils.checkAlmostEquals(0.0, hashTable.loadFactor(), 1e-4, "loadFactor() is incorrect");

            List<String> inputs = Files.readAllLines(Paths.get(Utils.ROOT, "input", inName + ".txt"));
            List<String> outputs = Files.readAllLines(Paths.get(Utils.ROOT, "expected", outName + "_output.txt"));
            List<String> states = Files.readAllLines(Paths.get(Utils.ROOT, "expected", outName + "_state.txt"));

            for (int t = 0; t < inputs.size(); t++) {
                runStep(hashTable, t, inputs.get(t), outputs.get(t), states.get(t));
            }
        }

        abstract void runStep(HashTable hashTable, int t, String input, String output, String state);

        @Override
        public String toString() {
            return String.format("inName = %s, capacity = %s, hashType = %s, outName = %s",
                    inName, capacity, hashType, outName);
        }
    }

    private static class InsertTest extends BaseTest {
        InsertTest(String inName, int capacity, String hashType, String outName) {
            super(inName, capacity, hashType, outName);
        }

        @Override
        void runStep(HashTable hashTable, int t, String input, String output, String state) {
            boolean success = hashTable.insert(input);
            String[] outputTokens = output.split(" ");

            Utils.checkEquals(Boolean.parseBoolean(outputTokens[0]), success,
                    "Return value of insert() is incorrect after input " + (t + 1));
            Utils.checkEquals(Integer.parseInt(outputTokens[1]), hashTable.size(),
                    "size() is incorrect after input " + (t + 1));
            Utils.checkAlmostEquals(Double.parseDouble(outputTokens[2]), hashTable.loadFactor(), 1e-4,
                    "loadFactor() is incorrect after input " + (t + 1));
            if (success) {
                Utils.checkEquals(Integer.parseInt(outputTokens[3]), hashTable.numCollisions(),
                        "numCollisions() is incorrect after input " + (t + 1));
            }

            String[] stateTokens = state.split(" ");
            for (int i = 0; i < stateTokens.length; i++) {
                if (stateTokens[i].equals("──────")) {
                    stateTokens[i] = null;
                }
            }
            Utils.checkArrayEquals(stateTokens, hashTable.table, "Table state is incorrect after input " + (t + 1));
        }
    }

    private static class ContainsTest extends BaseTest {
        static final List<String> excluded = loadExcluded();

        ContainsTest(String inName, int capacity, String hashType, String outName) {
            super(inName, capacity, hashType, outName);
        }

        private static List<String> loadExcluded() {
            try {
                return Files.readAllLines(Paths.get(Utils.ROOT, "input", "small_excluded.txt"));
            } catch (IOException e) {
                throw new RuntimeException("Excluded list not found!");
            }
        }

        @Override
        void runStep(HashTable hashTable, int t, String input, String output, String state) {
            hashTable.insert(input);

            // make sure contains() returns true for all strings in the hash table
            String[] stateTokens = state.split(" ");
            for (String string : stateTokens) {
                if (!string.equals("──────")) {
                    Utils.checkEquals(true, hashTable.contains(string),
                            "contains(" + string + ") is incorrect after input " + (t + 1));
                }
            }

            // make sure contains() returns false for some strings not in the hash table
            for (String string : excluded) {
                Utils.checkEquals(false, hashTable.contains(string),
                        "contains(" + string + ") is incorrect after input " + (t + 1));
            }
        }
    }
}
