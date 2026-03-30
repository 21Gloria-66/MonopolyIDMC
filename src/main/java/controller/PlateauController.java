/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import modele.Carte;
import modele.Case;
import modele.CaseUE;
import modele.Joueur;
import modele.Plateau;
import modele.GestionnaireSauvegarde;

 
import java.net.URL;
import java.util.*;
import javafx.scene.control.TextInputDialog;
/**
 *
 * @author thailakeita
 */

public class PlateauController implements Initializable {

    // La structure principale de l'écran
    @FXML private StackPane racine;             // le conteneur racine (tout est dedans)
    @FXML private BorderPane plateauBorderPane; // le plateau avec haut/bas/gauche/droite/centre
    @FXML private GridPane grilleJeu;           // la grille des 40 cases du plateau

    // La popup qui apparaît au centre quand il se passe quelque chose
    // (achat d'UE, paiement de loyer, carte événement...)
    @FXML private VBox conteneurPopup;     // la boîte de la popup
    @FXML private Label labelTitrePopup;   // ex : "🏛️ TAL Fondamentaux"
    @FXML private Label labelCorpsPopup;   // ex : "Prix : 60 ECTS"
    @FXML private Button btnPopupAction;   // bouton "ACHETER" ou "OUI"
    @FXML private Button btnPopupAnnuler;  // bouton "ANNULER" ou "NON"

    // Les HUDs = les petites boîtes aux 4 coins qui affichent les infos joueurs
    // J1 = coin haut gauche, J2 = coin haut droit, J3 = bas gauche, J4 = bas droit
    @FXML private VBox hudJ1;
    @FXML private VBox hudJ2;
    @FXML private VBox hudJ3;
    @FXML private VBox hudJ4;
    @FXML private Label labelEctsJ1, labelEctsJ2, labelEctsJ3, labelEctsJ4;
    @FXML private Label labelPseudoJ1, labelPseudoJ2, labelPseudoJ3, labelPseudoJ4;

    // La barre du bas : affiche le tour, le résultat des dés et le bouton pour jouer
    @FXML private Button btnLancerDes;
    @FXML private Label labelDes;          // affiche ex : "🎲 3 + 5 = 8"
    @FXML private Label labelTourActuel;   // affiche ex : "Tour de : Alice"



    private List<Joueur> joueurs;      // la liste de tous les joueurs de la partie
    private Plateau plateau;           // le plateau avec ses 40 cases
    private Arbitre arbitre;           // l'arbitre qui applique les règles du jeu
    private int joueurActuelIndex;     // l'index du joueur dont c'est le tour (0, 1, 2 ou 3)

    // On stocke les pions (cercles colorés) dans une Map
    // clé = id du joueur, valeur = son cercle graphique
    private Map<Integer, Circle> pions = new HashMap<>();

