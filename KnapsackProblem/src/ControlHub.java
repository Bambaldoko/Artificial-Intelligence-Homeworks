import java.util.*;

public class ControlHub {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int M = input.nextInt();
        int N = input.nextInt();

        Item[] items = new Item[N];
        for(int i = 0; i < N; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        int counter = 0;
        int limit = 1; // 500 or 1500
        Map<Integer, Integer> map = new HashMap<>();

        while(counter < limit) {
            Solver solver = new Solver(M, items);
            int result = solver.solve();
            map.merge(result, 1, (a, b) -> a + b);

            counter++;
//            System.out.println("Retry number " + counter);
        }

        // statistics below //
//        List<Map.Entry<Integer,Integer>> entries = new ArrayList<>(
//                map.entrySet()
//        );
//        entries.sort(Comparator.comparingInt(Map.Entry::getKey));
//        for (Map.Entry<Integer,Integer> e : entries) {
//            System.out.println(e.getKey()+":"+e.getValue() + ":" + e.getValue() / (float)limit * 100 + "%");
//        }
    }
}
