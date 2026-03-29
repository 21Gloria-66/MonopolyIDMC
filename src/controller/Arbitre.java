/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.util.List;
import modele.*;
import modele.Carte;
import modele.Case;
import modele.CaseAction;
import modele.CaseSpeciale;
import modele.CaseUE;
import modele.Joueur;
import modele.Plateau;
import java.util.List;
/**
 *
 * @author thailakeita
 */

public class Arbitre {

    private List<Joueur> joueurs;
    private Plateau plateau;
    private PlateauController ui;

    public Arbitre(List<Joueur> joueurs, Plateau plateau, PlateauController ui) {
        this.joueurs = joueurs;
        this.plateau = plateau;
        this.ui = ui;
    }

   
    public void appliquerEffetCase(Joueur joueur, Case caseAtteinte) {

        switch (caseAtteinte.getType()) {

            case UE:
                gererCaseUE(joueur, (CaseUE) caseAtteinte);
                break;

            case Action:
                gererCaseAction(joueur, (CaseAction) caseAtteinte);
                break;

            case Depart:
                joueur.ajouterEcts(200);
                ui.ouvrirPopup("INFO", "🎓 Passage par le Départ ! +200 ECTS !");
                break;

            case Allez_en_rattrapages:
                joueur.setPositionActuelle(10);
                joueur.setEnPause(true);
                joueur.setToursPause(2);
                ui.ouvrirPopup("INFO",
                    "😰 " + joueur.getPseudo() + " est envoyé(e) en Rattrapages !\n"
                    + "Bloqué(e) pendant 2 tours.");
                break;

            case rattrapages:
                if (joueur.isEnPause()) {
                    ui.ouvrirPopup("INFO",
                        "🔒 " + joueur.getPseudo() + " est en Rattrapages.\n"
                        + "Tours restants : " + joueur.getToursPause());
                } else {
                    ui.ouvrirPopup("INFO",
                        "👀 Simple passage par les Rattrapages. Bonne chance aux autres !");
                }
                break;

            case Parking:
                ui.ouvrirPopup("INFO", "🅿️ Parking de l'université. Reposez-vous !");
                break;

            case Taxe:
                CaseSpeciale taxe = (CaseSpeciale) caseAtteinte;
                String msg = taxe.appliquerEffet(joueur);
                ui.ouvrirPopup("INFO", msg);
                break;
        }

        // Vérifier faillite après chaque effet
        if (joueur.estEnfallite()) {
            ui.ouvrirPopup("FAILLITE",
                "💀 " + joueur.getPseudo() + " est en faillite !\n"
                + "Ses propriétés sont remises en vente.");
            mettreEnFaillite(joueur);
        }
    }

    private void gererCaseUE(Joueur joueur, CaseUE ue) {
        if (ue.estDisponible()) {
            if (joueur.getEcts() >= ue.getPrixAchat()) {
                ui.ouvrirPopup("ACHAT", ue);
            } else {
                ui.ouvrirPopup("INFO",
                    "🏛️ " + ue.getNom() + " est à vendre ("
                    + ue.getPrixAchat() + " ECTS) mais vous n'avez pas assez de fonds !");
            }
        } else if (ue.getProprietaire().equals(joueur)) {
            if (ue.estAméliorable() && joueur.getEcts() >= ue.getPrixAmelioration()) {
                ui.ouvrirPopup("AMELIORATION", ue);
            } else if (!ue.estAméliorable()) {
                ui.ouvrirPopup("INFO",
                    "✅ " + ue.getNom() + " est déjà au niveau maximum (M2) !");
            } else {
                ui.ouvrirPopup("INFO",
                    "📈 Vous possédez " + ue.getNom()
                    + ". Pas assez d'ECTS pour améliorer.");
            }
        } else {
            int loyer = ue.calculerLoyer();
            Joueur proprio = ue.getProprietaire();
            joueur.retirerEcts(loyer);
            proprio.ajouterEcts(loyer);
            ui.ouvrirPopup("LOYER",
                "💸 " + joueur.getPseudo() + " paye " + loyer + " ECTS\n"
                + "à " + proprio.getPseudo() + " pour " + ue.getNom());
        }
    }

    private void gererCaseAction(Joueur joueur, CaseAction caseAction) {
        Carte carte = caseAction.declencherEvenement(joueur);
        if (carte != null) {
            ui.ouvrirPopup("CARTE", carte);
        }
    }

    private void mettreEnFaillite(Joueur joueur) {
        for (CaseUE ue : joueur.getListeProprietes()) {
            ue.setProprietaire(null);
            ue.setNiveauAmelioration(0);
        }
        joueur.getListeProprietes().clear();
        joueurs.remove(joueur);
    }
}