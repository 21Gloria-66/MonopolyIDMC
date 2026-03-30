/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;
import java.util.ArrayList;
import java.util.List;
/**
 *@param montant ( montant à retirer)
 *@return( true si le retrait à reussi, false si solde insuffisant ( fallite)
 * @author thailakeita
 */
public class Joueur {
    
    // les attributs 
    
    private int id;
    private String pseudo;
    private String couleurPion;
    private int ects;
    private int positionActuelle;
    private List<CaseUE> listeProprietes;
    private boolean enPause;// permet de bloquer le joueur ( quand il est en rattrapage ) 
    private int toursPause;// si jamais il est bloqué , le nombre de tpurs restants si bloqué
    
    // les methodes 
    public Joueur ( int id , String pseudo, String couleurPion){
        this.id=id;
        this.pseudo=pseudo;
        this.couleurPion=couleurPion;
        this.ects= 1500;// ceci sera le capital de départ 
        this.positionActuelle=0;
        this.listeProprietes= new ArrayList<>();
        this.enPause=false;
        this.toursPause=0;
    }
    // getters & Setters 
    
    public int getId() { return id;}
    
    public String getPseudo() { return pseudo;}
    public void setPseudo(String pseudo){ this.pseudo=pseudo;}
    
    public String getCouleurPion(){ return couleurPion;}
    public void setCouleurPion(String couleurPion){this.couleurPion= couleurPion;}
    
    public int getEcts(){return ects;}
    public void setEcts(int ects){ this.ects = ects;}
    public void ajouterEcts(int montant){this.ects+= montant;}
    // Calcule le patrimoine total joueur.
    //@return solde ECTS + somme des prix d'achat de toutes les UE possédées
    public boolean retirerEcts(int montant){
        if(this.ects>= montant){
            this.ects-= montant;
            return true;

        }
        return false; // ce qui signifie fallite
        
    }
    
public int getPositionActuelle (){ return positionActuelle;}

public void setPositionActuelle ( int positionActuelle){this.positionActuelle=positionActuelle;}

public List<CaseUE> getListeProprietes(){return listeProprietes;}

public void ajouterPropriete(CaseUE ue){this.listeProprietes.add(ue);}

public void retirerPropriete(CaseUE ue){this.listeProprietes.remove(ue);}

public boolean isEnPause(){ return enPause;}

public void setEnPause(boolean enPause){ this.enPause=enPause;}

public int getToursPause(){return toursPause;}

public void setToursPause(int toursPause){this.toursPause= toursPause;}

public void decrementerPause(){
    if ( toursPause>0) toursPause--;
    if(toursPause == 0) enPause = false;
    
}
public boolean estEnfallite(){return ects < 0;}

@Override
public String toString(){
    return pseudo +"("+ ects + " ECTS)";
    
}

}

    

