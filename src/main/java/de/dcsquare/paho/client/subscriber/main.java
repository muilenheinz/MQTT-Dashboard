package de.dcsquare.paho.client.subscriber;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class main extends Application{

    public static String channel = "toju";
    public static Stage primaryStage = null;
    private Scene scene;
    private static TableView mqttTable;
    private List<MQTTTopic> activeTopics = new ArrayList<MQTTTopic>();

    public static String getChannel(){
        return channel;
    }
    public static Scene getScene() {
        if (primaryStage == null) {
            System.out.println("scene not yet created");
        }
        return primaryStage.getScene();
    }

    //initialize the Application window
    public void start(Stage _primaryStage) throws Exception{
        URL url = new File("src/main/java/de/dcsquare/paho/client/subscriber/dashboard.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        _primaryStage.setTitle("MQTT Dashboard");
        _primaryStage.setScene(new Scene(root, 600, 400));
        _primaryStage.show();

        //scene has to be passed down to Dashboard Controller (trough all classes in hierarchy)
        final Subscriber subscriber = new Subscriber(channel, _primaryStage.getScene());
        subscriber.start();
    }

    public static void main(String... args) {
        Application.launch(args);

    }
}
