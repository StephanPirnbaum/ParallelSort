package main.java.algorithm.primitiveTypes;

import java.util.concurrent.RecursiveAction;

/**
 * Template for sorting an int array using the fork join pool
 */
public abstract class ArrayForkJoinSort extends RecursiveAction {
    int[] unsortedArray;

    int minimalSize;

    int leftBoundary;

    int rightBoundary;

    public ArrayForkJoinSort(int[] unsortedArray, int minimalSize, int leftBoundary, int rightBoundary) {
        this.unsortedArray = unsortedArray;
        this.minimalSize = minimalSize;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
    }

    abstract void sortArrayDirect(int leftBoundary, int rightBoundary);

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


}
