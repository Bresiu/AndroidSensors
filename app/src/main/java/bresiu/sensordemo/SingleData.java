package bresiu.sensordemo;

public class SingleData {
    private int generation; // Measurement number
    private long timestamp; // Timestamp in millis, where first measurement = 0

    // Accelerometer
    private double accX;
    private double accY;
    private double accZ;

    // Gyroscope
    private double gyroX;
    private double gyroY;
    private double gyroZ;

    // Magnetometer
    private double magnX;
    private double magnY;
    private double magnZ;

    public SingleData() {
    }

    public SingleData(int generation, long timestamp, double accX, double accY, double accZ,
                      double gyroX, double gyroY, double gyroZ, double magnX, double magnY,
                      double magnZ) {
        this.generation = generation;
        this.timestamp = timestamp;
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
        this.gyroX = gyroX;
        this.gyroY = gyroY;
        this.gyroZ = gyroZ;
        this.magnX = magnX;
        this.magnY = magnY;
        this.magnZ = magnZ;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public void setAccZ(double accZ) {
        this.accZ = accZ;
    }

    public void setGyroX(double gyroX) {
        this.gyroX = gyroX;
    }

    public void setGyroY(double gyroY) {
        this.gyroY = gyroY;
    }

    public void setGyroZ(double gyroZ) {
        this.gyroZ = gyroZ;
    }

    public void setMagnX(double magnX) {
        this.magnX = magnX;
    }

    public void setMagnY(double magnY) {
        this.magnY = magnY;
    }

    public void setMagnZ(double magnZ) {
        this.magnZ = magnZ;
    }

    @Override
    public String toString() {
        return generation + " " + timestamp + " " + accX + " " + accY + " " + accZ +
                gyroX + " " + gyroY + " " + gyroZ + " " + magnX + " " + magnY + " " + magnZ;
    }
}
