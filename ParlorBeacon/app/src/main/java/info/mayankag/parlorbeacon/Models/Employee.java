package info.mayankag.parlorbeacon.Models;

public class Employee {

    private String Name;
    private String Email;
    private String PhoneNo;
    private String Designation;

    public Employee(String name, String email, String phoneNo, String designation) {
        Name = name;
        Email = email;
        PhoneNo = phoneNo;
        Designation = designation;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }
}
