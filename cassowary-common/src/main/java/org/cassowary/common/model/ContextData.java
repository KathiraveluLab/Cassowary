package org.cassowary.common.model;

public class ContextData {
    private String deviceId;
    private String type; // e.g., "temperature", "light"
    private Double value;
    private long timestamp;

    public ContextData() {
        this.timestamp = System.currentTimeMillis();
    }

    public ContextData(String deviceId, String type, Double value) {
        this();
        this.deviceId = deviceId;
        this.type = type;
        this.value = value;
    }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
