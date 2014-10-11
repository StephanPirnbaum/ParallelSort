package main.test;

import main.java.algorithm.primitiveTypes.QuickSortArrayThread;
import main.java.algorithm.objects.QuickSortThread;
import main.java.algorithm.primitiveTypes.QuickSortArrayForkJoin;
import main.java.algorithm.objects.QuickSortForkJoin;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertTrue;

/**
 * Created by stephan on 5/24/14.
 */
public class QuickSortAlgorithmTest {
    private static final int ARRAY_SIZE = 4194304;

    private final int MINIMAL_SIZE = 1000;

    private final int THREADS_TO_USE = 4;

    private ArrayList<Integer> unsortedArray;
    private static ArrayList<Integer> backup;

    @BeforeClass
    public static void before() {
        QuickSortAlgorithmTest.backup = new ArrayList<>();
        for(int i = 0; i < ARRAY_SIZE; i++) {
            QuickSortAlgorithmTest.backup.add(i, (int) (Math.random() * 10000000));
        }
    }

    @Before
    public void setUp() {
        if(backup != null)
            //noinspection unchecked
            unsortedArray = (ArrayList<Integer>) backup.clone();
        else {
            this.unsortedArray = new ArrayList<>(ARRAY_SIZE);
            for(int i = 0; i < ARRAY_SIZE; i++) {
                unsortedArray.add(i, (int) (Math.random() * 10000000));
            }
            //noinspection unchecked
            backup = (ArrayList<Integer>) unsortedArray.clone();
        }
    }

    @Test
    public void testQuickSortAlgorithmSingleThread() {
        QuickSortThread<Integer> quickSortThread = new QuickSortThread<>(unsortedArray, 1, MINIMAL_SIZE, 0, ARRAY_SIZE - 1, new ThreadGroup("QuickSortList1"));
        long startTime = System.currentTimeMillis();
        quickSortThread.start();
        try {
            quickSortThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("recursive single thread " + (System.currentTimeMillis() - startTime));
        Integer prev = unsortedArray.get(0);
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(prev.toString() + " " + unsortedArray.get(i), prev.compareTo(unsortedArray.get(i)) <= 0);
            prev = unsortedArray.get(i);
        }
    }

    @Test
    public void testQuickSortMultiThread() {
        QuickSortThread<Integer> quickSortThread = new QuickSortThread<>(unsortedArray, THREADS_TO_USE, MINIMAL_SIZE, 0, ARRAY_SIZE - 1, new ThreadGroup("QuickSortList"+THREADS_TO_USE));
        long startTime = System.currentTimeMillis();
        quickSortThread.start();
        try {
            quickSortThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("recursive multi thread " + (System.currentTimeMillis() - startTime));
        Integer prev = unsortedArray.get(0);
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(prev.toString() + " " + unsortedArray.get(i), prev.compareTo(unsortedArray.get(i)) <= 0);
            prev = unsortedArray.get(i);
        }
    }

    @Test
    public void testQuickSortIterative() {
        QuickSortThread<Integer> quickSortThread = new QuickSortThread<>(unsortedArray, THREADS_TO_USE, MINIMAL_SIZE, 0, ARRAY_SIZE - 1, new ThreadGroup("QuickSortIterative"));
        long startTime = System.currentTimeMillis();
        quickSortThread.sortArrayIterative(0, ARRAY_SIZE - 1);
        System.out.println("Iterative: " + (System.currentTimeMillis() - startTime));
        Integer prev = unsortedArray.get(0);
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(prev.toString() + " " + unsortedArray.get(i), prev.compareTo(unsortedArray.get(i)) <= 0);
            prev = unsortedArray.get(i);
        }
    }

    @Test
    public void testJavaSort() {
        long startTime = System.currentTimeMillis();
        Collections.sort(unsortedArray);
        System.out.println("Java: " + (System.currentTimeMillis() - startTime));
    }

    @Test
    public void testQuickSortForkJoin() {
        QuickSortForkJoin<Integer> quickSortForkJoin = new QuickSortForkJoin<>(unsortedArray, MINIMAL_SIZE, 0, ARRAY_SIZE - 1);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        forkJoinPool.invoke(quickSortForkJoin);
        forkJoinPool.shutdown();
        System.out.println("Fork Join: " + (System.currentTimeMillis() - startTime));
        Integer prev = unsortedArray.get(0);
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(prev.toString() + " " + unsortedArray.get(i), prev.compareTo(unsortedArray.get(i)) <= 0);
            prev = unsortedArray.get(i);
        }
    }

    @Test
    public void testQuickSortArray() {
        int[] unsorted = new int[ARRAY_SIZE];
        for(int i = 0; i < ARRAY_SIZE; i++)
            unsorted[i] = unsortedArray.get(i);
        QuickSortArrayThread quickSortArrayThread = new QuickSortArrayThread(unsorted, 1, MINIMAL_SIZE, 0, ARRAY_SIZE - 1, new ThreadGroup("QuickSortArray1"));
        long startTime = System.currentTimeMillis();
        quickSortArrayThread.start();
        try {
            quickSortArrayThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Single Thread Array: " + (System.currentTimeMillis() - startTime));
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(unsorted[i-1] <= unsorted[i]);
        }
    }

    @Test
    public void testQuickSortArrayParallel() {
        int[] unsorted = new int[ARRAY_SIZE];
        for(int i = 0; i < ARRAY_SIZE; i++)
            unsorted[i] = unsortedArray.get(i);
        QuickSortArrayThread quickSortArrayThread = new QuickSortArrayThread(unsorted, THREADS_TO_USE, MINIMAL_SIZE, 0, ARRAY_SIZE - 1, new ThreadGroup("QuickSortArray"+THREADS_TO_USE));
        long startTime = System.currentTimeMillis();
        quickSortArrayThread.start();
        try {
            quickSortArrayThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Multi Thread Array: " + (System.currentTimeMillis() - startTime));
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(unsorted[i-1] <= unsorted[i]);
        }
    }

    @Test
    public void testQuickSortArrayForkJoin() {
        int[] unsorted = new int[ARRAY_SIZE];
        for(int i = 0; i < ARRAY_SIZE; i++)
            unsorted[i] = unsortedArray.get(i);
        QuickSortArrayForkJoin quickSortArrayForkJoin = new QuickSortArrayForkJoin(unsorted, MINIMAL_SIZE, 0, ARRAY_SIZE - 1);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        forkJoinPool.invoke(quickSortArrayForkJoin);
        forkJoinPool.shutdown();
        System.out.println("Array Fork Join: " + (System.currentTimeMillis() - startTime));
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(unsorted[i-1] <= unsorted[i]);
        }
    }

    @Test
    public void testQuickSortArrayIterative() {
        int[] unsorted = new int[ARRAY_SIZE];
        for(int i = 0; i < ARRAY_SIZE; i++)
            unsorted[i] = unsortedArray.get(i);
        QuickSortArrayThread quickSortArrayThread = new QuickSortArrayThread(unsorted, THREADS_TO_USE, MINIMAL_SIZE, 0, ARRAY_SIZE - 1, new ThreadGroup("QuickSortArrayIterative"));
        long startTime = System.currentTimeMillis();
        quickSortArrayThread.sortArrayIterative(0, ARRAY_SIZE - 1);
        System.out.println("Iterative Array: " + (System.currentTimeMillis() - startTime));
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(unsorted[i-1] <= unsorted[i]);
        }
    }
}
