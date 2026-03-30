/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;
//Construction d'une case avec ses propriétés de base
//@param index ( position de la case sur le plateau (0 à 39)
//@param nom ( nom affiché sur la case 
//@param type ( type de case (UE,Action,Taxe etc ..)
//
/**
 *
 * @author thailakeita
 */
public abstract class  Case {
    public enum TypeCase{
        UE, // unité d'enseignement achetable 
        Action,// concrtiser avec des pop up ( bonus ou malus )
        Depart, // chaque passage ( collecte 200 ECTS )
        rattrapages,
        Allez_en_rattrapages,// envoie directement en rattrapages
        Parking, // case neutre
        Taxe // take fixe
        
    }
    
    protected int index;
    protected String nom;
    protected TypeCase type;
    
    public Case(int index , String nom, TypeCase type){
        this.index = index;
        this.nom= nom;
        this.type=type;
        
    }
    
    public int getIndex(){return index;}
    public String getNom(){return nom;}
    public TypeCase getType(){return type;}
    
    @Override 
    public String toString(){return"[" + index + "] " + nom + " (" + type + ")";}
}
