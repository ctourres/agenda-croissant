/**
* Cette classe représente un utilisateur (nom, prénom, mail, date, est actif ou non, a déja amené ou non)
*/
class User{
    constructor(elem){
        /**
         * Nom de l'utilisateur
         */
        this.name = elem.name;

        /**
         * Prénom de l'utilisateur
         */
        this.forename = elem.forename;

        /**
         * Adresse email de l'utilisateur
         */
        this.mail = elem.mail;

        /**
         * Date à laquelle l'utilisateur amène les croissants
         */
        if (elem.date && null !== elem.date) {
            this.date = new Date(elem.date);
        }
        else {
            this.date = null;
        }

        /**
         * Booléen true lorsque le membre de l'équipe utilise l'application. Passe à
         * false si il ne l'utilise plus
         */
        this.isActive = elem.isActive;

        /**
        * Boolean false tant que le membre de l'équipe n'a pas amené les crossants pour
        * la rotation de planning en cours. Devient true lorsque les croissants sont
        * amenés. Redevient false au début du nouveau planning.
        */
        this.didBringCroissants = elem.didBringCroissants;
    }
}