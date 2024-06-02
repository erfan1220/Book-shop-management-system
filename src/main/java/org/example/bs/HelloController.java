package org.example.bs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.sql.*;

import static javafx.fxml.FXMLLoader.load;

public class HelloController implements Initializable {
    @FXML
    private TextField QuestionCR;

    @FXML
    private Button createbtnCR;

    @FXML
    private DatePicker dateCR;

    @FXML
    private Button loginbtnCR;

    @FXML
    private PasswordField passwordCR;

    @FXML
    private TextField usernameCR;

    @FXML
    private Button createbtnlog;

    @FXML
    private TextField roleCR;

    @FXML
    private PasswordField passwordlog;

    @FXML
    private TextField usernamelog;


    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    private Error error;

    public void switchtoCreateAcc(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "CreateAccount.fxml");
    }

    public void switchtoLogin(ActionEvent event) throws IOException {
        Switch s1 = new Switch();
        s1.switchto(event, "hello-view.fxml");
    }

    public void switchForgetPass(ActionEvent event) throws IOException {
//        mail il = new mail();
//        il.email("sltanyh1384@gmail.com");
        Switch s1 = new Switch();
        s1.switchto(event, "ForgetPass.fxml");
    }
    public void loginbtn2(ActionEvent event) throws IOException {
        Static.name = usernamelog.getText();
        if (usernamelog.getText().isEmpty() || passwordlog.getText().isEmpty()) {
            error = new Error();
            error.setfield("Invalid information.");
        } else {
            String logdeta = "SELECT name, password FROM information WHERE name = ? and password = ? " +
                    "and role = 'user'";
            String logindeta = "SELECT name, password FROM information WHERE name = ? and password = ? " +
                    "and role = 'admin'";
            try {
                connect = Detabase.CODB();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                prepare = connect.prepareStatement(logdeta);
                prepare.setString(1, usernamelog.getText());
                prepare.setString(2, passwordlog.getText());
                resultSet = prepare.executeQuery();
                if (resultSet.next()) {
                    Switch s1 = new Switch();
                    s1.switchto(event, "UserPage.fxml");
                } else {
                    prepare = connect.prepareStatement(logindeta);
                    prepare.setString(1, usernamelog.getText());
                    prepare.setString(2, passwordlog.getText());
                    resultSet = prepare.executeQuery();
                    if(resultSet.next()){
                        Switch s1 = new Switch();
                        s1.switchto(event, "Adminpage.fxml");
                    }else {
                        error = new Error();
                        error.setfield("Invalid information.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createACbtn() throws SQLException {
        if (usernameCR.getText().isEmpty() || passwordCR.getText().isEmpty() ||
                QuestionCR.getText().isEmpty() || dateCR.getValue() == null ||
            roleCR.getText().isEmpty()) {
            error = new Error();
            error.setfield("Please fill out all field");
        } else if (passwordCR.getText().length() < 5) {
            error = new Error();
            error.setfield("Your password should have at least 5 characters.");
        } else {
            String signdeta = "INSERT INTO information (name , password, city,Date, role)" +
                    "VALUES(?,?,?,?,?)";
            connect = Detabase.CODB();
            try {
                String checkname = "SELECT name FROM information WHERE name = '" +
                        usernameCR.getText() + "'";
                prepare = connect.prepareStatement(checkname);
                resultSet = prepare.executeQuery();
                if (resultSet.next()) {
                    error = new Error();
                    error.setfield("This username has already been taken.");
                } else {
                    prepare = connect.prepareStatement(signdeta);
                    prepare.setString(1, usernameCR.getText());
                    prepare.setString(2, passwordCR.getText());
                    prepare.setString(3, QuestionCR.getText());
                    prepare.setString(4, String.valueOf(dateCR.getValue()));//*******
                    prepare.setString(5,roleCR.getText());
                    prepare.executeUpdate();

                    error = new Error();
                    error.update("Successfully registered Account.");

                    usernameCR.setText("");
                    passwordCR.setText("");
                    QuestionCR.setText("");
                    roleCR.setText("");
                    dateCR.setValue(LocalDate.parse("2020-05-01"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}