/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;
import java.util.List;
/**
 *
 * @author thailakeita
 */

public class EtatPartie {

    private List<Joueur> joueurs;
    private int indexJoueurActuel;
    private List<EtatCase> etasCases;
    private String dateCreation;
    private String nomPartie;

    public EtatPartie() {}

    public EtatPartie(List<Joueur> joueurs, int indexJoueurActuel,
                      List<EtatCase> etasCases, String nomPartie) {
        this.joueurs = joueurs;
        this.indexJoueurActuel = indexJoueurActuel;
        this.etasCases = etasCases;
        this.nomPartie = nomPartie;
        this.dateCreation = java.time.LocalDateTime.now().toString();
    }

    public List<Joueur> getJoueurs() { return joueurs; }
    public void setJoueurs(List<Joueur> joueurs) { this.joueurs = joueurs; }

    public int getIndexJoueurActuel() { return indexJoueurActuel; }
    public void setIndexJoueurActuel(int i) { this.indexJoueurActuel = i; }

    public List<EtatCase> getEtasCases() { return etasCases; }
    public void setEtasCases(List<EtatCase> etasCases) { this.etasCases = etasCases; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String d) { this.dateCreation = d; }

    public String getNomPartie() { return nomPartie; }
    public void setNomPartie(String nomPartie) { this.nomPartie = nomPartie; }

    public static class EtatCase {

        private int indexCase;
        private int idProprietaire;
        private int niveauAmelioration;

        public EtatCase() {}

        public EtatCase(int indexCase, int idProprietaire, int niveauAmelioration) {
            this.indexCase = indexCase;
            this.idProprietaire = idProprietaire;
            this.niveauAmelioration = niveauAmelioration;
        }

        public int getIndexCase() { return indexCase; }
        public void setIndexCase(int indexCase) { this.indexCase = indexCase; }

        public int getIdProprietaire() { return idProprietaire; }
        public void setIdProprietaire(int id) { this.idProprietaire = id; }

        public int getNiveauAmelioration() { return niveauAmelioration; }
        public void setNiveauAmelioration(int n) { this.niveauAmelioration = n; }
    }
}