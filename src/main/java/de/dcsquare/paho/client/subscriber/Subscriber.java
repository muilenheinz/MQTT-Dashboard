package de.dcsquare.paho.client.subscriber;

import de.dcsquare.paho.client.util.Utils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import javafx.scene.Scene;

/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class Subscriber {

    public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";

    //We have to generate a unique Client id.
    String clientId = Utils.getMacAddress() + "-sub";
    private MqttClient mqttClient;
    private String channel;
    private Scene scene;

    public Subscriber(String _channel, Scene _scene) {
        this.channel = _channel;
        this.scene = _scene;

        try {
            mqttClient = new MqttClient(BROKER_URL, clientId);
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void start() {
        try {
            mqttClient.setCallback(new SubscribeCallback(scene, BROKER_URL));
            mqttClient.connect();

            //Subscribe to all subtopics of "toju"
            final String topic = channel + "/#";
            mqttClient.subscribe(topic);
            System.out.println("Subscriber is now listening to " + topic);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
