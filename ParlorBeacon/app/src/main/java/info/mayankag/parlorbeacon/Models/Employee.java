package info.mayankag.parlorbeacon.Models;

public class Employee {

    private String Name;
    private String Email;
    private String PhoneNo;
    private String Designation;

    public Employee(String name, String email, String phoneNo, String designation)
    {
        Name = name;
        Email = email;
        PhoneNo = phoneNo;
        Designation = designation;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }
}
