package main.java;

import main.java.algorithm.objects.MergeSortForkJoin;
import main.java.algorithm.objects.MergeSortThread;
import main.java.algorithm.objects.QuickSortForkJoin;
import main.java.algorithm.objects.QuickSortThread;
import main.java.algorithm.primitiveTypes.MergeSortArrayForkJoin;
import main.java.algorithm.primitiveTypes.MergeSortArrayThread;
import main.java.algorithm.primitiveTypes.QuickSortArrayForkJoin;
import main.java.algorithm.primitiveTypes.QuickSortArrayThread;
import main.java.gui.SortGui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.assertTrue;

/**
 * Created by stephan on 5/25/14.
 */
public class AlgorithmExecutor extends Thread{
    private List<String> algorithmsToRun;

    private int lowerExponent; // 2^lowerExponent

    private int upperExponent;   // 2^upperExponent

    private int threadsToUse;

    private int minimalSize;

    private int runs;

    private SortGui sortGui;

    private static Map<String, ArrayList<Long>> result;

    private static ArrayList<Integer> listToSort;
    private static ArrayList<Integer> listToSortBackUp;

    public AlgorithmExecutor(List<String> algorithmsToRun, int lowerExponent, int upperExponent,
                             int threadsToUse, int minimalSizeExponent, int runs, SortGui sortGui) {
        this.algorithmsToRun = algorithmsToRun;
        this.lowerExponent = lowerExponent;
        this.upperExponent = upperExponent;
        this.threadsToUse = threadsToUse;
        this.minimalSize = (int) Math.pow(2, minimalSizeExponent);
        this.runs = runs;
        this.sortGui = sortGui;
        result = new TreeMap<>();

    }

