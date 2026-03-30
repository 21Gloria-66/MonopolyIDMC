/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;

/**
 *
 * @author thailakeita
 */
public class CaseUE extends Case{
    // les attributs
    private int loyerBase;
    private int prixAchat;
    private int prixAmelioration;
    private String couleur;// les groupes de couleur
    private int niveauAmelioration;
    private Joueur proprietaire;
    
    // Labels des niveaux pour l'affichage 
    
    public static final String[] LABELS_NIVEAU={ "L1","L2","M1","M2"};
    
    public CaseUE(int index , String nom , int loyerBase, int prixAchat, int prixAmelioration,String couleur){
        super(index , nom , TypeCase.UE);
        this.loyerBase=loyerBase;
        this.prixAchat= prixAchat;
        this.couleur= couleur;
        this.niveauAmelioration=0;
        this.proprietaire=null;
        
    }
    
    // calcule le loyer à payer selon : si un joueur possède au moins 2 carte de la meme famille lelprix du loyer des deux case augmente et si le joueur possède toute une famille complète soit des cases du meme couelur alors il peut l'améliorer 
    
    public int calculerLoyer(){
        if ( proprietaire == null) return 0;
        
        long nbCouleur = proprietaire.getListeProprietes().stream().filter(ue-> ue.getCouleur().equals(this.couleur)).count();
         
        return loyerBase *(int) nbCouleur *(niveauAmelioration +1);
       
        
    }
    
    public boolean ameliorer(){
        if(niveauAmelioration < 3){
            niveauAmelioration++;
            return true;
            
        }
        return false;
    }
    
    public boolean estAméliorable(){
        return niveauAmelioration<3 && proprietaire != null;
    }
    
    public boolean estDisponible(){
        return proprietaire == null;
    }
    
    // les getters et les setters 
    
    public int getLoyerBase(){ return loyerBase;}
    public void setLoyerBase(int loyerBase){this.loyerBase=loyerBase;}
    
    public int getPrixAchat(){return prixAchat;}
    public void setPrixAchat(int prixAchat){this.prixAchat=prixAchat;}
    
    public int getPrixAmelioration(){ return prixAmelioration;}
    public void setPrixAmelioration(int prixAmelioration){this.prixAmelioration=prixAmelioration;}
    
    public String getCouleur(){return couleur;}
    public void setCouleur(String couleur){this.couleur=couleur;}
    
    public int getNiveauAmelioration(){return niveauAmelioration;}
    public void setNiveauAmelioration(int niveauAmelioration){this. niveauAmelioration = niveauAmelioration;}
    
    
    public Joueur getProprietaire(){return proprietaire;}
    public void setProprietaire ( Joueur proprietaire){ this.proprietaire = proprietaire;}
    
}
