/**
* Cette classe représente une Configuration
* (Jour des croissants, nombre de personne affectés par jour)
*/
class ConfigAdmin{
    constructor(elem){
        /**
         * Jour des croissants
         */
        this.croissantDay = elem.croissantDay;

        /**
         * nombre de personne affectés par jour
         */
        this.nbPerDay = elem.nbPerDay;

    }
}