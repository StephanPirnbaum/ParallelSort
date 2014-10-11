package main.java.algorithm.objects;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * Sorts a list in parallel using the fork join pool
 * @param <T> The objects to sort
 */
public abstract class ForkJoinSort<T extends Comparable<T>> extends RecursiveAction {
    List<T> unsortedList;

    int minimalSize;

    int leftBoundary;

    int rightBoundary;

    public ForkJoinSort(List<T> unsortedList, int minimalSize, int leftBoundary, int rightBoundary) {
        this.unsortedList = unsortedList;
        this.minimalSize = minimalSize;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
    }

    /**
     * Swaps the elements of the Array at the positions given as parameter
     * @param leftPointer The position of the left element to swap
     * @param rightPointer The position of the right element to swap
     */
    final void swapElements(int leftPointer, int rightPointer) {
        T old = unsortedList.get(leftPointer);
        unsortedList.set(leftPointer, unsortedList.get(rightPointer));
        unsortedList.set(rightPointer, old);
    }

    final void insertionSort(int leftBoundary, int rightBoundary) {
        T compareElement;
        for(int i = leftBoundary; i <= rightBoundary; i++) {
            compareElement = unsortedList.get(i);
            int j = i;
            while(j > 0 && unsortedList.get(j - 1).compareTo(compareElement) > 0) {
                unsortedList.set(j, unsortedList.get(j-1));
                j--;
            }
            unsortedList.set(j, compareElement);
        }
    }
}
