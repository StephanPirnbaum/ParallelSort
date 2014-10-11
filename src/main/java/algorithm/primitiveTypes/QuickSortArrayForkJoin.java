package main.java.algorithm.primitiveTypes;

/**
 * Created by stephan on 6/7/14.
 */
public class QuickSortArrayForkJoin extends ArrayForkJoinSort {

    public QuickSortArrayForkJoin(int[] unsortedArray, int minimalSize, int leftBoundary, int rightBoundary) {
        super(unsortedArray, minimalSize, leftBoundary, rightBoundary);
    }

    @Override
    protected void compute() {
        if (leftBoundary < rightBoundary){
            int pivotElementPosition = partition(leftBoundary, rightBoundary);
            if(rightBoundary - leftBoundary < 1000) {
                sortArrayDirect(leftBoundary, pivotElementPosition - 1);
                sortArrayDirect(pivotElementPosition, rightBoundary);

            } else {
                invokeAll(new QuickSortArrayForkJoin(unsortedArray, minimalSize, leftBoundary, pivotElementPosition-1),
                        new QuickSortArrayForkJoin(unsortedArray, minimalSize, pivotElementPosition, rightBoundary));
            }
        }

    }

    /**
     * Computes the pivot element of the list in the given bounds
     * @param leftBoundary The left boundary
     * @param rightBoundary The right boundary
     * @return The pivot element computed
     */
    private int choosePivotElement(int leftBoundary, int rightBoundary) {
        int middle = (rightBoundary + leftBoundary) / 2;
        int leftElement = unsortedArray[leftBoundary];
        int middleElement = unsortedArray[middle];
        int rightElement = unsortedArray[rightBoundary];
        if(leftElement > rightElement) {
            swapElements(leftBoundary, rightBoundary);
            leftElement = rightElement;
            rightElement = unsortedArray[rightBoundary];
        }
        if(leftElement > middleElement) {
            swapElements(leftBoundary, middle);
            middleElement = leftElement;
        }
        if(rightElement < middleElement)
            swapElements(middle, rightBoundary);
        return unsortedArray[middle];
    }

    private int partition(int leftBoundary, int rightBoundary) {
        int leftPointer = leftBoundary;
        int rightPointer = rightBoundary;
        int pivotElement = choosePivotElement(leftBoundary, rightBoundary);
        while(leftPointer <= rightPointer) {
            while(unsortedArray[leftPointer] < pivotElement) leftPointer++;
            while(unsortedArray[rightPointer] > pivotElement) rightPointer--;
            if(leftPointer <= rightPointer) {
                swapElements(leftPointer, rightPointer);
                leftPointer++;
                rightPointer--;
            }
        }
        return leftPointer;
    }

    @Override
    public void sortArrayDirect(int leftBoundary, int rightBoundary) {
        int leftPointer = leftBoundary;
        int rightPointer = rightBoundary;
        int pivotElement = choosePivotElement(leftBoundary, rightBoundary);
        if((rightPointer - leftPointer) == 1) {
            if (unsortedArray[leftPointer] > unsortedArray[rightPointer])
                swapElements(leftPointer, rightPointer);
            return;
        }
        while(leftPointer <= rightPointer) {
            while(unsortedArray[leftPointer] < pivotElement) leftPointer++;
            while(unsortedArray[rightPointer] > pivotElement) rightPointer--;
            if(leftPointer <= rightPointer) {
                swapElements(leftPointer, rightPointer);
                leftPointer++;
                rightPointer--;
            }
        }
        if(leftBoundary < rightPointer)
            sortArrayDirect(leftBoundary, rightPointer);
        if(leftPointer < rightBoundary)
            sortArrayDirect(leftPointer, rightBoundary);
    }

}

