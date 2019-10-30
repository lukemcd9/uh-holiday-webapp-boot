package edu.hawaii.its.holiday.type;

import java.io.Serializable;

public class Employee implements Serializable {

    public static final long serialVersionUID = 2L;
    private Long id;

    public Employee() {
        // Empty.
    }

    public Employee(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + "]";
    }

}
