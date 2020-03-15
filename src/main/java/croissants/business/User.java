package croissants.business;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Cette classe représente un objet User en fonction de son id, nom, prenom et
 * mail
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    /**
     * Nom du membre de l'équipe
     */
    private String name;

    /**
     * Prénom du membre de l'équipe
     */
    private String forename;

    /**
     * Adresse mail du membre de l'équipe
     */
    private String mail;

    /**
     * Prochaine date à laquelle le membre doit amener les croissants
     */
    private Date date;

    /**
     * Boolean true lorsque le membre de l'équipe est encore en activité
     * dans l'équipe.
     */
    private Boolean isActive;

    /**
     * A REVOIR EN CONCEPTION : (pas utile ?)
     * <p>
     * Boolean false tant que le membre de l'équipe n'a pas amené les
     * crossants pour la rotation de planning en cours. Devient true
     * lorsque les croissants sont amenés.
     * Redevient false au début du nouveau planning.
     */
    private Boolean didBringCroissants;

    /**
     * Accesseur de l'attribut name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Mutateur de l'attribut name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Accesseur de l'attribut forename
     *
     * @return forename
     */
    public String getForename() {
        return forename;
    }

    /**
     * Mutateur de l'attribut forename
     *
     * @param forename
     */
    public void setForename(String forename) {
        this.forename = forename;
    }

    /**
     * Accesseur de l'attribut mail
     *
     * @return mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * Mutateur de l'attribut mail
     *
     * @param mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Accesseur de l'attribut date
     *
     * @return date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Mutateur de l'attribut date
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Accesseur de l'attribut isActive
     *
     * @return isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * Mutateur de l'attribut isActive
     *
     * @param isActive
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Accesseur de l'attribut didBringCroissant
     *
     * @return didBringCroissants
     */
    public Boolean getDidBringCroissants() {
        return didBringCroissants;
    }

    /**
     * Mutateur de l'attribut didBringCroissant
     *
     * @param didBringCroissants
     */
    public void setDidBringCroissants(Boolean didBringCroissants) {
        this.didBringCroissants = didBringCroissants;
    }

    /**
     * Méthode toString
     *
     * @return une représentation textuelle de l'objet user
     */
    @Override
    public String toString() {
        return "User [name=" + name + ", forename=" + forename + ", mail=" + mail + ", Date=" + date + ", isActive="
                + isActive + ", didBringCroissants=" + didBringCroissants + "]";

    }
}