package de.dcsquare.paho.client.subscriber;
import javafx.scene.Scene;
import org.eclipse.paho.client.mqttv3.*;

/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class SubscribeCallback implements MqttCallback {

    private DashboardController dbc;

    public SubscribeCallback(Scene _scene, String BROKER_URL){
        dbc = new DashboardController();
        dbc.setScene(_scene, BROKER_URL);
    }

    @Override
    public void connectionLost(Throwable cause) {
        //This is called when the connection is lost. We could reconnect here.
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        System.out.println("Message arrived. Topic: " + topic + "  Message: " + message.toString());

        if ("home/LWT".equals(topic)) {
            System.err.println("Sensor gone!");
        }

        dbc.handleMessage(message.toString(), topic);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //no-op
    }
}
