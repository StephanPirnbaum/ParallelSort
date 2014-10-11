package main.java.algorithm.primitiveTypes;

/**
 * Created by stephan on 6/8/14.
 */
public class MergeSortArrayForkJoin extends ArrayForkJoinSort {

    public MergeSortArrayForkJoin(int[] unsortedArray, int minimalSize, int leftBoundary, int rightBoundary) {
        super(unsortedArray, minimalSize, leftBoundary, rightBoundary);
    }

    @Override
    protected void compute() {
        int middle = (rightBoundary + leftBoundary) / 2;
        if(leftBoundary < rightBoundary) {
//            if((rightBoundary - leftBoundary) < minimalSize) {
            if((middle - leftBoundary) < minimalSize) {
                sortArrayDirect(leftBoundary, middle);
                sortArrayDirect(middle + 1, rightBoundary);
            } else {
                MergeSortArrayForkJoin leftHalf = new MergeSortArrayForkJoin(unsortedArray, minimalSize, leftBoundary, middle);
                MergeSortArrayForkJoin rightHalf = new MergeSortArrayForkJoin(unsortedArray, minimalSize, middle + 1, rightBoundary);
                invokeAll(leftHalf, rightHalf);
            }
            merge(leftBoundary, middle, unsortedArray, middle + 1, rightBoundary);
        }
    }

    @Override
    void sortArrayDirect(int leftBoundary, int rightBoundary) {
        if(rightBoundary - leftBoundary > 1) {
            int middle = (rightBoundary + leftBoundary) / 2;
            sortArrayDirect(leftBoundary, middle);
            sortArrayDirect(middle + 1, rightBoundary);
            merge(leftBoundary, middle, unsortedArray, middle + 1, rightBoundary);
        } else if(rightBoundary - leftBoundary == 1) {
            if(unsortedArray[leftBoundary] > unsortedArray[rightBoundary]) {
                swapElements(leftBoundary, rightBoundary);
            }
        }
    }

    /**
     * Merges the given parts of the list
     * @param leftLeftBoundary The left boundary of the left part
     * @param leftRightBoundary The right boundary of the left part
     * @param unsortedArray The list to sort
     * @param rightLeftBoundary The left boundary of the right part
     * @param rightRightBoundary The right boundary of the left part
     */
    private void merge(int leftLeftBoundary, int leftRightBoundary, int[] unsortedArray, int rightLeftBoundary, int rightRightBoundary) {
        int length = leftRightBoundary - leftLeftBoundary + 1 + rightRightBoundary - rightLeftBoundary + 1;
        int[] temporaryList = new int[length];
        int index = 0;
        int leftStartPosition = leftLeftBoundary;
        int leftElem;
        int rightElem;
        while(leftLeftBoundary <= leftRightBoundary && rightLeftBoundary <= rightRightBoundary) {
            leftElem = unsortedArray[leftLeftBoundary];
            rightElem = unsortedArray[rightLeftBoundary];
            if(leftElem <= rightElem) {
                temporaryList[index] = leftElem;
                leftLeftBoundary++;
                index++;
            } else {
                temporaryList[index] = rightElem;
                rightLeftBoundary++;
                index++;
            }
        }
        while(leftLeftBoundary <= leftRightBoundary) {
            temporaryList[index] = unsortedArray[leftLeftBoundary];
            leftLeftBoundary++;
            index++;
        }
        while(rightLeftBoundary <= rightRightBoundary) {
            temporaryList[index] = unsortedArray[rightLeftBoundary];
            rightLeftBoundary++;
            index++;
        }
        System.arraycopy(temporaryList, 0, unsortedArray, leftStartPosition, length);
    }
}
