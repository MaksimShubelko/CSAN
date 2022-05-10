package com.example.csan;

import com.example.csan.command.Command;
import com.example.csan.command.impl.CommandProvider;
import com.example.csan.controller.client.PingClient;
import com.example.csan.controller.server.PingServer;
import com.example.csan.model.dao.QueryDao;
import com.example.csan.model.dao.TransactionManager;
import com.example.csan.model.dao.impl.QueryDaoImpl;
import com.example.csan.model.entity.Query;
import com.example.csan.util.Parser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public ChoiceBox<String> portBox;

    private int port;

    @FXML
    private ScrollPane historyAnchorPane;

    private AdditionStage additionStage = new AdditionStage();

    @FXML
    private Pane mainAnchorPane;

    private TransactionManager transactionManager = TransactionManager.getInstance();

    private QueryDao queryDao = QueryDaoImpl.getInstance();

    private PingServer pingServer = new PingServer();

    private Thread serverThread = new Thread(pingServer);

    private AdditionController additionController = new AdditionController();

    @FXML
    private TextField queryField;

    @FXML
    private TextArea resultField;



    public MainController() throws IOException {

    }

    public void pingAddress(ActionEvent actionEvent) throws IOException, InterruptedException {
        Thread clientThread = null;
        PingClient.setPort(port);
        PingClient pingClient = new PingClient();
        Parser parser = new Parser();
        Map<String, String> commands = parser.parseQuery(queryField.getText());
        pingClient.setAddress(parser.getAddress());
        try {
            if (serverThread.isAlive()) {
                serverThread.start();
            }
            Command.setData(commands);
            pingClient.setIP_REGEX("");
            for (String command : commands.keySet()) {
                Command commandExecution = CommandProvider.defineCommand(command);
                commandExecution.execute(pingClient);
            }
            transactionManager.initTransaction();
            queryDao.addQuery(queryField.getText());
            clientThread = new Thread(pingClient);
            clientThread.start();
            Thread.sleep(1000);
            resultField.setText(pingClient.getStringBuffer().toString());
            transactionManager.endTransaction();
        } catch (Exception e) {
            transactionManager.rollback();
        } finally {
            transactionManager.commit();
            while (true) {
                boolean isClientInterrupted = pingClient.isInterrupted();
                if (isClientInterrupted) {
                    break;
                }
                Thread.sleep(100);
            }
        }
    }

    public void hideHistory(ActionEvent actionEvent) throws IOException {
        historyAnchorPane.setVisible(false);
        historyAnchorPane.prefHeight(0);
    }

    public void openHistory(ActionEvent actionEvent) throws IOException {
        int positionY = 0;
        try {
            transactionManager.initTransaction();
            historyAnchorPane.setVisible(true);
            AnchorPane root = new AnchorPane();
            List<Query> queries = queryDao.findAllQuery();
            for (Query query : queries) {
                Button button = new Button();
                button.setPrefWidth(250);
                button.setStyle("-fx-background-color: white");
                Text text = new Text(270, positionY + 15, query.getDate().toString());
                text.setText(query.getDate().toString());
                text.setVisible(true);
                button.setText(query.getQuery());
                EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        queryField.setText(button.getText());
                        event.consume();
                    }
                };
                button.setOnAction(buttonHandler);
                button.setLayoutY(positionY);
                root.getChildren().addAll(button, text);
                positionY += 30;
            }
            historyAnchorPane.setContent(root);
            historyAnchorPane.setHmax(400);
            Insets insets = new Insets(20D);
            historyAnchorPane.setPadding(insets);
            transactionManager.endTransaction();
        } catch (Exception e) {
            transactionManager.rollback();
        } finally {
            transactionManager.commit();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setPort(ActionEvent actionEvent) {
        setPort(Integer.parseInt(portBox.getValue()));
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ChoiceBox<String> getPortBox() {
        return portBox;
    }

    public void loadPorts(ActionEvent actionEvent) throws IOException {
        File file = new File("src/main/resources/port");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ObservableList<String> stringObservableList = FXCollections.observableArrayList();
        stringObservableList.add(bufferedReader.readLine());
        portBox.setItems(stringObservableList);
    }

    public void clear(ActionEvent actionEvent) {
        resultField.setText("");
    }

    public void clearHistory(ActionEvent actionEvent) {
        try {
            transactionManager.initTransaction();
            queryDao.deleteAllQuery();
            transactionManager.endTransaction();
        } catch (Exception e) {
            transactionManager.rollback();
        } finally {
            transactionManager.commit();
        }
    }

    public void openHelper(ActionEvent actionEvent) throws IOException {
        additionStage.showStage();
        additionStage.setTextAreaText(readUsingApacheCommonsIO("src/main/resources/help"));
        additionController.openAbout();
    }


    public static String readUsingApacheCommonsIO(String fileName) throws IOException {
        return FileUtils.readFileToString(new File(fileName), String.valueOf(StandardCharsets.UTF_8));
    }

    public void openAuthors(ActionEvent actionEvent) throws IOException {
        additionStage.showStage();
        additionStage.setTextAreaText(readUsingApacheCommonsIO("src/main/resources/authors"));
        additionController.openAbout();
    }
}