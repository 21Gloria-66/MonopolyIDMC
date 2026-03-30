/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;
import java.util.ArrayList;
import java.util.List;
import modele.Case;
import static modele.Case.TypeCase.Allez_en_rattrapages;
import modele.CaseAction;
import modele.CaseSpeciale;
import modele.CaseUE;

// Groupes de couelurs fillières :
// Violet : TAL , BLEU: INFROMATIQUE/ALGORITHMIQUE ; VERT : ERGONOMIE COGNITIVE, ORANGE: SCIENCES COGNITIVES, ROUGE : cOMMUNICATION & MEDITATION ; JAUNE : Langues & traduction


/**
 *
 * @author thailakeita
 */
public class Plateau {
    private List<Case> cases;
    private static final int nb_cases=40;
    
    public Plateau(){
        cases= new ArrayList<>();
        initialiserPlateau();
    }
    
    private void initialiserPlateau(){
        // le bas de notre plateau de la gauche vers la droite 
        cases.add(new CaseSpeciale(0,  "🎓 Départ IDMC", Case.TypeCase.Depart));
        cases.add(new CaseUE(1,  "TAL Fondamentaux",       60,  60,  50, "VIOLET"));
        cases.add(new CaseAction(2,  "📬 Courrier de la Scolarité"));
        cases.add(new CaseUE(3,  "TAL Avancé",             60,  60,  50, "VIOLET"));
        cases.add(new CaseSpeciale(4,  "💸 Frais d'inscription",  Case.TypeCase.Taxe, 200));
        cases.add(new CaseUE(5,  "Algorithmique L1",       100, 200, 100, "BLEU"));
        cases.add(new CaseAction(6,  "📬 Courrier de la Scolarité"));
        cases.add(new CaseUE(7,  "Structures de Données",  100, 200, 100, "BLEU"));
        cases.add(new CaseUE(8,  "Bases de Données",       120, 200, 100, "BLEU"));
        cases.add(new CaseUE(9,  "Programmation Objet",    120, 240, 120, "BLEU"));
        
        // le coté gauche du haute vers le bas ( 10 à 19)
        
        cases.add(new CaseSpeciale(10, "🔒 Rattrapages", Case.TypeCase.rattrapages));
        cases.add(new CaseUE(11, "Ergonomie Cognitive L1",  140, 260, 130, "VERT"));
        cases.add(new CaseUE(12, "Interfaces Homme-Machine",140, 260, 130, "VERT"));
        cases.add(new CaseAction(13, "🎓 Conseil de l'UFR"));
        cases.add(new CaseUE(14, "Ergonomie des Systèmes", 160, 300, 150, "VERT"));
        cases.add(new CaseUE(15, "Psychologie Cognitive",  140, 280, 140, "ORANGE"));
        cases.add(new CaseAction(16, "🎓 Conseil de l'UFR"));
        cases.add(new CaseUE(17, "Neurosciences Cognitives",140, 280, 140, "ORANGE"));
        cases.add(new CaseUE(18, "Cognition & Langage",    160, 300, 150, "ORANGE"));
        cases.add(new CaseUE(19, "Sciences de l'Éducation",180, 320, 160, "ORANGE"));
        
        
        
        // coté haut du plateau DE LA DROITE VERS LA GAUCHE
        cases.add(new CaseSpeciale(20, "🅿️ Parking BU", Case.TypeCase.Parking));
        cases.add(new CaseUE(21, "Communication Orale",    180, 350, 175, "ROUGE"));
        cases.add(new CaseUE(22, "Médiation Culturelle",   180, 350, 175, "ROUGE"));
        cases.add(new CaseAction(23, "🎲 Hasard IDMC"));
        cases.add(new CaseUE(24, "Écriture Professionnelle",200, 380, 190, "ROUGE"));
        cases.add(new CaseUE(25, "Linguistique Générale",  200, 350, 175, "JAUNE"));
        cases.add(new CaseAction(26, "🎲 Hasard IDMC"));
        cases.add(new CaseUE(27, "Traductologie",          220, 380, 190, "JAUNE"));
        cases.add(new CaseUE(28, "Terminologie",           220, 380, 190, "JAUNE"));
        cases.add(new CaseSpeciale(29, "💸 Droits de scolarité M2", Case.TypeCase.Taxe, 100));
        
        
  
        // coté droit du bas vers le haut (index 30 à 39)
        
        cases.add(new CaseSpeciale(30, "😰 Allez en Rattrapages !",     Case.TypeCase.Allez_en_rattrapages));
        cases.add(new CaseUE(31, "Interprétation",  220, 400, 200, "JAUNE"));
        cases.add(new CaseUE(32, "NLP & IA Linguistique",  240, 400, 200, "VIOLET"));
        cases.add(new CaseAction(33, "📬 Courrier de la Scolarité"));
        cases.add(new CaseUE(34, "Ontologies & Web Sémantique",240,400,200,"VIOLET"));
        cases.add(new CaseUE(35, "Annotation Linguistique",260, 440, 220, "VIOLET"));
        cases.add(new CaseAction(36, "🎓 Conseil de l'UFR"));
        cases.add(new CaseUE(37, "Projet Intégré M1", 260, 440, 220, "ROUGE"));
        cases.add(new CaseUE(38, "Mémoire de Recherche", 300, 500, 250, "ROUGE"));
        cases.add(new CaseUE(39, "Soutenance M2", 300, 500, 250, "ROUGE"));
    }
    

public Case getcase(int index){
    return cases.get(index % nb_cases);
}

public List<Case> getCases(){ return cases;}

public int getNbcases(){ return nb_cases;}

}
   
