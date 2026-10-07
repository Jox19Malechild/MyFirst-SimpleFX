package org.example;

// MODEL (slide 15): groups one customer's details together.
// Each Customer object becomes one row in the table (slide 14).
public class Customer {
    private final String name, province;

    public Customer(String name, String province) {
        this.name = name;
        this.province = province;
    }

    // The table columns read these getters (slides 18-19).
    public String getName() { return name; }
    public String getProvince() { return province; }
}