    // On stocke aussi les cases graphiques dans une Map
    // clé = index de la case (0 à 39), valeur = le StackPane de la case
    private Map<Integer, StackPane> casesUI = new HashMap<>();


    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // La popup est invisible au démarrage, on la montrera si besoin
        conteneurPopup.setVisible(false);
        conteneurPopup.setManaged(false);
    }

    /*
     * Cette méthode est appelée depuis ConfigController,
     * une fois que les joueurs ont été configurés (pseudo + couleur).
     * Elle reçoit la liste des joueurs et démarre la partie.
     */
    public void injecterJoueurs(List<Joueur> joueurs) {
        this.joueurs = joueurs;
        this.plateau = new Plateau();                          // on crée le plateau de 40 cases
        this.arbitre = new Arbitre(joueurs, plateau, this);    // l'arbitre connaît tout
        this.joueurActuelIndex = 0;                            // c'est le joueur 1 qui commence

        configurerHUD();            // afficher les noms et ECTS dans les coins
        construireGrillePlateau();  // dessiner les 40 cases dans la grille
        creerPions();               // placer les pions de chaque joueur sur la case 0
        mettreAJourTourLabel();     // afficher "Tour de : Joueur 1"
    }


    
    //  DEPLACEMENT : "Le Saut de Pion"
    //  Le pion ne glisse pas doucement, il saute de case en case avec une pause de 0.2 secondes entre chaque case.
    
   
    /*
     * Méthode principale de déplacement.
     * On lui donne l'id du joueur et la case d'arrivée,
     * et elle gère tout le déplacement case par case.
     */
    public void deplacerPionSaut(int joueurId, int destination) {
        Joueur joueur = trouverJoueur(joueurId);
        if (joueur == null) return; // sécurité : si le joueur n'existe pas, on arrête

        Circle pion = pions.get(joueurId);
        int posDepart = joueur.getPositionActuelle();
        int nbCases = plateau.getNbcases(); // = 40

        // Calcul du nombre de cases à parcourir
        // Le % nbCases gère le tour complet (si on dépasse la case 39, on repart à 0)
        int pas = (destination - posDepart + nbCases) % nbCases;
        if (pas == 0) pas = nbCases; // cas rare : on fait un tour complet

        // On lance la boucle de saut récursive
        lancerSaut(joueur, pion, posDepart, pas, nbCases, destination);
    }

    /*
     * La boucle récursive du saut.
    
     * On utilise PauseTransition plutôt que Thread.sleep() car on est dans JavaFX
     * et on ne peut pas bloquer le fil d'exécution principal.
     */
    private void lancerSaut(Joueur joueur, Circle pion, int posCourante, int pasRestants, int nbCases, int destination) {

        // Condition d'arrêt : on est arrivé !
        if (pasRestants <= 0) {
            joueur.setPositionActuelle(destination);
            btnLancerDes.setDisable(false); // on réactive le bouton pour le prochain joueur
            arbitre.appliquerEffetCase(joueur, plateau.getcase(destination)); // effet de la case
            return;
        }

        // On retire le pion de la case actuelle (il disparaît)
        retirerPionDeCaseUI(pion, posCourante);

        // On calcule la prochaine case
        int prochaineCase = (posCourante + 1) % nbCases;

        // Si on passe par la case Départ (index 0), on gagne 200 ECTS !
        if (prochaineCase == 0 && posCourante != 0) {
            joueur.ajouterEcts(200);
            labelDes.setText(labelDes.getText() + " +200 ECTS!");
        }

        // On place le pion sur la prochaine case (il apparaît instantanément)
        placerPionSurCaseUI(pion, prochaineCase);

        // On attend 0.2 secondes avant de passer à la case suivante
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(e ->
            lancerSaut(joueur, pion, prochaineCase, pasRestants - 1, nbCases, destination)
        );
        pause.play();
    }


    public void ouvrirPopup(String type, Object donnee) {
        // On bloque le bouton des dés tant que la popup est ouverte
        btnLancerDes.setDisable(true);

        // On rend la popup visible
        conteneurPopup.setVisible(true);
        conteneurPopup.setManaged(true);

        // On remet les deux boutons visibles (certains types n'en utilisent qu'un)
        btnPopupAction.setVisible(true);
        btnPopupAnnuler.setVisible(true);

        switch (type) {

            // -----------------------------------------------------------------
            // CAS 1 : Le joueur tombe sur une UE non achetée
            // On lui propose de l'acheter ou non
            // -----------------------------------------------------------------
            case "ACHAT": {
                CaseUE ue = (CaseUE) donnee;
                Joueur joueur = joueurs.get(joueurActuelIndex);

                labelTitrePopup.setText("🏛️ " + ue.getNom());
                labelCorpsPopup.setText(
                    "Prix d'achat : " + ue.getPrixAchat() + " ECTS\n" +
                    "Loyer de base : " + ue.getLoyerBase() + " ECTS\n" +
                    "Groupe : " + ue.getCouleur() + "\n\n" +
                    "Votre solde : " + joueur.getEcts() + " ECTS"
                );
                btnPopupAction.setText("ACHETER");
                btnPopupAnnuler.setText("ANNULER");

                // Si le joueur clique ACHETER : on retire les ECTS et on lui donne l'UE
                btnPopupAction.setOnAction(e -> {
                    if (joueur.retirerEcts(ue.getPrixAchat())) {
                        ue.setProprietaire(joueur);
                        joueur.ajouterPropriete(ue);
                        mettreAJourCouleurCase(ue); // la case change de couleur sur le plateau
                    }
                    fermerPopup();
                    continuerTour();
                });

                // Si le joueur clique ANNULER : rien ne se passe, on passe au suivant
                btnPopupAnnuler.setOnAction(e -> { fermerPopup(); continuerTour(); });
                break;
            }

            
            // CAS 2 : Le joueur tombe sur une UE qu'il possède déjà
            // On lui propose de l'améliorer (L1 -> L2 -> M1 -> M2)
            
            case "AMELIORATION": {
                CaseUE ue = (CaseUE) donnee;
                Joueur joueur = joueurs.get(joueurActuelIndex);

                labelTitrePopup.setText("Ameliorer " + ue.getNom());
                labelCorpsPopup.setText(
                    "Niveau actuel : " + CaseUE.LABELS_NIVEAU[ue.getNiveauAmelioration()] + "\n" +
                    "Cout de l'amelioration : " + ue.getPrixAmelioration() + " ECTS\n\n" +
                    "Votre solde : " + joueur.getEcts() + " ECTS"
                );
                btnPopupAction.setText("OUI");
                btnPopupAnnuler.setText("NON");

                btnPopupAction.setOnAction(e -> {
                    if (joueur.retirerEcts(ue.getPrixAmelioration())) {
                        ue.ameliorer(); // niveau + 1
                    }
                    fermerPopup();
                    continuerTour();
                });
                btnPopupAnnuler.setOnAction(e -> { fermerPopup(); continuerTour(); });
                break;
            }

           
            // CAS 3 : Le joueur doit payer un loyer OU simple message info
            // On affiche juste un message avec un bouton OK
         
            case "LOYER":
            case "INFO": {
                labelTitrePopup.setText(type.equals("LOYER") ? "Loyer du !" : "Information");
                labelCorpsPopup.setText((String) donnee);
                btnPopupAction.setText("OK");
                btnPopupAnnuler.setVisible(false); // pas besoin d'annuler ici
                btnPopupAction.setOnAction(e -> { fermerPopup(); continuerTour(); });
                break;
            }

           
            // CAS 4 : Le joueur tombe sur une case Action et pioche une carte
            // La carte peut donner des ECTS, en retirer, ou déplacer le joueur

            case "CARTE": {
                Carte carte = (Carte) donnee;
                labelTitrePopup.setText("Evenement IDMC");
                labelCorpsPopup.setText(carte.getDescription());
                btnPopupAction.setText("OK");
                btnPopupAnnuler.setVisible(false);

                btnPopupAction.setOnAction(e -> {
                    fermerPopup();
                    // Si la carte déplace le joueur, on anime le saut vers la nouvelle case
                    if (carte.getType() == Carte.TypeCarte.Deplacement_force) {
                        Joueur j = joueurs.get(joueurActuelIndex);
                        deplacerPionSaut(j.getId(), carte.getValeur());
                    } else {
                        // Sinon on passe juste au joueur suivant
                        continuerTour();
                    }
                });
                break;
            }

            
            // CAS 5 : Le joueur est en faillite (plus d'ECTS)
            // On affiche un message et on le sort du jeu
            
            case "FAILLITE": {
                labelTitrePopup.setText("Faillite !");
                labelCorpsPopup.setText((String) donnee);
                btnPopupAction.setText("OK");
                btnPopupAnnuler.setVisible(false);
                btnPopupAction.setOnAction(e -> fermerPopup());
                break;
            }
        }
    }

    // Ferme la popup et la rend invisible
    private void fermerPopup() {
        conteneurPopup.setVisible(false);
        conteneurPopup.setManaged(false);
    }


    
    @FXML
    private void onLancerDes() {
        btnLancerDes.setDisable(true); // on désactive le bouton pendant le déplacement

        Joueur joueur = joueurs.get(joueurActuelIndex);

        // Tirage aléatoire de deux dés entre 1 et 6
        int de1 = (int)(Math.random() * 6) + 1;
        int de2 = (int)(Math.random() * 6) + 1;
        int total = de1 + de2;

        // On affiche le résultat des dés dans le label
        labelDes.setText("Dés : " + de1 + " + " + de2 + " = " + total);

        // On calcule la case d'arrivée (le % 40 gère le passage par le départ)
        int destination = (joueur.getPositionActuelle() + total) % plateau.getNbcases();

        // Et on lance l'animation du pion !
        deplacerPionSaut(joueur.getId(), destination);
    }

    /*
     * Appelée après que la popup est fermée.
     * Met à jour les HUDs et passe au joueur suivant.
     */
    private void continuerTour() {
        mettreAJourHUD();        // on rafraîchit les ECTS dans les coins
        passerAuJoueurSuivant();
    }

    /*
     * Passe au prochain joueur dans la liste.
     * Si un joueur est en pause (rattrapages), on le saute et on décrémente son compteur.
     */
    private void passerAuJoueurSuivant() {
        joueurActuelIndex = (joueurActuelIndex + 1) % joueurs.size();

        // On vérifie si le joueur suivant est bloqué en rattrapages
        int debut = joueurActuelIndex;
        while (joueurs.get(joueurActuelIndex).isEnPause()) {
            joueurs.get(joueurActuelIndex).decrementerPause(); // -1 tour de blocage
            joueurActuelIndex = (joueurActuelIndex + 1) % joueurs.size();
            // Sécurité : évite une boucle infinie si tout le monde est bloqué
            if (joueurActuelIndex == debut) break;
        }

        mettreAJourTourLabel();
        btnLancerDes.setDisable(false); // le prochain joueur peut lancer les dés
    }


   // initialisation HUD
    private void configurerHUD() {
        int nb = joueurs.size();

        // On cache les coins inutilisés
        hudJ3.setVisible(nb >= 3);
        hudJ3.setManaged(nb >= 3);
        hudJ4.setVisible(nb >= 4);
        hudJ4.setManaged(nb >= 4);

        // On affiche le pseudo et les ECTS de chaque joueur dans son coin
        labelPseudoJ1.setText(joueurs.get(0).getPseudo());
        labelEctsJ1.setText(joueurs.get(0).getEcts() + " ECTS");
        labelPseudoJ2.setText(joueurs.get(1).getPseudo());
        labelEctsJ2.setText(joueurs.get(1).getEcts() + " ECTS");
        if (nb >= 3) {
            labelPseudoJ3.setText(joueurs.get(2).getPseudo());
            labelEctsJ3.setText(joueurs.get(2).getEcts() + " ECTS");
        }
        if (nb >= 4) {
            labelPseudoJ4.setText(joueurs.get(3).getPseudo());
            labelEctsJ4.setText(joueurs.get(3).getEcts() + " ECTS");
        }
    }

    /*
     * Met à jour les ECTS affichés dans les HUDs après chaque action.
     * (achat, loyer, carte...)
     */
    private void mettreAJourHUD() {
        Label[] labelsEcts = {labelEctsJ1, labelEctsJ2, labelEctsJ3, labelEctsJ4};
        for (int i = 0; i < joueurs.size(); i++) {
            labelsEcts[i].setText(joueurs.get(i).getEcts() + " ECTS");
        }
    }

    // Met à jour le label "Tour de : ..."
    private void mettreAJourTourLabel() {
        labelTourActuel.setText("Tour de : " + joueurs.get(joueurActuelIndex).getPseudo());
    }


    // =========================================================================
    //  CONSTRUCTION DE LA GRILLE DU PLATEAU
    // =========================================================================

    /*
     * Dessine les 40 cases du plateau dans la GridPane.
     * Chaque case est un StackPane (une boîte) avec un label dedans.
     */
    private void construireGrillePlateau() {
        for (int i = 0; i < 40; i++) {
            Case c = plateau.getcase(i);
            StackPane casePane = creerCasePane(c); // on crée la boîte graphique
            int[] pos = indexVersGrid(i);           // on calcule sa position dans la grille
            grilleJeu.add(casePane, pos[1], pos[0]);
            casesUI.put(i, casePane); // on la stocke pour pouvoir la retrouver plus tard
        }
    }

    /*
     * Crée le visuel d'une case :
     * - bordure colorée selon le groupe de filière si c'est une UE
     * - fond gris neutre pour les cases spéciales (départ, parking, etc.)
     */
    private StackPane creerCasePane(Case c) {
        StackPane sp = new StackPane();
        sp.setPrefSize(70, 70);

        // Le nom de la case affiché en tout petit pour tenir dans la case
        Label nom = new Label(c.getNom());
        nom.setWrapText(true);
        nom.setStyle("-fx-font-size:7px; -fx-text-alignment:center;");

        // Si c'est une UE, on colorie la bordure selon la filière
        if (c instanceof CaseUE) {
            CaseUE ue = (CaseUE) c;
            sp.setStyle(
                "-fx-border-color:" + couleurGroupe(ue.getCouleur()) + ";" +
                "-fx-border-width:3;" +
                "-fx-background-color:#FAFAFA;"
            );
        } else {
            // Couleur neutre pour les cases spéciales, actions, etc.
            sp.setStyle(
                "-fx-background-color:#EEF2F7;" +
                "-fx-border-color:#BDC3C7;" +
                "-fx-border-width:1;"
            );
        }

        sp.getChildren().add(nom);
        return sp;
    }

    /*
     * Convertit un index de case (0 à 39) en position [row, col] dans la GridPane 11x11.
     *
     * Le plateau est organisé dans le sens horaire :
     *   Index  0 à  9 -> rangée du bas   (de droite à gauche)
     *   Index 10 à 19 -> colonne gauche   (de bas en haut)
     *   Index 20 à 29 -> rangée du haut   (de gauche à droite)
     *   Index 30 à 39 -> colonne droite   (de haut en bas)
     */
    private int[] indexVersGrid(int index) {
        if (index <= 9)  return new int[]{10, 10 - index};       // bas
        if (index <= 19) return new int[]{10 - (index - 10), 0}; // gauche
        if (index <= 29) return new int[]{0, index - 20};        // haut
        return new int[]{index - 30, 10};                         // droite
    }


    // =========================================================================
    //  PIONS (les cercles colorés qui représentent les joueurs)
    // =========================================================================

    /*
     * Crée un cercle coloré pour chaque joueur et le place sur la case 0 (Départ).
     */
    private void creerPions() {
        for (Joueur j : joueurs) {
            Circle pion = new Circle(8); // cercle de rayon 8px
            pion.setFill(Color.web(j.getCouleurPion())); // couleur choisie dans ConfigView
            pion.setStroke(Color.WHITE);  // contour blanc pour mieux le voir
            pion.setStrokeWidth(2);
            pions.put(j.getId(), pion);
            placerPionSurCaseUI(pion, 0); // tout le monde démarre à la case 0
        }
    }

    // Place un pion sur une case graphique
    private void placerPionSurCaseUI(Circle pion, int indexCase) {
        StackPane sp = casesUI.get(indexCase);
        if (sp != null && !sp.getChildren().contains(pion)) {
            sp.getChildren().add(pion);
        }
    }

    // Retire un pion d'une case graphique (avant de le déplacer)
    private void retirerPionDeCaseUI(Circle pion, int indexCase) {
        StackPane sp = casesUI.get(indexCase);
        if (sp != null) {
            sp.getChildren().remove(pion);
        }
    }


    // =========================================================================
    //  METHODES UTILITAIRES
    // =========================================================================

    /*
     * Quand un joueur achète une UE, on colorie légèrement le fond de la case
     * avec sa couleur de pion pour montrer que c'est la sienne.
     */
    private void mettreAJourCouleurCase(CaseUE ue) {
        StackPane sp = casesUI.get(ue.getIndex());
        if (sp != null) {
            sp.setStyle(
                "-fx-border-color:" + couleurGroupe(ue.getCouleur()) + ";" +
                "-fx-border-width:3;" +
                // le "33" à la fin = opacité 20% (fond très léger de la couleur du joueur)
                "-fx-background-color:" + ue.getProprietaire().getCouleurPion() + "33;"
            );
        }
    }

    // Cherche un joueur dans la liste par son id
    // Retourne null si le joueur n'est pas trouvé
    private Joueur trouverJoueur(int id) {
        return joueurs.stream()
                      .filter(j -> j.getId() == id)
                      .findFirst()
                      .orElse(null);
    }

    /*
     * Retourne la couleur hexadécimale correspondant à chaque groupe de filières.
     * Ces couleurs sont les mêmes que dans le CSS.
     */
    private String couleurGroupe(String groupe) {
        switch (groupe) {
            case "VIOLET": return "#8E44AD"; // TAL
            case "BLEU":   return "#2980B9"; // Informatique
            case "VERT":   return "#27AE60"; // Ergonomie
            case "ORANGE": return "#E67E22"; // Sciences Cognitives
            case "ROUGE":  return "#C0392B"; // Communication
            case "JAUNE":  return "#F1C40F"; // Langues
            default:       return "#95A5A6"; // gris par défaut
        }
    }




    /*
     * Appelée quand le joueur clique sur "Sauvegarder".
     * On enregistre l'état complet de la partie dans un fichier JSON
     * grâce à la bibliothèque GSON.
     */
    @FXML
    private void onSauvegarder() {
        TextInputDialog dialog = new TextInputDialog("partie_" + java.time.LocalDate.now());
        dialog.setTitle("Sauvegarder");
        dialog.setHeaderText("Nommer votre sauvegarde");
        dialog.setContentText("Nom :");
        dialog.showAndWait().ifPresent(nom -> {
            if (!nom.trim().isEmpty()){
                GestionnaireSauvegarde gs = new GestionnaireSauvegarde();
                gs.sauvegarder(joueurs, plateau, joueurActuelIndex, "partie_monopschool");
                ouvrirPopup("INFO", "Partie sauvegardee avec succes !");
            }
        });
        
    }

} // fin de la classe PlateauController