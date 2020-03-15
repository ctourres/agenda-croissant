/**
 * @class qui gère les appels au Back pour la gestion des utilisateurs
 */
class ConfigAdminService extends Service {
    /**
     * Constructeur de la classe UserService
     */
    constructor(idTeam) {
        super();
        this.prefixURL = `${idTeam}`;
        this.prefixURLAdmin = `${idTeam}/admin/config`;
    }

    /**
     * Fonction qui permet de récupérer la configuration
     *
     * @return La configuration
     */

    getConfig() {
      return this.request(this.http.GET, this.prefixURLAdmin)
      .then((Conf) => {
        return new ConfigAdmin(Conf);
      });
    }

    /**
     * Fonction qui permet d'éditer une config
     *
     * @return true ou false si OK ou KO
     */
     editConfig(editedConfig) {
         return this.request(this.http.PUT, this.prefixURLAdmin, editedConfig);
     }

}
