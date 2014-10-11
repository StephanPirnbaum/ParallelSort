package main.java.algorithm.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephan on 5/29/14.
 */
public class MergeSortThread<T extends Comparable<T>> extends ListSortThread<T> {

    public MergeSortThread(List<T> unsortedList, int threadsAvailable, int minimalSize, int leftBoundary, int rightBoundary, ThreadGroup threadGroup) {
        super(unsortedList, threadsAvailable, minimalSize, leftBoundary, rightBoundary, threadGroup);
    }

    @Override
    public void run() {
        MergeSortThread<T> mergeSortThreadChild = null;
        int middle = (rightBoundary + leftBoundary) / 2;
//        if((rightBoundary - leftBoundary) > minimalSize) {
        if((middle - leftBoundary) > minimalSize) {
            if (this.getThreadGroup().activeCount() < threadsAvailable) {
                mergeSortThreadChild = new MergeSortThread<>(unsortedList, threadsAvailable, minimalSize, leftBoundary, middle, this.getThreadGroup());
                mergeSortThreadChild.start();
            } else {
                sortArrayParallel(leftBoundary, middle);
            }
            sortArrayParallel(middle + 1, rightBoundary);
            if(mergeSortThreadChild != null) {
                try {
                    mergeSortThreadChild.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            merge(leftBoundary, middle, unsortedList, middle + 1, rightBoundary);
        } else {
            sortArrayDirect(leftBoundary, rightBoundary);
        }
    }

    @Override
    public void sortArrayParallel(int leftBoundary, int rightBoundary) {
        int middle = (rightBoundary + leftBoundary) / 2;
//        if(rightBoundary - leftBoundary > minimalSize) {
        if((middle - leftBoundary) > minimalSize) {
            MergeSortThread<T> mergeSortThreadChild = null;
            if (this.getThreadGroup().activeCount() < threadsAvailable) {
                mergeSortThreadChild = new MergeSortThread<>(unsortedList, threadsAvailable, minimalSize, leftBoundary, middle, this.getThreadGroup());
                mergeSortThreadChild.start();
            } else {
                sortArrayParallel(leftBoundary, middle);
            }
            sortArrayParallel(middle + 1, rightBoundary);
            if (mergeSortThreadChild != null) {
                try {
                    mergeSortThreadChild.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            merge(leftBoundary, middle, unsortedList, middle + 1, rightBoundary);
        } else {
            sortArrayDirect(leftBoundary, rightBoundary);
        }
    }

    private void sortArrayDirect(int leftBoundary, int rightBoundary) {
        if(rightBoundary - leftBoundary > 1) {
            int middle = (rightBoundary + leftBoundary) / 2;
            sortArrayDirect(leftBoundary, middle);
            sortArrayDirect(middle + 1, rightBoundary);
            merge(leftBoundary, middle, unsortedList, middle + 1, rightBoundary);
        } else if(rightBoundary - leftBoundary == 1) {
            if(unsortedList.get(leftBoundary).compareTo(unsortedList.get(rightBoundary)) > 0) {
                swapElements(leftBoundary, rightBoundary);
            }
        }
    }

    /**
     * Merges the given parts of the list
     * @param leftLeftBoundary The left boundary of the left part
     * @param leftRightBoundary The right boundary of the left part
     * @param unsortedList The list to sort
     * @param rightLeftBoundary The left boundary of the right part
     * @param rightRightBoundary The right boundary of the left part
     */
    private void merge(int leftLeftBoundary, int leftRightBoundary, List<T> unsortedList, int rightLeftBoundary, int rightRightBoundary) {
        int length = leftRightBoundary - leftLeftBoundary + 1 + rightRightBoundary - rightLeftBoundary + 1;
        List<T> temporaryList = new ArrayList<>(length);
        int leftStartPosition = leftLeftBoundary;
        T leftElem;
        T rightElem;
        while(leftLeftBoundary <= leftRightBoundary && rightLeftBoundary <= rightRightBoundary) {
            leftElem = unsortedList.get(leftLeftBoundary);
            rightElem = unsortedList.get(rightLeftBoundary);
            if(leftElem.compareTo(rightElem) <= 0) {
                temporaryList.add(leftElem);
                leftLeftBoundary++;
            } else {
                temporaryList.add(rightElem);
                rightLeftBoundary++;
            }
        }
        while(leftLeftBoundary <= leftRightBoundary) {
            temporaryList.add(unsortedList.get(leftLeftBoundary));
            leftLeftBoundary++;
        }
        while(rightLeftBoundary <= rightRightBoundary) {
            temporaryList.add(unsortedList.get(rightLeftBoundary));
            rightLeftBoundary++;
        }
        for(int i = 0; i < temporaryList.size(); i++)
            unsortedList.set(leftStartPosition + i, temporaryList.get(i));
    }
}
