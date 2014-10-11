package main.java.algorithm.objects;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Stack;

/**
 * Implementation of the QuickSort algorithm for use with Multi Threading
 * Created by stephan on 5/24/14.
 */
public class QuickSortThread<T extends Comparable<T>> extends ListSortThread<T> {

    public QuickSortThread(@NotNull List<T> unsortedList, int threadsAvailable, int minimalSize, int leftBoundary, int rightBoundary, ThreadGroup threadGroup) {
        super(unsortedList, threadsAvailable, minimalSize, leftBoundary, rightBoundary, threadGroup);
    }

    @Override
    public void run() {
        if(rightBoundary - leftBoundary > minimalSize) {
            sortArrayParallel(leftBoundary, rightBoundary);
        } else {
            sortArray(leftBoundary, rightBoundary);
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

    @Override
    void sortArrayParallel(int leftBoundary, int rightBoundary) {
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
        QuickSortThread<T> quickSortThreadChild = null;
        boolean sortLeftHalf = leftBoundary < rightPointer;
        boolean sortRightHalf = rightBoundary > leftPointer;
        boolean sortLeftHalfParallelPossible = (rightPointer - leftBoundary) > minimalSize;
        boolean sortRightHalfParallelPossible = (rightBoundary - leftPointer) > minimalSize;
        if(sortLeftHalf && sortRightHalf) {
            /*
             * both halves needs to get sorted
             */
            if(this.getThreadGroup().activeCount() >= threadsAvailable) {
                /*
                 * no free resources to start a new thread
                 * sort left half in actual thread
                 */
                if(sortLeftHalfParallelPossible)
                    sortArrayParallel(leftBoundary, rightPointer);
                else
                    sortArray(leftBoundary, rightPointer);
                /*
                 * after returning, actual thread has nothing to do
                 * thus, no new thread needs to be forked to sort the right half
                 * sort right half in actual thread
                 */
                if(sortRightHalfParallelPossible)
                    sortArrayParallel(leftPointer, rightBoundary);
                else
                    sortArray(leftPointer, rightBoundary);
                return;
            } else {
                /*
                 * a new thread can be forked
                */
                if(sortLeftHalfParallelPossible) {
                    /*
                     * sort left half in new thread
                     * left half big enough to get sorted in a new thread (because of overhead)
                     * sort right half in actual thread
                     * this avoids a waiting actual thread
                     */
                    quickSortThreadChild = new QuickSortThread<>(unsortedList, threadsAvailable, minimalSize, leftBoundary, rightPointer, this.getThreadGroup());
                    quickSortThreadChild.start();
                    if(sortRightHalfParallelPossible)
                        sortArrayParallel(leftPointer, rightBoundary);
                    else
                        sortArray(leftPointer, rightBoundary);
                } else if(sortRightHalfParallelPossible) {
                   /*
                    * a new thread can be forked
                    * left half to small to get sorted in a new thread
                    * but right half is big enough
                    * sort right half in new thread
                    * sort left half in actual thread
                    * this avoids a waiting actual thread
                    */
                    quickSortThreadChild = new QuickSortThread<>(unsortedList, threadsAvailable, minimalSize, leftPointer, rightBoundary, this.getThreadGroup());
                    quickSortThreadChild.start();
                    sortArray(leftBoundary, rightPointer);

                } else {
                    /*
                     * both halves to small to involve starting a new thread to sort them
                     * sort left half in actual thread
                     * sort right half in actual thread
                     */
                    sortArray(leftBoundary, rightPointer);
                    sortArray(leftPointer, rightBoundary);
                    return;
                }
            }
        } else if(sortRightHalf) {
            /*
             * just right half needs to get sorted
             * instead of forking new thread use actual thread
             * to avoid creating a thread unnecessarily and leaving actual thread waiting
             */
            if(sortRightHalfParallelPossible)
                sortArrayParallel(leftPointer, rightBoundary);
            else
                sortArray(leftPointer, rightBoundary);
            return;
        } else if(sortLeftHalf) {
            /*
             * just left half needs to get sorted
             * instead of forking new thread use actual thread
             * to avoid creating a thread unnecessarily and leaving actual thread waiting
             */
            if(sortLeftHalfParallelPossible)
                sortArrayParallel(leftBoundary, rightPointer);
            else
                sortArray(leftBoundary, rightPointer);
            return;
        }
        if(quickSortThreadChild != null)
            try {
                quickSortThreadChild.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        /*
         * nothing to sort
         */
    }

    public void sortArrayIterative(int leftBoundary, int rightBoundary) {
        Stack<Integer> quickSortStack = new Stack<>();
        while(true) {
            while(leftBoundary < rightBoundary) {
                T pivotElement = choosePivotElement(leftBoundary, rightBoundary);
                /*
                 * sort right half later
                 */
                quickSortStack.push(rightBoundary);
                /*
                 + to compensate increment/decrement in first loop
                 */
                int leftPointer = leftBoundary - 1;
                rightBoundary++;
                while(true) {
                    do {
                        leftPointer++;
                    } while(unsortedList.get(leftPointer).compareTo(pivotElement) < 0);
                    do {
                        rightBoundary--;
                    } while(unsortedList.get(rightBoundary).compareTo(pivotElement) > 0);
                    if(leftPointer >= rightBoundary)
                        break;
                    swapElements(leftPointer, rightBoundary);
                }
            }
            if(quickSortStack.empty())
                break;
            leftBoundary = rightBoundary + 1;
            rightBoundary = quickSortStack.pop();
        }
    }

    public void sortArray(int leftBoundary, int rightBoundary) {
        int leftPointer = leftBoundary;
        int rightPointer = rightBoundary;
        T pivotElement = choosePivotElement(leftBoundary, rightBoundary);
        if((rightPointer - leftPointer) == 1) {
            if(unsortedList.get(leftPointer).compareTo(unsortedList.get(rightPointer)) > 0)
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
            sortArray(leftBoundary, rightPointer);
        if(leftPointer < rightBoundary)
            sortArray(leftPointer, rightBoundary);
    }
}
