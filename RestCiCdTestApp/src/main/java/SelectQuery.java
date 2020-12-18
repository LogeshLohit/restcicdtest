
public class SelectQuery {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String selectQueryWithOne = "select * from abc;";
		
		System.out.println(selectQueryWithOne.split("(?i)select")[0]);
	}

}
