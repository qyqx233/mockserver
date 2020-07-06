package alg.sort;

public class QuickSort {
    <T> void swap(T[] arr, int x, int y) {
        T tmp = arr[x];
        arr[x] = arr[y];
        arr[y] = tmp;
    }

    <T extends Comparable> T SelectPivotMedianOfThree(T arr[], int low, int high) {
        int mid = low + ((high - low) >> 1);//计算数组中间的元素的下标

        //使用三数取中法选择枢轴
        if (arr[mid].compareTo(arr[high]) > 0)//目标: arr[mid] <= arr[high]
        {
            swap(arr, mid, high);
        }
        if (arr[low].compareTo(arr[high]) > 0)//目标: arr[low] <= arr[high]
        {
            swap(arr, low, high);
        }
        if (arr[mid].compareTo(arr[low]) > 0) //目标: arr[low] >= arr[mid]
        {
            swap(arr, mid, low);
        }
        //此时，arr[mid] <= arr[low] <= arr[high]
        return arr[low];
        //low的位置上保存这三个位置中间的值
        //分割时可以直接使用low位置的元素作为枢轴，而不用改变分割函数了
    }


    <T extends Comparable> void QSort(T arr[], int low, int high) {
        int first = low;
        int last = high;

        int left = low;
        int right = high;

        int leftLen = 0;
        int rightLen = 0;

        if (high - low + 1 < 10) {
            InsertSort.sort(arr, low, high);
            return;
        }

        //一次分割
        T key = SelectPivotMedianOfThree(arr, low, high);//使用三数取中法选择枢轴

//        while (low < high) {
//            while (high > low && arr[high] >= key) {
//                if (arr[high] == key)//处理相等元素
//                {
//                    swap(arr, right, high);
//                    right--;
//                    rightLen++;
//                }
//                high--;
//            }
//            arr[low] = arr[high];
//            while (high > low && arr[low] <= key) {
//                if (arr[low] == key) {
//                    swap(arr[left], arr[low]);
//                    left++;
//                    leftLen++;
//                }
//                low++;
//            }
//            arr[high] = arr[low];
//        }
//        arr[low] = key;
//
//        //一次快排结束
//        //把与枢轴key相同的元素移到枢轴最终位置周围
//        int i = low - 1;
//        int j = first;
//        while (j < left && arr[i] != key) {
//            swap(arr[i], arr[j]);
//            i--;
//            j++;
//        }
//        i = low + 1;
//        j = last;
//        while (j > right && arr[i] != key) {
//            swap(arr[i], arr[j]);
//            i++;
//            j--;
//        }
//        QSort(arr, first, low - 1 - leftLen);
//        QSort(arr, low + 1 + rightLen, last);
    }

    public static void main(String[] args) {
        Integer a = 1, b = 2, c = 2;
        System.out.println(a.compareTo(b));
        System.out.println(c.compareTo(b));
        System.out.println(c.compareTo(a));
    }
}
