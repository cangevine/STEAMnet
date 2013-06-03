package dead;

public class SimpleSpark {
	private String name;
	private int id;
	private String type;
	
	public SimpleSpark(String n, int i, String t) {
		name = n;
		id = i;
		type = t;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
}
