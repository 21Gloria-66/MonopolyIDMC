/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package MonopSchool;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author thailakeita
 */
public class Main extends Application{
    @Override
    
    public void start(Stage stage)throws Exception{
        // on charge lemenu principal au démarrage
        FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/vue/MenuView.xml"));
    
    
    Scene scene = new Scene(loader.load(),700,700);
    
    stage.setTitle("MonopSchool-IDMC Nancy");
    stage.setScene(scene);
    stage.setResizable(true);
    stage.show();
} 

public static void main( String [] args){
    launch(args);
}
}
