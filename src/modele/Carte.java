/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;

/**
 *
 * @author thailakeita
 */
public class Carte {
    public enum TypeCarte{
        Bonus_ects,// le joueur gagne des Ects
        Malus_ects,// le joueur perd des Ects 
        Deplacement_force // en donction du résulat de la carte le joueur est déplacer ou no
    }
    private String description;
    private TypeCarte type;
    private int valeur;
    
    public Carte(String description,TypeCarte type , int valeur){
        this.description= description;
        this.type=type;
        this.valeur=valeur;
    }
    
    public String getDescription(){ return description;}
    public TypeCarte getType(){return type;}
    public int getValeur(){return valeur;}
    
@Override 
public String toString(){return description;}
}
