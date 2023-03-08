package phonebook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {


        List<String> arrRead = readFromFile("D:\\MY DATA\\Downloads\\directory.txt");
        List<String> arrFind = readFromFile("D:\\MY DATA\\Downloads\\find.txt");

        List<String> arrRead2 = arrRead;
        long linearSearchStart = System.currentTimeMillis();
        linearSearch(arrFind, arrRead);
        long linearSearchEnd = System.currentTimeMillis();
        System.out.println("Time taken: "+converionTime(linearSearchEnd - linearSearchStart));
        System.out.println();
        System.out.println("Start searching (bubble sort + jump search)...");
        long bubbleSortStart = System.currentTimeMillis();
        boolean isComplete = bubleSort(arrRead, arrFind, (linearSearchEnd - linearSearchStart));
        long bubbleSortEnd = System.currentTimeMillis();
        if (isComplete){
            long jumpTimeStart = System.currentTimeMillis();
            int count= 0;
            int len = arrFind.size();
            for (String value: arrFind){

                jumpSearch(arrRead,value);
                count++;

            }

            long jumpTimeEnd = System.currentTimeMillis();

            System.out.print("Found "+count+"/"+len+" entries. ");
            System.out.println("Time taken: "+ converionTime((bubbleSortEnd - bubbleSortStart) + (jumpTimeEnd-jumpTimeStart)));
            System.out.println("Sorting time: " + (converionTime(bubbleSortEnd - bubbleSortStart)));
            System.out.println("Searching time: " + converionTime(jumpTimeEnd - jumpTimeStart));
        }
        //Collections.sort(arrRead);
        System.out.println();

        System.out.println("Start searching (quick sort + binary search)...");
        long quicksortStart = System.currentTimeMillis();
        quickSort(arrRead2,0, arrRead2.size()-1);
        long quickSortEnd = System.currentTimeMillis();
        long binarySearchStart = System.currentTimeMillis();
        int countBinary =0;
        for (String s : arrFind){
            binarySearch(arrRead2, s);
            countBinary++;
        }
        long binarySearchEnd = System.currentTimeMillis();
        System.out.print("Found "+countBinary+"/"+arrFind.size()+" entries. ");
        System.out.println("Time taken: "+ converionTime((binarySearchEnd  - binarySearchStart)+(quickSortEnd - quicksortStart)));
        System.out.println("Sorting time: " + (converionTime(quickSortEnd - quicksortStart)));
        System.out.println("Searching time: " + converionTime(binarySearchEnd - binarySearchStart));
        System.out.println();
        System.out.println("Start searching (hash table)...");
        long hashStart = System.currentTimeMillis();
        Hashtable<String, String > hashtable = new Hashtable<>();

        for(String s : arrRead2){
            hashtable.put(s, String.valueOf(s.hashCode()));
        }
        long hashEndTime = System.currentTimeMillis();
        long hashSearchStart = System.currentTimeMillis();
        int countHash = 0;
        for (String s : arrFind){
            hashtable.get(s.hashCode());
            countHash++;
        }
        long hashSearchEnd = System.currentTimeMillis();
        System.out.print("Found "+countHash+"/"+arrFind.size()+" entries. ");
        System.out.println("Time taken: "+ converionTime((hashEndTime - hashStart)+(hashSearchEnd - hashSearchStart)));
        System.out.println("Creating time: " + (converionTime(hashEndTime - hashStart)));
        System.out.println("Searching time: " + converionTime(hashSearchEnd - hashSearchStart));






    }
    public static List<String> readFromFile(String path) throws IOException {

        Path file = Paths.get(path);
        List< String > lines = Files.readAllLines(file);
        List <String> lines2 = new ArrayList<>();
        for (String s :lines) {
             lines2.add(s.replaceAll("\\d+ ", ""));
        }

        return lines2;
    }

    //linear Search
    public static long linearSearch(List<String> arrFind, List<String> arrRead){
        System.out.println("Start searching (linear search)...");
        long startTime = System.currentTimeMillis();

        int count = 0;

        for (String s:arrFind) {
            for (String a: arrRead){
                if (a.equals(s)){
                    count++;
                    break;
                }
            }
        }
        int len = arrFind.size();
        long endTime =System.currentTimeMillis();
        System.out.print("Found "+count+"/"+len+" entries. ");
        converionTime(endTime - startTime);
        return endTime - startTime;
    }
    //bubbleSort
    public static boolean bubleSort(List<String> list, List<String> listFind, long ms){
        boolean isDone = true;
        long startTime = System.currentTimeMillis();
        long endtime =0;
        int n = list.size();
        for (int i = 0; i < n;i++) {

            int start = 0;
            for (int j = 1; j < n-i ; j++){
                int next = start + 1;
                String startString = list.get(start);
                String nextString = list.get(next);
                if ((startString.compareTo(nextString) > 0)){
                    list.set(start,nextString);
                    list.set(next, startString);
                }
                start ++;

            }
            endtime = System.currentTimeMillis();
            if ((endtime - startTime) > (10 * ms)){
                long linearSearchTime = linearSearch(listFind, list);
                System.out.println("Time taken: "+ converionTime((endtime - startTime) + linearSearchTime));
                System.out.println("Sorting time: "+converionTime(endtime - startTime)/*+". - STOPPED, moved to linear search"*/);
                System.out.println("Searching time: "+converionTime(linearSearchTime)+".");
                isDone = false;

                break;

            }
        }
        //endtime = System.currentTimeMillis();

        //System.out.println(list);

        //converionTime(endtime - startTime);
        return isDone;



    }
    public static int jumpSearch(List<String> readList, String value){
        //bubleSort(readList, findList, ms);
        //System.out.println(readList);

        if (readList.isEmpty()){
            return  -1;
        }

        int curr = 0;
        int prev = 0;
        int last = readList.size();
        //System.out.println(last);
        int step = (int) Math.floor(Math.sqrt(readList.size()));
        //System.out.println(step);
        while(readList.get(curr).compareTo(value) < 0){
            if (curr == last){
                return -1;
            }
            prev = curr;
            curr = Math.min(curr + step, last);
        }
        while (readList.get(curr).compareTo(value) > 0){
            curr = curr - 1;
            if (curr <= prev){
                return -1;
            }
        }
        if (readList.get(curr).compareTo(value) == 0){
            //System.out.println(readList.get(curr));
            return curr;
        }

        return -1;
    }


    public static String converionTime(long ms) {
        int min = (int) (ms/60000);
        int milliLeft = (int) (ms % 60000);
        int seconds = milliLeft / 1000;
        milliLeft = milliLeft % 1000;
        return (min+" min. "+seconds+ " sec. "+milliLeft+" ms.");


    }


    public static void quickSort(List<String> list, int low, int high){
        if (low < high) {

            // pi is partitioning index, arr[p]
            // is now at right place
            int pi = partition(list, low, high);

            // Separately sort elements before
            // partition and after partition
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }
    public static void swap(List<String> list ,int i, int j){
        String temp = list.get(i);
        list.set(i,list.get(j));
        list.set(j,temp);

    }
    public static int partition(List<String> list, int low, int high) {
        String pivot = list.get(high);
        int i = low -1;
        for (int j = low; j<= high - 1; j++) {
            if (list.get(j).compareTo(pivot) < 0){
                i++;
                swap(list,i, j);
            }
        }
        String temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high,temp);
        return (i + 1);
    }
    public static int binarySearch(List<String> arr, String val){
        int left = 0;
        int right = arr.size()-1;

        while(left <= right){
            int middle = (left + right) / 2;
            if (arr.get(middle).equals(val)){

                return middle;//return count;

            } else if (arr.get(middle).compareTo(val) > 0) {
                right = middle -1;
            }else
                left = middle + 1;

        }

        return -1;

    }



}
