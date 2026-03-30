/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;

/**
 *
 * @author glori
 */
public abstract class CaseAchetable extends Case{
    
    protected int prixAchat;
    protected Joueur proprietaire;
    
    public  CaseAchetable(int index, String nom, TypeCase type, int prixAchat){
    super(index, nom, type);
    this.prixAchat = prixAchat;
    this.proprietaire = null;
}
    public int getPrixAchat(){
        return prixAchat;
    }
    public void setPrixAchat(int prix){
        this.prixAchat = prix;
    }
    public Joueur getProprietaire(){
        return proprietaire;
    }
    public void setProprietaire(Joueur proprietaire){
        this.proprietaire = proprietaire;
    }
    public boolean estDisponible(){
        return proprietaire == null;
    }
}
