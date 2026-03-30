/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;

/**
 *
 * @author thailakeita
 */
public class CaseSpeciale extends Case {
    private int montantTaxe;
    
    public CaseSpeciale(int index , String nom , TypeCase type){
        super(index, nom, type);
        this.montantTaxe=0;
      
    }
    public CaseSpeciale(int index , String nom, TypeCase type, int montantTaxe){
        super(index , nom, type);
        this.montantTaxe = montantTaxe;
    
    }
    public int getMontantTaxe(){return montantTaxe;}
    
    // notifaction à afficher dans l'interface graphique
    
    public String appliquerEffet(Joueur joueur){
        switch(this.type){
            case Depart:
                joueur.ajouterEcts(200);
                return"🎓 Passage par le Départ ! +" + 200 + " ECTS,";
            case Allez_en_rattrapages : 
                joueur.setPositionActuelle(10);
                joueur.setEnPause(true);
                joueur.setToursPause(2);
            return"😰 Direction les Rattrapages ! Vous êtes bloqué(e) 2 tours.";
            
            case rattrapages : 
            return "🔒 Case Rattrapages — Vous êtes juste en visite... ou bloqué(e).";
            
            case Parking:
                return "🅿️ Parking de l'université — Rien ne se passe.";
            case Taxe:
                return "💸 Frais d'inscription annuels ! Vous payez " + montantTaxe + " ECTS.";
                
            default:
                
                return"";
                
               
        }
    }
}
