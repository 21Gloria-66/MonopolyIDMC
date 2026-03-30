/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.EtatPartie;
import modele.Plateau;
import modele.GestionnaireSauvegarde;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
 
/**
 *
 * @author thailakeita
 */



import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML private Button btnNouvellePartie;
    @FXML private Button btnCharger;
    @FXML private Button btnQuitter;
    @FXML private VBox panelChargement;
    @FXML private ListView<String> listeSauvegardes;
    @FXML private Label labelVersion;

    private GestionnaireSauvegarde gestionnaire;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionnaire = new GestionnaireSauvegarde();
        panelChargement.setVisible(false);
        panelChargement.setManaged(false);
        labelVersion.setText("v1.0 — IDMC Nancy");
    }

    @FXML
    private void onNouvellePartie() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vue/ConfigView.xml"));
            Scene scene = new Scene(loader.load(), 700, 600);
            Stage stage = (Stage) btnNouvellePartie.getScene().getWindow();
            stage.setTitle("Monopschool — Configuration");
            stage.setScene(scene);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                "Impossible d'ouvrir la configuration : " + e.getMessage()).show();
        }
    }

    @FXML
    private void onCharger() {
        List<String> sauvegardes = gestionnaire.listerSauvegardes();
        if (sauvegardes.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION,
                "Aucune sauvegarde trouvée.").showAndWait();
            return;
        }
        listeSauvegardes.getItems().setAll(sauvegardes);
        panelChargement.setVisible(true);
        panelChargement.setManaged(true);
    }

    @FXML
    private void onLancerSauvegarde() {
        String selected = listeSauvegardes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING,
                "Sélectionnez une sauvegarde.").showAndWait();
            return;
        }
        Plateau plateau = new Plateau();
        EtatPartie etat = gestionnaire.charger(selected, plateau);
        if (etat == null) {
            new Alert(Alert.AlertType.ERROR,
                "Impossible de charger : " + selected).show();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vue/PlateauView.xml"));
            Scene scene = new Scene(loader.load());
            PlateauController ctrl = loader.getController();
            ctrl.injecterJoueurs(etat.getJoueurs());
            Stage stage = (Stage) btnCharger.getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                "Erreur chargement : " + e.getMessage()).show();
        }
    }

    @FXML
    private void onAnnulerChargement() {
        panelChargement.setVisible(false);
        panelChargement.setManaged(false);
    }

    @FXML
    private void onQuitter() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Quitter Monopschool ?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) System.exit(0);
        });
    }
}