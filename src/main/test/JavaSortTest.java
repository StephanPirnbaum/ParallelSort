package main.test;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by stephan on 5/30/14.
 */
public class JavaSortTest {
    private static final int ARRAY_SIZE = 10000000;

    private int[] unsortedArray;
    private static int[] backup;

    @BeforeClass
    public static void before() {
        JavaSortTest.backup = new int[ARRAY_SIZE];
        for(int i = 0; i < ARRAY_SIZE; i++) {
            //unsortedArray.add(i, i);
            JavaSortTest.backup[i] = (int) (Math.random() * 10000000);
        }
    }

    @Before
    public void setUp() {
        unsortedArray = new int[ARRAY_SIZE];
        System.arraycopy(JavaSortTest.backup, 0, unsortedArray, 0, ARRAY_SIZE);
    }

    @Test
    public void testSingleThread() {
        long startTime = System.currentTimeMillis();
        Arrays.sort(unsortedArray);
        long endTime = System.currentTimeMillis();
        System.out.println("java single thread " + (endTime - startTime));
    }

    @Test
    public void testMultiThread() {
        long startTime = System.currentTimeMillis();
        Arrays.parallelSort(unsortedArray);
        long endTime = System.currentTimeMillis();
        System.out.println("java multi thread " + (endTime - startTime));
    }
}
