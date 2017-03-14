package info.mayankag.parlorbeacon.Models;

public class Service {

    private String Name;
    private String Duration;
    private String Type;

    public Service(String name, String duration, String type)
    {
        Name = name;
        Duration = duration;
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public String getDuration() {
        return Duration;
    }

    public String getType() {
        return Type;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public void setType(String type) {
        Type = type;
    }
}
