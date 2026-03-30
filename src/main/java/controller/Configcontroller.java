/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modele.Joueur;
 
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
 
/**
 *
 * @author thailakeita
 */


public class Configcontroller implements Initializable {

    @FXML private Slider sliderNbJoueurs;
    @FXML private Label labelNbJoueurs;
    @FXML private VBox conteneurJoueurs;
    @FXML private Button btnLancer;

    private static final Color[] COULEURS_DEFAUT = {
        Color.web("#E74C3C"),
        Color.web("#3498DB"),
        Color.web("#2ECC71"),
        Color.web("#F39C12")
    };

    private static final String[] PSEUDOS_DEFAUT = {
        "Étudiant 1", "Étudiant 2", "Étudiant 3", "Étudiant 4"
    };

    private List<TextField> champsNom = new ArrayList<>();
    private List<ColorPicker> colorPickers = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sliderNbJoueurs.setMin(2);
        sliderNbJoueurs.setMax(4);
        sliderNbJoueurs.setValue(2);
        sliderNbJoueurs.setMajorTickUnit(1);
        sliderNbJoueurs.setMinorTickCount(0);
        sliderNbJoueurs.setSnapToTicks(true);
        sliderNbJoueurs.setShowTickLabels(true);

        labelNbJoueurs.setText("2 joueurs");
        genererChampsJoueurs(2);

        sliderNbJoueurs.valueProperty().addListener((obs, oldVal, newVal) -> {
            int nb = newVal.intValue();
            labelNbJoueurs.setText(nb + " joueur" + (nb > 1 ? "s" : ""));
            genererChampsJoueurs(nb);
        });
    }

    private void genererChampsJoueurs(int nbJoueurs) {
        conteneurJoueurs.getChildren().clear();
        champsNom.clear();
        colorPickers.clear();

        for (int i = 0; i < nbJoueurs; i++) {
            HBox ligne = new HBox(12);
            ligne.setPadding(new Insets(8));
            ligne.setAlignment(Pos.CENTER_LEFT);
            ligne.setStyle(
                "-fx-background-color: #F8F9FA;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #DEE2E6;" +
                "-fx-border-radius: 8;"
            );

            Label numLabel = new Label("Joueur " + (i + 1));
            numLabel.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-min-width: 80;"
            );

            TextField tfPseudo = new TextField(PSEUDOS_DEFAUT[i]);
            tfPseudo.setPromptText("Pseudo...");
            tfPseudo.setPrefWidth(180);

            Label cpLabel = new Label("Couleur du pion :");
            cpLabel.setStyle("-fx-font-size: 12px;");

            ColorPicker cp = new ColorPicker(COULEURS_DEFAUT[i]);
            cp.setPrefWidth(110);

            ligne.getChildren().addAll(numLabel, tfPseudo, cpLabel, cp);
            conteneurJoueurs.getChildren().add(ligne);
            champsNom.add(tfPseudo);
            colorPickers.add(cp);
        }
    }

    @FXML
    private void onLancer() {
        List<Joueur> joueurs = new ArrayList<>();

        for (int i = 0; i < champsNom.size(); i++) {
            String pseudo = champsNom.get(i).getText().trim();
            if (pseudo.isEmpty()) pseudo = PSEUDOS_DEFAUT[i];

            Color couleur = colorPickers.get(i).getValue();
            String hexCouleur = String.format("#%02X%02X%02X",
                (int)(couleur.getRed()   * 255),
                (int)(couleur.getGreen() * 255),
                (int)(couleur.getBlue()  * 255));

            joueurs.add(new Joueur(i, pseudo, hexCouleur));
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vue/PlateauView.xml"));
            Scene scenePlateau = new Scene(loader.load());

            PlateauController plateauCtrl = loader.getController();
            plateauCtrl.injecterJoueurs(joueurs);

            Stage stage = (Stage) btnLancer.getScene().getWindow();
            stage.setScene(scenePlateau);
            stage.setTitle("Monopschool — Plateau");
            stage.setMaximized(true);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                "Erreur chargement plateau : " + e.getMessage()).show();
        }
    }
}