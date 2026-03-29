/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author thailakeita
 */
public class CaseAction extends Case {
    private  List<Carte> deck;
    private int indexCourant;
    
    public CaseAction(int index ,String nom){
        super(index,nom,TypeCase.Action);
        this.deck=new ArrayList<>();
        this.indexCourant=0;
        initialiserDeck();
        Collections.shuffle(deck);
    }
    public Carte declencherEvenement(Joueur j){
        if(deck.isEmpty()) return null;
        Carte carte = deck.get(indexCourant% deck.size());
        indexCourant++;
        
        switch(carte.getType()){
            case Bonus_ects:
                j.ajouterEcts(carte.getValeur());
                break;
            case Malus_ects:
                j.retirerEcts(carte.getValeur());
                break;
            case Deplacement_force:
               j.setPositionActuelle(carte.getValeur());
               break;
        }
        return carte;       
        }
    
    private void initialiserDeck(){
        // Bonus ECTS
         deck.add(new Carte(
            "🎓 Vous avez décroché la bourse CROUS ! Recevez 150 ECTS.",
            Carte.TypeCarte.Bonus_ects, 150));
        deck.add(new Carte(
            "🏆 Meilleur exposé du séminaire TAL ! Bonus de 100 ECTS.",
            Carte.TypeCarte.Bonus_ects, 100));
        deck.add(new Carte(
            "🎉 Le Gala de l'IDMC a été un succès. Vous touchez 80 ECTS.",
            Carte.TypeCarte.Bonus_ects, 80));      
        deck.add(new Carte(
                "📚 Votre mémoire de M2 est noté 18/20. Recevez 200 ECTS.",
            Carte.TypeCarte.Bonus_ects, 200));
        deck.add(new Carte(
                "💻 Votre projet Algorithmique a impressionné le jury. +120 ECTS.",
            Carte.TypeCarte.Bonus_ects, 120));
        deck.add(new Carte(
                "🧠 Excellent résultat en Cognition ! Vous gagnez 90 ECTS.",
            Carte.TypeCarte.Bonus_ects, 90));
        deck.add(new Carte(
                "🌍 Stage Erasmus validé avec mention. Recevez 180 ECTS.",
                Carte.TypeCarte.Bonus_ects, 180));
        
        // Malus ects
        
        deck.add(new Carte(
               "😱 Rattrapages en Ergo ! Vous devez payer 100 ECTS de frais.",
            Carte.TypeCarte.Malus_ects, 100));
               
        deck.add(new Carte(
                
              "📝 Vous avez raté la date limite de rendu. Amende : 75 ECTS.",
            Carte.TypeCarte.Malus_ects, 75));
        
        deck.add(new Carte(
                 "☕ Trop de cafés à la BU... Perdez 50 ECTS.",
            Carte.TypeCarte.Malus_ects, 50));
        
        deck.add(new Carte(
              "🖨️ L'imprimante de l'IDMC tombe en panne avant votre soutenance. -80 ECTS.",
            Carte.TypeCarte.Malus_ects, 80));  
                
        deck.add(new Carte(
                "📉 Absence injustifiée au cours de TAL. Retrait : 60 ECTS.",
            Carte.TypeCarte.Malus_ects, 60));
        deck.add(new Carte(
            "🚌 Bus manqué, arrivée en retard à l'examen. Pénalité : 40 ECTS.",
            Carte.TypeCarte.Malus_ects, 40));
        
                
     // Déplacement forcés
     deck.add(new Carte(
            "📬 Convocation d'urgence au bureau de la scolarité ! Allez case 10.",
            Carte.TypeCarte.Deplacement_force, 10));
        deck.add(new Carte(
            "🔁 Retour à la case Départ pour refaire l'inscription.",
            Carte.TypeCarte.Deplacement_force, 0));
        deck.add(new Carte(
            "🎤 Le conférencier en amphi 3 vous attend ! Déplacez-vous case 25.",
            Carte.TypeCarte.Deplacement_force, 25));
     
                    
}
    
public List<Carte> getDeck(){return deck;}

}
