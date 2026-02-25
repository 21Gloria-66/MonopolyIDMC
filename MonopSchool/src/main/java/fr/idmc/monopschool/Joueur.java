/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.idmc.monopschool;

/**
 *
 * @author thailakeita
 */
public class Joueur {

    // Attributs définis dans l'UML ( classes)
    private String nom;
    private int soldeECTS;
    private int position;

    // Constructeur 
    public Joueur(String nom) {
        this.nom = nom;
        this.soldeECTS = 1500; // capital initial
        this.position = 0;     // case de départ
    }

    // Méthodes UML 

    // Lancer un dé (1 à 6)
    public int lancerDes() {
        return (int)(Math.random() * 6) + 1;
    }

    // Payer un montant
    public void payer(int montant) {
        this.soldeECTS -= montant;
    }

    // Encaisser un loyer
    public void encaisserLoyer(int montant) {
        this.soldeECTS += montant;
    }

    // Déplacement sur un plateau de 24 cases
    public void seDeplacer(int nbCases) {
        this.position = (this.position + nbCases) % 24;
    }

    //  Getters 
    public String getNom() {
        return nom;
    }

    public int getSoldeECTS() {
        return soldeECTS;
    }

    public int getPosition() {
        return position;
    }

    // 
    @Override// redéfinition, cette partie de code va nous permettre d'inspecter que tout fonctionne correctemen
    public String toString() {
        return "Joueur{" +
                "nom='" + nom + '\'' +
                ", soldeECTS=" + soldeECTS +
                ", position=" + position +
                '}';
    }
}
