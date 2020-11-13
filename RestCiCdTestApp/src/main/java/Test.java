import java.util.Map;
import java.util.TreeMap;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
		map.putIfAbsent("logesh", 1);
		map.putIfAbsent("log", 3);
		map.putIfAbsent("logesh", 4);
//		map.computeIfAbsent("3", a -> a.get(a) );
//		map.pu
		System.out.println(map);
	}

}
