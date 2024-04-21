import java.util.Arrays;

public class Main {
    private static int[] globalArray;
    private static int[] sortedArray;

    public static void main(String[] args) {
        globalArray = new int[]{9, 7, 3, 5, 6, 1, 8, 2, 10, 4};
        sortedArray = new int[globalArray.length];
        SortingThread thread1 = new SortingThread(0, globalArray.length / 2);
        SortingThread thread2 = new SortingThread(globalArray.length / 2, globalArray.length);
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
            MergingThread mergingThread = new MergingThread();
            mergingThread.start();
            mergingThread.join();
            System.out.println(Arrays.toString(sortedArray));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    static class SortingThread extends Thread {
        private final int startIndex;
        private final int endIndex;

        SortingThread(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
        public void run() {
            Arrays.sort(globalArray, startIndex, endIndex);
        }
    }

    static class MergingThread extends Thread {
        public void run() {
            int i = 0, j = globalArray.length / 2, k = 0;
            while (i < globalArray.length / 2 && j < globalArray.length) {
                if (globalArray[i] < globalArray[j]) {
                    sortedArray[k++] = globalArray[i++];
                } else {
                    sortedArray[k++] = globalArray[j++];
                }
            }

            while (i < globalArray.length / 2) {
                sortedArray[k++] = globalArray[i++];
            }
            while (j < globalArray.length) {
                sortedArray[k++] = globalArray[j++];
            }
        }
    }
}