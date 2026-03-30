/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;
import modele.*;
 


/**
 *
 * @author thailakeita
 */
// Chargement de la partie depuis un fichier GSON et re constitue l'état complet
//@param plateau ( plateau à mettre à jour)
//@param nomFichier
//@param joueur
//@param indexJoueurActuel


public class GestionnaireSauvegarde {

    private static final String DOSSIER = 
            System.getProperty("user.home")+ "/Documents/MonopSchool/sauvegardes/";
    
    private final Gson gson;

    public GestionnaireSauvegarde() {
        this.gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try {
            Files.createDirectories(Paths.get(DOSSIER));
        } catch (IOException e) {
            System.err.println("Erreur creation dossier : " + e.getMessage());
        }
    }

    public void sauvegarder(List<Joueur> joueurs, Plateau plateau,
                            int indexJoueurActuel, String nomFichier) {

        List<EtatPartie.EtatCase> etasCases = new ArrayList<>();

        for (Case c : plateau.getCases()) {
            if (c instanceof CaseUE) {
                CaseUE ue = (CaseUE) c;
                int idProprio = (ue.getProprietaire() != null)
                        ? ue.getProprietaire().getId() : -1;
                etasCases.add(new EtatPartie.EtatCase(
                        c.getIndex(), idProprio, ue.getNiveauAmelioration()));
            }
        }

        EtatPartie etat = new EtatPartie(joueurs, indexJoueurActuel, etasCases, nomFichier);
        String chemin = DOSSIER + nomFichier + ".json";

        try (Writer writer = new FileWriter(chemin)) {
            gson.toJson(etat, writer);
            System.out.println("Sauvegarde OK : " + chemin);
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde : " + e.getMessage());
        }
    }

    public EtatPartie charger(String nomFichier, Plateau plateau) {
        String chemin = DOSSIER + nomFichier + ".json";
        try (Reader reader = new FileReader(chemin)) {
            EtatPartie etat = gson.fromJson(reader, EtatPartie.class);

            if (etat.getEtasCases() != null) {
                for (EtatPartie.EtatCase ec : etat.getEtasCases()) {
                    Case c = plateau.getcase(ec.getIndexCase());
                    if (c instanceof CaseUE) {
                        CaseUE ue = (CaseUE) c;
                        ue.setNiveauAmelioration(ec.getNiveauAmelioration());
                        if (ec.getIdProprietaire() >= 0 && etat.getJoueurs() != null) {
                            etat.getJoueurs().stream()
                                .filter(j -> j.getId() == ec.getIdProprietaire())
                                .findFirst()
                                .ifPresent(j -> {
                                    ue.setProprietaire(j);
                                    j.ajouterPropriete(ue);
                                });
                        }
                    }
                }
            }
            return etat;

        } catch (IOException e) {
            System.err.println("Fichier introuvable : " + chemin);
            return null;
        }
    }

    public List<String> listerSauvegardes() {
        List<String> noms = new ArrayList<>();
        File dossier = new File(DOSSIER);
        File[] fichiers = dossier.listFiles((d, n) -> n.endsWith(".json"));
        if (fichiers != null) {
            for (File f : fichiers) {
                noms.add(f.getName().replace(".json", ""));
            }
        }
        return noms;
    }
}