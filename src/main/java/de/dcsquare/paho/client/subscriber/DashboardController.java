package de.dcsquare.paho.client.subscriber;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    private Scene scene;
    private List<MQTTTopic> activeTopics = new ArrayList<MQTTTopic>();
    private TableView mqttTable;
    private Label brokerURL;


    public void setScene(Scene _scene, String BROKER_URL) {
        this.scene = _scene;
        this.mqttTable = (TableView) scene.lookup("#mqttTable");
        this.brokerURL = (Label) scene.lookup("#brokerURL");

        //setup Table columns
        TableColumn topicColumn = new TableColumn("Topic");
        TableColumn valueColumn = new TableColumn("Value");
        TableColumn stateColumn = new TableColumn("State");
        TableColumn datetimeColumn = new TableColumn("last update");

        topicColumn.prefWidthProperty().bind(mqttTable.widthProperty().divide(4));
        valueColumn.prefWidthProperty().bind(mqttTable.widthProperty().divide(4));
        stateColumn.prefWidthProperty().bind(mqttTable.widthProperty().divide(4));
        datetimeColumn.prefWidthProperty().bind(mqttTable.widthProperty().divide(4));

        topicColumn.setCellValueFactory(new PropertyValueFactory<MQTTTopic, String>("topic"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<MQTTTopic, String>("recentValue"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<MQTTTopic, String>("state"));
        datetimeColumn.setCellValueFactory(new PropertyValueFactory<MQTTTopic, String>("lastUpdate"));

        mqttTable.getColumns().addAll(topicColumn, valueColumn, stateColumn, datetimeColumn);

        //init Broker URL Label
        brokerURL.setText("Listening to Broker: " + BROKER_URL);

    }


    public void handleMessage(String message, String topic) {


        //sensor gone
        if (topic.equals("toju/LWT")) {
            for (MQTTTopic d : activeTopics) {
                //assume that one sensor publishes several values to one topic, so in this case all sensors
                // sending to topic/* "die"
                if (topic.indexOf(d.getTopic().substring(0, d.getTopic().indexOf("/"))) != -1) {
                    d.setState("Sensor died");
                }
            }
            refreshTable();
        }

        //check if the topic has already been registered before
        MQTTTopic currentTopic = null;

        for (MQTTTopic d : activeTopics) {
            if (d.getTopic().equals(topic)) {
                currentTopic = d;
            }
        }
        handleTopics(currentTopic, topic, message);


    }

    public void handleTopics(MQTTTopic currentTopic, String topic, String message) {
        //create new topic
        if (currentTopic == null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    MQTTTopic newTopic = new MQTTTopic(topic, message);
                    activeTopics.add(newTopic);

                    refreshTable();
                }
            });
        } else {
            //update current topic
            currentTopic.setRecentValue(message);
            refreshTable();
        }
    }

    public void refreshTable() {
        //workaround for .refresh(), which is causing trouble
        mqttTable.getItems().clear();
        mqttTable.getItems().addAll(activeTopics);

    }
}
