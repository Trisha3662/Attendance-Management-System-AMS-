package attendance.models;

public abstract class Person {      //code duplication   and define common behavior  and it acts as a blueprint 
    protected String name;
    protected int id;

    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { 
    	return name; 
    }
    public int getId() {
    	return id; 
    }
    public abstract void displayDetails();
}