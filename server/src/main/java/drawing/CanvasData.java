package drawing;

public class CanvasData {
    private String ip;
    private String name;
    private String canvas;

    public CanvasData(){}

    public CanvasData(String userId, String pass, String vote) {
        this.ip = userId;
        this.name = pass;
        this.canvas = vote;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCanvas() {
        return canvas;
    }

    public void setCanvas(String canvas) {
        this.canvas = canvas;
    }

    @Override
    public String toString() {
        return "ip: " + ip + "\n name: " + name + "\n canvas: " + canvas;
    }
}
