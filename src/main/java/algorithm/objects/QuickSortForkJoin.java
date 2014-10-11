package main.java.algorithm.objects;

import java.util.List;

/**
 * Created by stephan on 5/29/14.
 */
public class QuickSortForkJoin<T extends Comparable<T>> extends ForkJoinSort<T> {
    public QuickSortForkJoin(List<T> unsortedList, int minimalSize, int leftBoundary, int rightBoundary) {
        super(unsortedList, minimalSize, leftBoundary, rightBoundary);
    }

    @Override
    protected void compute() {
        if (leftBoundary < rightBoundary){
            int pivotElementPosition = partition(leftBoundary, rightBoundary);
            if(rightBoundary - leftBoundary < minimalSize) {
                sortArrayDirectly(leftBoundary, pivotElementPosition - 1);
                sortArrayDirectly(pivotElementPosition, rightBoundary);

            } else {
                invokeAll(new QuickSortForkJoin<>(unsortedList, minimalSize, leftBoundary, pivotElementPosition-1),
                        new QuickSortForkJoin<>(unsortedList, minimalSize, pivotElementPosition, rightBoundary));
            }
        }
    }

    /**
     * Computes the pivot element of the list in the given bounds
     * @param leftBoundary The left boundary
     * @param rightBoundary The right boundary
     * @return The pivot element computed
     */
    private T choosePivotElement(int leftBoundary, int rightBoundary) {
        int middle = (rightBoundary + leftBoundary) / 2;
        T leftElement = unsortedList.get(leftBoundary);
        T middleElement = unsortedList.get(middle);
        T rightElement = unsortedList.get(rightBoundary);
        if(leftElement.compareTo(rightElement) > 0) {
            swapElements(leftBoundary, rightBoundary);
            leftElement = rightElement;
            rightElement = unsortedList.get(rightBoundary);
        }
        if(leftElement.compareTo(middleElement) > 0) {
            swapElements(leftBoundary, middle);
            middleElement = leftElement;
        }
        if(rightElement.compareTo(middleElement) < 0)
            swapElements(middle, rightBoundary);
        return unsortedList.get(middle);
    }

    private int partition(int leftBoundary, int rightBoundary) {
        int leftPointer = leftBoundary;
        int rightPointer = rightBoundary;
        T pivotElement = choosePivotElement(leftBoundary, rightBoundary);
        while(leftPointer <= rightPointer) {
            while(unsortedList.get(leftPointer).compareTo(pivotElement) < 0) leftPointer++;
            while(unsortedList.get(rightPointer).compareTo(pivotElement) > 0) rightPointer--;
            if(leftPointer <= rightPointer) {
                swapElements(leftPointer, rightPointer);
                leftPointer++;
                rightPointer--;
            }
        }
        return leftPointer;
    }

    public void sortArrayDirectly(int leftBoundary, int rightBoundary) {
        int leftPointer = leftBoundary;
        int rightPointer = rightBoundary;
        T pivotElement = choosePivotElement(leftBoundary, rightBoundary);
        if((rightPointer - leftPointer) == 1) {
            if (unsortedList.get(leftPointer).compareTo(unsortedList.get(rightPointer)) > 0)
                swapElements(leftPointer, rightPointer);
            return;
        }
        while(leftPointer <= rightPointer) {
            while(unsortedList.get(leftPointer).compareTo(pivotElement) < 0) leftPointer++;
            while(unsortedList.get(rightPointer).compareTo(pivotElement) > 0) rightPointer--;
            if(leftPointer <= rightPointer) {
                swapElements(leftPointer, rightPointer);
                leftPointer++;
                rightPointer--;
            }
        }
        if(leftBoundary < rightPointer)
            sortArrayDirectly(leftBoundary, rightPointer);
        if(leftPointer < rightBoundary)
            sortArrayDirectly(leftPointer, rightBoundary);
    }

}
