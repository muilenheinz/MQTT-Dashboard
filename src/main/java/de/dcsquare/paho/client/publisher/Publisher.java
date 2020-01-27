package de.dcsquare.paho.client.publisher;

import de.dcsquare.paho.client.subscriber.main;
import de.dcsquare.paho.client.util.Utils;
import org.eclipse.paho.client.mqttv3.*;

/**
 * @author Dominik Obermaier
 * @author Christian Götz
 */
public class Publisher {

    public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";

    private static String channel = main.getChannel();

    public static final String TOPIC_HUMIDITY = channel + "/humidity";
    public static final String TOPIC_TEMPERATURE = channel + "/temperature";

    private MqttClient client;


    public Publisher() {

        //We have to generate a unique Client id.
        String clientId = Utils.getMacAddress() + "-pub";

        try {
            client = new MqttClient(BROKER_URL, clientId);
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void start() {

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setWill(client.getTopic(channel + "/LWT"), "I'm gone :(".getBytes(), 0, false);

            client.connect(options);

            //Publish data forever
            while (true) {

                publishBrightness();

                Thread.sleep(5000);

                publishTemperature();

                Thread.sleep(5000);
                long quiesceTimeout = 0;


            }
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void publishTemperature() throws MqttException {
        final MqttTopic temperatureTopic = client.getTopic(TOPIC_TEMPERATURE);

        final int temperatureNumber = Utils.createRandomNumberBetween(20, 30);
        final String temperature = temperatureNumber + "°C";

        temperatureTopic.publish(new MqttMessage(temperature.getBytes()));

        System.out.println("Published data. Topic: " + temperatureTopic.getName() + "  Message: " + temperature);
    }

    private void publishBrightness() throws MqttException {
        final MqttTopic brightnessTopic = client.getTopic(TOPIC_HUMIDITY);

        final int brightnessNumber = Utils.createRandomNumberBetween(0, 100);
        final String brigthness = brightnessNumber + "%";

        brightnessTopic.publish(new MqttMessage(brigthness.getBytes()));

        System.out.println("Published data. Topic: " + brightnessTopic.getName() + "   Message: " + brigthness);
    }

    public static void main(String... args) {
        final Publisher publisher = new Publisher();
        publisher.start();
    }
}
