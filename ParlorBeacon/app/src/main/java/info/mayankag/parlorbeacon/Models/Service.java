package info.mayankag.parlorbeacon.Models;

public class Service {

    private String Name;
    private String Duration;
    private String Type;

    public Service(String name, String duration, String type) {
        Name = name;
        Duration = duration;
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
