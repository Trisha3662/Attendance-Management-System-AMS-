package attendance.models;

public class Student extends Person {
    private String name;
    private int id;
    private String course;

    public Student(String name, int id){
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
   
    public String getCourse() { 
    	return course; 
    }
    public void setCourse(String course) { 
    	this.course = course; 
    }

    @Override
    public boolean equals(Object obj) {    // if we enter same id it will error
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;
        Student student = (Student) obj;
        return id == student.id;
    }

    @Override
    public int hashCode() {     //When you override equals(), you must override hashCode()
        return id;
    }

	@Override
	public void displayDetails() {
		// TODO Auto-generated method stub
		System.out.println(0);
		
	}
}