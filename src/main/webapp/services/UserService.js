/**
 * @class qui gère les appels au Back pour la gestion des utilisateurs
 */
class UserService extends Service {
    /**
     * Constructeur de la classe UserService
     */
    constructor(idTeam) {
        super();
        this.prefixURL = `${idTeam}/users`;
        this.prefixURLGeneRandPlan = `${idTeam}/planningGeneration`;
    }

    /**
     * Fonction qui permet de récupérer l'ensemble des utlisateurs
     *
     * @return La liste des utilisateurs
     */
    getAll() {
      return this.request(this.http.GET, this.prefixURL)
      .then((res) => {
          return res.members.map((elem) => new User(elem));
      });
    }

    /**
     * Fonction qui permet d'éditer un utilisateur
     *
     * @return true ou false si OK ou KO
     */
     editUser(editedUser) {
         return this.request(this.http.PUT, this.prefixURL + "/" + editedUser.mail, editedUser)
     }

    /**
     * Fonction qui permet d'ajouter un utilisateur
     *
     * @return true ou false si OK ou KO
     */
    addUser(user) {
        return this.request(this.http.POST, this.prefixURL, user);
    }

    /**
     * Fonction qui permet de supprimer un utilisateur
     *
     * @return Un booléen indiquant si la suppression est effective
     */
    delUser(user) {
      return this.request(this.http.DELETE, this.prefixURL + "/" + user.mail);
    }
    /**
    * Fonction qui permet la génération aléatoire de dates attribuées à chaque utilisateur
    *
    * @return Un booléen indiquant la bonne écriture du nouveau planning dans la base de données
    */
    generateRandomPlanning(){
      return this.request(this.http.GET, this.prefixURLGeneRandPlan);
    }
}
