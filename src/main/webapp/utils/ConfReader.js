/**
 * @class de lecture d'un fichier de configuration générale pour l'application
 */
class ConfReader{
  /**
  * Constructeur de la classe ConfReader
  * @param path : chemin vers le fichier de configuration
  */
  constructor(path){
     this.path = path
  }

  /**
   * Génère un objet possèdant les propriétés de configuration à partir du fichier spécifié
   */
  getConfiguration() {
      const request = new XMLHttpRequest();
      request.open("GET", this.path, false);
      request.send(null);
      const text = request.responseText;
      return JSON.parse(text);
  }


}
