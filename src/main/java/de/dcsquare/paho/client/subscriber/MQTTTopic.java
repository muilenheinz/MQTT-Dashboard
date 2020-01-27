package de.dcsquare.paho.client.subscriber;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MQTTTopic {
    public String topic;
    public String recentValue;
    public String state;
    public LocalDateTime lastUpdate;

    public MQTTTopic(String _topic, String _recentValue) {
        topic = _topic;
        recentValue = _recentValue;
        lastUpdate = LocalDateTime.now();

        //if a Topic is created the sensor is necessarily connected
        if (_topic.indexOf("LWT") == -1) {
            state = "Sending";
        } else {
            state = "Sensor died";
        }
    }

    //needed for the TableView "mqttTable"
    public String getTopic() {
        return topic;
    }
    public String getRecentValue() { return recentValue; }
    public String getState() { return state; }
    public String getLastUpdate() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formattedDate = lastUpdate.format(dateFormat);
        return formattedDate;
    }

    public void setRecentValue(String _value) {
        this.recentValue = _value;
        this.state = "Sending";
        this.lastUpdate = LocalDateTime.now();
    }

    public void setState(String _state) {
        this.state = _state;
        this.lastUpdate = LocalDateTime.now();
    }
}
