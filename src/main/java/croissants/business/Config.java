package croissants.business;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    /**
     * Représente le jour de la semaine quand les croissants sont à
     * livrer 0 : Sunday 1 : Monday 2 : Tuesday 3 : Wednesday 4 : Thursday 5 :
     * Friday 6 : Saturday
     */

    private int croissantDay = 5;

    /**
     * Représente le nombre de personne affectée par jour.
     */
    private int nbPerDay = 1;

    /**
     * Récupération du jour de croissant de l'équipe
     *
     * @return Le croissantDay de l'équipe
     */
    public int getCroissantDay() {
        return croissantDay;
    }

    /**
     * Mutateur du jour de croissant de l'équipe
     *
     * @param croissantDay croissantDay de l'équipe
     */
    public void setCroissantDay(int croissantDay) {
        this.croissantDay = croissantDay;
    }


    /**
     * Récupération du nombre de personne a affecter pour le jour
     *
     * @return Le nbPerDay de l'équipe
     */
    public int getNbPerDay() {return nbPerDay;}

    /**
     * Mutateur du nombre de personne a affecter pour le jour
     *
     * @param nbPersonneCroissant le nombre de personne a affecter
     */
    public void setNbPerDay(int nbPersonneCroissant) {this.nbPerDay = nbPersonneCroissant;}

    /**
     * Méthode toString
     *
     * @return une représentation textuelle de l'objet config
     */
    @Override
    public String toString() {
        return "Config{" +
                "croissantDay=" + croissantDay +
                ", nbPerDay=" + nbPerDay +
                '}';
    }
}
