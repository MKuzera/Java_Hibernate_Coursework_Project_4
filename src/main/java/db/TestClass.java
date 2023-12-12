package db;

import jakarta.persistence.*;

@Entity
@Table (name = "test")
public class TestClass {


    private String name;
    private String lname;

    public String getName() {
        return name;
    }

    public String getLname() {
        return lname;
    }

    public double getSalary() {
        return salary;
    }

    private double salary;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public TestClass(){

    }

    public TestClass(String name, String lname, double salary) {
        this.name = name;
        this.lname = lname;
        this.salary = salary;
    }
}