    private void createBackupArray(int exp) {
        int size = (int) Math.pow(2, exp);
        listToSortBackUp = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            listToSortBackUp.add(i, (int) (Math.random() * 10000000));
        }
    }

    @SuppressWarnings("unchecked")
    private void createArray() {
        listToSort = (ArrayList<Integer>) listToSortBackUp.clone();
    }

    private void printResult() {
        File f = new File("result.csv");
        FileWriter fw;
        try {
            fw = new FileWriter(f);
            for(String s : result.keySet()) {
                fw.write(s + ",");
            }
            fw.write(System.getProperty("line.separator"));
            for(int i = 0; i < runs; i++) {
                for(String s : result.keySet()) {
                    fw.write(result.get(s).get(i).toString() + ",");
                }
                fw.write(System.getProperty("line.separator"));
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateSpeedup() {
        Map<String, ArrayList<Double>> speedup = new TreeMap<>();
        File f = new File("speedup.csv");
        try {
            FileWriter fw = new FileWriter(f);
            for(int i = lowerExponent; i <= upperExponent; i++) {
                for(int j = 0; j < runs; j++) {

                    int size = (int) Math.pow(2, i);

                    if(algorithmsToRun.contains("quickArraySingle")) {
                        if(algorithmsToRun.contains("quickArrayMulti")) {
                            /*
                             * QuickSort Array Speedup
                             */
                            if (!speedup.containsKey(size + " QuickSort Array Speedup"))
                            speedup.put(size + " QuickSort Array Speedup", new ArrayList<>(runs));
                            speedup.get(size + " QuickSort Array Speedup").add((double)
                                    result.get(size + " QuickSort Array 1 Thread").get(j)/
                                    result.get(size + " QuickSort Array " + threadsToUse + " Thread").get(j));
                        }
                        if(algorithmsToRun.contains("quickArrayFork")) {
                            /*
                             * QuickSort Array Fork Join Speedup
                             */
                            if (!speedup.containsKey(size + " QuickSort Array ForkJoin Speedup"))
                            speedup.put(size + " QuickSort Array ForkJoin Speedup", new ArrayList<>(runs));
                            speedup.get(size + " QuickSort Array ForkJoin Speedup").add((double)
                                    result.get(size + " QuickSort Array 1 Thread").get(j)/
                                    result.get(size + " QuickSort Array ForkJoin").get(j));
                        }
                    }

                    if(algorithmsToRun.contains("quickListSingle")) {
                        if(algorithmsToRun.contains("quickListMulti")) {
                            /*
                             * QuickSort List Speedup
                             */
                            if (!speedup.containsKey(size + " QuickSort List Speedup"))
                            speedup.put(size + " QuickSort List Speedup", new ArrayList<>(runs));
                            speedup.get(size + " QuickSort List Speedup").add((double)
                                    result.get(size + " QuickSort List 1 Thread").get(j)/
                                    result.get(size + " QuickSort List " + threadsToUse + " Thread").get(j));
                        }
                        if(algorithmsToRun.contains("quickListFork")) {
                            /*
                             * QuickSort List Fork Join Speedup
                             */
                            if (!speedup.containsKey(size + " QuickSort List ForkJoin Speedup"))
                                speedup.put(size + " QuickSort List ForkJoin Speedup", new ArrayList<>(runs));
                            speedup.get(size + " QuickSort List ForkJoin Speedup").add((double)
                                    result.get(size + " QuickSort List 1 Thread").get(j)/
                                    result.get(size + " QuickSort List ForkJoin").get(j));
                        }
                    }

                    if(algorithmsToRun.contains("mergeArraySingle")) {
                        if(algorithmsToRun.contains("mergeArrayMulti")) {
                            /*
                             * MergeSort Array Speedup
                             */
                            if (!speedup.containsKey(size + " MergeSort Array Speedup"))
                                speedup.put(size + " MergeSort Array Speedup", new ArrayList<>(runs));
                            speedup.get(size + " MergeSort Array Speedup").add((double)
                                    result.get(size + " MergeSort Array 1 Thread").get(j)/
                                    result.get(size + " MergeSort Array " + threadsToUse + " Thread").get(j));
                        }
                        if(algorithmsToRun.contains("mergeArrayFork")) {
                            /*
                             * MergeSort Array Fork Join Speedup
                             */
                            if (!speedup.containsKey(size + " MergeSort Array ForkJoin Speedup"))
                                speedup.put(size + " MergeSort Array ForkJoin Speedup", new ArrayList<>(runs));
                            speedup.get(size + " MergeSort Array ForkJoin Speedup").add((double)
                                    result.get(size + " MergeSort Array 1 Thread").get(j) /
                                    result.get(size + " MergeSort Array ForkJoin").get(j));
                        }
                    }
                    if(algorithmsToRun.contains("mergeListSingle")) {
                        if(algorithmsToRun.contains("mergeListMulti")) {
                            /*
                             * MergeSort List Speedup
                             */
                            if (!speedup.containsKey(size + " MergeSort List Speedup"))
                                speedup.put(size + " MergeSort List Speedup", new ArrayList<>(runs));
                            speedup.get(size + " MergeSort List Speedup").add((double)
                                    result.get(size + " MergeSort List 1 Thread").get(j)/
                                    result.get(size + " MergeSort List " + threadsToUse + " Thread").get(j));
                        }
                        if(algorithmsToRun.contains("mergeListFork")) {
                            /*
                             * MergeSort List Fork Join Speedup
                             */
                            if (!speedup.containsKey(size + " MergeSort List ForkJoin Speedup"))
                                speedup.put(size + " MergeSort List ForkJoin Speedup", new ArrayList<>(runs));
                            speedup.get(size + " MergeSort List ForkJoin Speedup").add((double)
                                    result.get(size + " MergeSort List 1 Thread").get(j)/
                                    result.get(size + " MergeSort List ForkJoin").get(j));
                        }
                    }
                    if(algorithmsToRun.containsAll(Arrays.asList("javaArraySingle", "javaArrayMulti"))) {
                        /*
                         * Java Array Sort Reference Speedup
                         */
                        if (!speedup.containsKey(size + " Java Array Reference Speedup"))
                            speedup.put(size + " Java Array Reference Speedup", new ArrayList<>(runs));
                        speedup.get(size + " Java Array Reference Speedup").add((double)
                                result.get(size + " JavaSort Array 1 Thread").get(j)/
                                result.get(size + " JavaSort Array " + threadsToUse + " Thread").get(j));
                    }
               }
            }
            for(String s : speedup.keySet()) {
                fw.write(s + ",");
            }
            fw.write(System.getProperty("line.separator"));
            for(int k = 0; k < runs; k++) {
                for(String s : speedup.keySet()) {
                    fw.write(String.valueOf(speedup.get(s).get(k)) + ",");
                }
                fw.write(System.getProperty("line.separator"));
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sortGui.printSpeedup(speedup);
        System.out.println(speedup);
    }

    /**
     * Executes the Merge Sort Algorithm on an int array
     * @param threads the number of threads to be used, 0 means using of fork join pool
     */
    private void executeMergeSortArray(int threads, int size) {
        int[] unsorted = new int[size];
        for (int i = 0; i < size; i++)
            unsorted[i] = listToSort.get(i);
        long startTime;
        long endTime;
        if (threads == 0) {
            MergeSortArrayForkJoin mergeSortArrayForkJoin = new MergeSortArrayForkJoin(unsorted, minimalSize, 0, size - 1);
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            startTime = System.nanoTime();
            forkJoinPool.invoke(mergeSortArrayForkJoin);
            forkJoinPool.shutdown();
            endTime = System.nanoTime();
            if (!result.containsKey(unsorted.length + " MergeSort Array ForkJoin"))
                result.put(unsorted.length + " MergeSort Array ForkJoin", new ArrayList<>(runs));
            result.get(unsorted.length + " MergeSort Array ForkJoin").add(endTime - startTime);
        } else {
            MergeSortArrayThread mergeSortArrayThread = new MergeSortArrayThread(unsorted, threads, minimalSize, 0, size - 1, new ThreadGroup("MergeSortArray"+threads));
            startTime = System.nanoTime();
            mergeSortArrayThread.start();
            try {
                mergeSortArrayThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            endTime = System.nanoTime();
            if(!result.containsKey(unsorted.length + " MergeSort Array " + threads + " Thread"))
                result.put(unsorted.length + " MergeSort Array " + threads + " Thread", new ArrayList<>(10));
            result.get(unsorted.length + " MergeSort Array " + threads + " Thread").add(endTime - startTime);
        }
        for(int i = 1; i < size; i++) {
            assertTrue(unsorted[i-1] + " " + unsorted[i], unsorted[i-1] <= unsorted[i]);
        }
        sortGui.incrementProgressBar();
    }

    /**
     * Executes the Quick Sort Algorithm on an int array
     * @param threads the number of threads to be used, 0 means using of fork join pool
     */
    private void executeQuickSortArray(int threads, int size) {
        int[] unsorted = new int[size];
        long startTime;
        long endTime;
            for (int i = 0; i < size; i++)
                unsorted[i] = listToSort.get(i);
            if (threads == 0) {
                QuickSortArrayForkJoin quickSortArrayForkJoin = new QuickSortArrayForkJoin(unsorted, minimalSize, 0, size - 1);
                ForkJoinPool forkJoinPool = new ForkJoinPool();
                startTime = System.nanoTime();
                forkJoinPool.invoke(quickSortArrayForkJoin);
                forkJoinPool.shutdown();
                endTime = System.nanoTime();
                if (!result.containsKey(unsorted.length + " QuickSort Array ForkJoin"))
                    result.put(unsorted.length + " QuickSort Array ForkJoin", new ArrayList<>(runs));
                result.get(unsorted.length + " QuickSort Array ForkJoin").add(endTime - startTime);
            } else {
                QuickSortArrayThread quickSortArrayThread = new QuickSortArrayThread(unsorted, threads, minimalSize, 0, size - 1, new ThreadGroup("QuickSortArray"+threads));
                startTime = System.nanoTime();
                quickSortArrayThread.start();
                try {
                    quickSortArrayThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                endTime = System.nanoTime();
                if (!result.containsKey(unsorted.length + " QuickSort Array " + threads + " Thread"))
                    result.put(unsorted.length + " QuickSort Array " + threads + " Thread", new ArrayList<>(10));
                result.get(unsorted.length + " QuickSort Array " + threads + " Thread").add(endTime - startTime);
            }
        sortGui.incrementProgressBar();
    }

    /**
     * Executes the Merge Sort Algorithm on an List of Integer
     * @param threads the number of threads to be used, 0 means using of fork join pool
     */
    private void executeMergeSortList(int threads) {
        long startTime;
        long endTime;
            if (threads == 0) {
                MergeSortForkJoin<Integer> mergeSortForkJoin = new MergeSortForkJoin<>(listToSort, minimalSize, 0, listToSort.size() - 1);
                ForkJoinPool forkJoinPool = new ForkJoinPool();
                startTime = System.nanoTime();
                forkJoinPool.invoke(mergeSortForkJoin);
                forkJoinPool.shutdown();
                endTime = System.nanoTime();
                if (!result.containsKey(listToSort.size() + " MergeSort List ForkJoin"))
                    result.put(listToSort.size() + " MergeSort List ForkJoin", new ArrayList<>(runs));
                result.get(listToSort.size() + " MergeSort List ForkJoin").add(endTime - startTime);
            } else {
                MergeSortThread<Integer> mergeSortThread = new MergeSortThread<>(listToSort, threads, minimalSize, 0, listToSort.size() - 1, new ThreadGroup("MergeSortList"+threads));
                startTime = System.nanoTime();
                mergeSortThread.start();
                try {
                    mergeSortThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                endTime = System.nanoTime();
                if(!result.containsKey(listToSort.size() + " MergeSort List " + threads + " Thread"))
                    result.put(listToSort.size() + " MergeSort List " + threads + " Thread", new ArrayList<>(10));
                result.get(listToSort.size() + " MergeSort List " + threads + " Thread").add(endTime - startTime);
            }
        sortGui.incrementProgressBar();
    }

    /**
     * Executes the Quick Sort Algorithm on an List of Integer
     * @param threads the number of threads to be used, 0 means using of fork join pool
     */
    private void executeQuickSortList(int threads) {
        long startTime;
        long endTime;
            if (threads == 0) {
                QuickSortForkJoin<Integer> quickSortForkJoin = new QuickSortForkJoin<>(listToSort, minimalSize, 0, listToSort.size() - 1);
                ForkJoinPool forkJoinPool = new ForkJoinPool();
                startTime = System.nanoTime();
                forkJoinPool.invoke(quickSortForkJoin);
                forkJoinPool.shutdown();
                endTime = System.nanoTime();
                if (!result.containsKey(listToSort.size() + " QuickSort List ForkJoin"))
                    result.put(listToSort.size() + " QuickSort List ForkJoin", new ArrayList<>(runs));
                result.get(listToSort.size() + " QuickSort List ForkJoin").add(endTime - startTime);
            } else {
                QuickSortThread<Integer> quickSortThread = new QuickSortThread<>(listToSort, threads, minimalSize, 0, listToSort.size() - 1, new ThreadGroup("QuickSortList"+threads));
                startTime = System.nanoTime();
                quickSortThread.start();
                try {
                    quickSortThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                endTime = System.nanoTime();
                if (!result.containsKey(listToSort.size() + " QuickSort List " + threads + " Thread"))
                    result.put(listToSort.size() + " QuickSort List " + threads + " Thread", new ArrayList<>(10));
                result.get(listToSort.size() + " QuickSort List " + threads + " Thread").add(endTime - startTime);
            }
        sortGui.incrementProgressBar();
    }

    /**
     * executes the reference implementation of array sort int the arrays class
     * @param parallel Whether if sorting is executed in parallel or not
     */
    private void executeJavaArraySort(boolean parallel, int size) {
        int[] unsorted = new int[size];
        long startTime;
        long endTime;
            for (int i = 0; i < size; i++)
                unsorted[i] = listToSort.get(i);
            if (!parallel) {
                startTime = System.nanoTime();
                Arrays.sort(unsorted);
                endTime = System.nanoTime();
                if (!result.containsKey(unsorted.length + " JavaSort Array 1 Thread"))
                    result.put(unsorted.length + " JavaSort Array 1 Thread", new ArrayList<>(runs));
                result.get(unsorted.length + " JavaSort Array 1 Thread").add(endTime - startTime);
            } else {
                startTime = System.nanoTime();
                Arrays.parallelSort(unsorted);
                endTime = System.nanoTime();
                if (!result.containsKey(unsorted.length + " JavaSort Array " + threadsToUse + " Thread"))
                    result.put(unsorted.length + " JavaSort Array " + threadsToUse + " Thread", new ArrayList<>(runs));
                result.get(unsorted.length + " JavaSort Array " + threadsToUse + " Thread").add(endTime - startTime);
            }
        sortGui.incrementProgressBar();
    }

    private void executeAlgorithms() {
        for(int exp = lowerExponent; exp <= upperExponent; exp++) {
            for (int i = 0; i < runs; i++) {
                System.out.println("Exp: " + exp + " Run: " + (i + 1));
                createBackupArray(exp);
                createArray();
                try {
                    if(algorithmsToRun.contains("javaArraySingle")) {
                        executeJavaArraySort(false, (int) Math.pow(2, exp));
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("javaArrayMulti")) {
                        executeJavaArraySort(true, (int) Math.pow(2, exp));
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("quickArraySingle")) {
                        executeQuickSortArray(1, (int) Math.pow(2, exp));
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("quickArrayMulti")) {
                        executeQuickSortArray(threadsToUse, (int) Math.pow(2, exp));
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("quickArrayFork")) {
                        executeQuickSortArray(0, (int) Math.pow(2, exp));
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("quickListSingle")) {
                        executeQuickSortList(1);
                        createArray();
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("quickListMulti")) {
                        executeQuickSortList(threadsToUse);
                        createArray();
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("quickListFork")) {
                        executeQuickSortList(0);
                        createArray();
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("mergeArraySingle")) {
                        executeMergeSortArray(1, (int) Math.pow(2, exp));
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("mergeArrayMulti")) {
                        executeMergeSortArray(threadsToUse, (int) Math.pow(2, exp));
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("mergeArrayFork")) {
                        executeMergeSortArray(0, (int) Math.pow(2, exp));
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("mergeListSingle")) {
                        executeMergeSortList(1);
                        createArray();
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("mergeListMulti")) {
                        executeMergeSortList(threadsToUse);
                        createArray();
                        sleep(50);
                    }
                    if(algorithmsToRun.contains("mergeListFork")) {
                        executeMergeSortList(0);
                        sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        printResult();
        calculateSpeedup();
        System.out.println(result);
    }

    @Override
    public void run() {
        executeAlgorithms();
    }
}
