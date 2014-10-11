package main.test;

import main.java.algorithm.objects.MergeSortThread;
import main.java.algorithm.objects.MergeSortForkJoin;
import main.java.algorithm.primitiveTypes.MergeSortArrayForkJoin;
import main.java.algorithm.primitiveTypes.MergeSortArrayThread;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertTrue;

/**
 * Created by stephan on 5/29/14.
 */
public class MergeSortAlgorithmTest {
    private static final int ARRAY_SIZE = (int) Math.pow(2, 23);

    private final int MINIMAL_SIZE = 1000;

    private final int THREADS_TO_USE = 4;

    private ArrayList<Integer> unsortedArray;
    private static ArrayList<Integer> backup;

    @BeforeClass
    public static void before() {
        MergeSortAlgorithmTest.backup = new ArrayList<>(ARRAY_SIZE);
        for(int i = 0; i < ARRAY_SIZE; i++) {
            MergeSortAlgorithmTest.backup.add(i, (int) (Math.random() * 10000000));
        }
    }

    @Before
    public void setUp() {
        //noinspection unchecked
        unsortedArray = (ArrayList<Integer>) backup.clone();
    }

    @Test
    public void testForkJoin() {
        MergeSortForkJoin<Integer> mergeSortForkJoin = new MergeSortForkJoin<>(unsortedArray, MINIMAL_SIZE, 0, ARRAY_SIZE - 1);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        forkJoinPool.invoke(mergeSortForkJoin);
        forkJoinPool.shutdown();
        System.out.println("Fork Join: " + (System.currentTimeMillis() - startTime));
        Integer prev = unsortedArray.get(0);
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(prev.toString() + " " + unsortedArray.get(i), prev.compareTo(unsortedArray.get(i)) <= 0);
            prev = unsortedArray.get(i);
        }

    }

    @Test
    public void testSingleThread() {
        MergeSortThread<Integer> mergeSortThread = new MergeSortThread<>(unsortedArray, 1, MINIMAL_SIZE, 0, ARRAY_SIZE - 1, new ThreadGroup("MergeSortList1"));
        long startTime = System.currentTimeMillis();
        mergeSortThread.start();
        try {
            mergeSortThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("recursive single thread " + (endTime - startTime));
        Integer prev = unsortedArray.get(0);
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(prev.toString() + " " + unsortedArray.get(i), prev.compareTo(unsortedArray.get(i)) <= 0);
            prev = unsortedArray.get(i);
        }
    }

    @Test
    public void testMultiThread() {
        MergeSortThread<Integer> mergeSortThread = new MergeSortThread<>(unsortedArray, THREADS_TO_USE, MINIMAL_SIZE, 0, ARRAY_SIZE - 1, new ThreadGroup("MergeSortList"+THREADS_TO_USE));
        long startTime = System.currentTimeMillis();
        mergeSortThread.start();
        try {
            mergeSortThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("recursive multi thread " + (endTime - startTime));
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
        long endTime = System.currentTimeMillis();
        System.out.println("java single thread " + (endTime - startTime));
    }

    @Test
    public void testMergeSortArray() {
        int[] unsorted = new int[ARRAY_SIZE];
        for(int i = 0; i < ARRAY_SIZE; i++)
            unsorted[i] = unsortedArray.get(i);
        MergeSortArrayThread mergeSortArrayThread= new MergeSortArrayThread(unsorted, 1, MINIMAL_SIZE, 0, ARRAY_SIZE - 1, new ThreadGroup("MergeSortArray1"));
        long startTime = System.currentTimeMillis();
        mergeSortArrayThread.start();
        try {
            mergeSortArrayThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Single Thread Array: " + (System.currentTimeMillis() - startTime));
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(unsorted[i-1] <= unsorted[i]);
        }
    }

    @Test
    public void testMergeSortArrayParallel() {
        int[] unsorted = new int[ARRAY_SIZE];
        for(int i = 0; i < ARRAY_SIZE; i++)
            unsorted[i] = unsortedArray.get(i);
        MergeSortArrayThread mergeSortArrayThread = new MergeSortArrayThread(unsorted, THREADS_TO_USE, MINIMAL_SIZE, 0, ARRAY_SIZE - 1, new ThreadGroup("MergeSortArray"+THREADS_TO_USE));
        long startTime = System.currentTimeMillis();
        mergeSortArrayThread.start();
        try {
            mergeSortArrayThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Multi Thread Array: " + (System.currentTimeMillis() - startTime));
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(unsorted[i-1] <= unsorted[i]);
        }
    }

    @Test
    public void testMergeSortArrayForkJoin() {
        int[] unsorted = new int[ARRAY_SIZE];
        for(int i = 0; i < ARRAY_SIZE; i++)
            unsorted[i] = unsortedArray.get(i);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        forkJoinPool.invoke(new MergeSortArrayForkJoin(unsorted, MINIMAL_SIZE, 0, ARRAY_SIZE - 1));
        forkJoinPool.shutdown();
        System.out.println("Fork Join Array: " + (System.currentTimeMillis() - startTime));
        for(int i = 1; i < ARRAY_SIZE; i++) {
            assertTrue(unsorted[i-1] <= unsorted[i]);
        }
    }

}
