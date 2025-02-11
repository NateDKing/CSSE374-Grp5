package umlparser;

public class FieldInfo {
    private String name;
    private String type;
    private int access;

    public FieldInfo(String name, String type, int access) {
        this.name = name;
        this.type = type;
        this.access = access;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getAccess() {
        return access;
    }
}