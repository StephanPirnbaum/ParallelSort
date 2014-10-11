package main.java.algorithm.primitiveTypes;

import org.jetbrains.annotations.NotNull;

/**
 * Template for parallel sorting an int array
 */
public abstract class ArraySortThread extends Thread {
    int[] unsortedArray;

    int threadsAvailable;

    int minimalSize;

    int leftBoundary;

    int rightBoundary;

    public ArraySortThread(@NotNull int[] unsortedArray, int threadsAvailable, int minimalSize, int leftBoundary, int rightBoundary, ThreadGroup threadGroup) {
        super(threadGroup, "Sort");
        this.unsortedArray = unsortedArray;
        this.threadsAvailable = threadsAvailable;
        this.minimalSize = minimalSize;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
    }

    abstract void sortArrayParallel(int leftBoundary, int rightBoundary);

    /**
     * Swaps the elements of the Array at the positions given as parameter
     * @param leftPointer The position of the left element to swap
     * @param rightPointer The position of the right element to swap
     */
    final void swapElements(int leftPointer, int rightPointer) {
        int old=  unsortedArray[leftPointer];
        unsortedArray[leftPointer] = unsortedArray[rightPointer];
        unsortedArray[rightPointer] = old;
    }

    @Override
    public void run() {
        this.sortArrayParallel(this.leftBoundary, this.rightBoundary);
    }
}
