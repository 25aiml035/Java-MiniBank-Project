package model;

import java.io.Serializable;

public class Customer implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String email;
    private String mobile;
    private final String customerId;
    private Address address;

    private static long customerCounter = 100;

    private static synchronized String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    public static class Address implements Serializable {
        private static final long serialVersionUID = 1L;
        private String line;
        private String city;
        private String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() { return line; }
        public String getCity() { return city; }
        public String getPincode() { return pincode; }

        @Override
        public String toString() {
            return line + ", " + city + " - " + pincode;
        }
    }

    public Customer(String name, String email, String mobile, Address address) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.customerId = generateCustomerId();
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public String getCustomerId() { return customerId; }
    public Address getAddress() { return address; }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Customer cloned = (Customer) super.clone();
        if (this.address != null) {
            cloned.address = new Address(this.address.line, this.address.city, this.address.pincode);
        }
        return cloned;
    }
}
