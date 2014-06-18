package bresiu.sensordemo;

public class Json {
    private double longitude;
    private double latitude;
    private float speed;
    private String provider;

    public Json(double longitude, double latitude, float speed, String provider) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.provider = provider;
    }
}
