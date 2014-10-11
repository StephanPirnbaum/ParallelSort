package main.java.algorithm.objects;

import java.util.List;

/**
 * Created by stephan on 5/24/14.
 */
public abstract class ListSortThread<T extends Comparable<T>> extends Thread{
    List<T> unsortedList;

    int threadsAvailable;

    int minimalSize;

    int leftBoundary;

    int rightBoundary;

    public ListSortThread(List<T> unsortedList, int threadsAvailable, int minimalSize, int leftBoundary, int rightBoundary, ThreadGroup threadGroup) {
        super(threadGroup, "Sort");
        this.unsortedList = unsortedList;
        this.threadsAvailable = threadsAvailable;
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

    abstract void sortArrayParallel(int leftBoundary, int rightBoundary);

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

    @Override
    public void run() {
        this.sortArrayParallel(this.leftBoundary, this.rightBoundary);
    }
}
