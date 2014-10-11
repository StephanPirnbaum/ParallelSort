package main.java.algorithm.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephan on 6/8/14.
 */
public class MergeSortForkJoin<T extends Comparable<T>> extends ForkJoinSort<T> {

    public MergeSortForkJoin(List<T> unsortedList, int minimalSize, int leftBoundary, int rightBoundary) {
        super(unsortedList, minimalSize, leftBoundary, rightBoundary);
    }

    @Override
    protected void compute() {
        if(leftBoundary < rightBoundary) {
            int middle = (rightBoundary + leftBoundary) / 2;
//            if ((rightBoundary - leftBoundary) < minimalSize) {
            if((middle - leftBoundary) < minimalSize) {
                sortArray(leftBoundary, middle);
                sortArray(middle + 1, rightBoundary);
            } else {
                invokeAll(new MergeSortForkJoin<>(unsortedList, minimalSize, leftBoundary, middle),
                        new MergeSortForkJoin<>(unsortedList, minimalSize, middle + 1, rightBoundary));
            }
            merge(leftBoundary, middle, unsortedList, middle + 1, rightBoundary);
        }
    }

    public void sortArray(int leftBoundary, int rightBoundary) {
        if(rightBoundary - leftBoundary > 1) {
            int middle = (rightBoundary + leftBoundary) / 2;
            sortArray(leftBoundary, middle);
            sortArray(middle + 1, rightBoundary);
            merge(leftBoundary, middle, unsortedList, middle + 1, rightBoundary);
        } else if(rightBoundary - leftBoundary == 1) {
            if(unsortedList.get(leftBoundary).compareTo(unsortedList.get(rightBoundary)) > 0) {
                T old = unsortedList.get(leftBoundary);
                unsortedList.set(leftBoundary, unsortedList.get(rightBoundary));
                unsortedList.set(rightBoundary, old);
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
