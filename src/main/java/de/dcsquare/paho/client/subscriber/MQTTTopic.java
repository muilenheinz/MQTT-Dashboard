package de.dcsquare.paho.client.subscriber;

public class MQTTTopic {
    public String topic;
    public String recentValue;
    public String state;

    public MQTTTopic(String _topic, String _recentValue) {
        topic = _topic;
        recentValue = _recentValue;

        //if a Topic is created the sensor is necessarily connected
        state = "Sending";
    }

    public String getTopic() {
        return topic;
    }
    public String getRecentValue() { return recentValue; }
    public String getState() { return state; }

    public void setRecentValue(String _value) {
        this.recentValue = _value;
        this.state = "Sending";
    }
    public void setState(String _state) {
        this.state = _state;
    }
}
