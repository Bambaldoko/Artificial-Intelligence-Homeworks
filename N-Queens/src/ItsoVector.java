import java.util.Random;
import java.util.Vector;

public class ItsoVector {
    // Member variables //
    private int[] data;
    private int end;

    // Constructors //
    ItsoVector(int N) {
        data = new int[N];
        end = 0;
    }

    ItsoVector(int[] array) {
        data = array;
        end = array.length;
    }

    // Methods //
    public void reset() {
        end = 0;
    }

    public void add(int number) {
        if(end >= data.length) return;

        data[end++] = number;
    }

    public void shuffle() {
        int index, temp;
        Random random = new Random();
        for (int i = data.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = data[index];
            data[index] = data[i];
            data[i] = temp;
        }
    }

    public int give() {
        if(end == 1) {
            return data[0];
        }
        return returnRandom();
    }

    private int returnRandom() {
        int index;
        Random random = new Random();
        index = random.nextInt(end);
        return data[index];
    }

    // Printers //
    public void print() {
        for (int number : data) {
            System.out.println(number);
        }
    }
}
