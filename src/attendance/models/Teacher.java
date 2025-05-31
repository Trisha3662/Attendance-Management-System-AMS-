package attendance.models;

public class Teacher  extends Person{
    private String name;
    private int id;

    public Teacher(String name, int id) {
    	super(name,id);
        this.name = name;
        this.id = id;
    }

    public String getName() { 
        return name; 
    }

    public int getId() { 
        return id; 
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Teacher)) return false;
        Teacher teacher = (Teacher) obj;
        return id == teacher.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

	@Override
	public void displayDetails() {
		// TODO Auto-generated method stub
		System.out.println(0);
		
	}
}