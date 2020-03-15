/**
 * @class parent à tous les services, gère la configuration des appels au Back
 */
class Service {
    /**
     * Constructeur de la classe Service
     */
    constructor() {
        /**
         * gestion de la configuration générale
         */
        this.config = CroissantConfig;

        /**
         * Domaine sur lequel l'interface et les API sont accessibles
         */
        this.host = this.config["host"];
    }

    /**
     * Affiche le composant Alert avec les informations en paramètres
     * @param info informations à afficher
     * @param info.code code de la réponse
     * @param info.message message de la réponse
     */
    log(info) {
        const alertComponent = app.$root.$refs.alert;
        if (alertComponent) {
            alertComponent.show(info.code, info.message);
        }
    }

    /**
     * Effectue une requête HTTP
     * @param method méthode HTTP de la requête
     * @param url URL de la requête
     * @param data données envoyées dans le corps de la requête
     * @param options options supplémentaires ajoutées à la requête
     */
    request(method, url, data, options) {
        return new Promise((resolve, reject) => {
            const scope = this;
            const request = {
                url: `${this.host}/${url}`,
                type: method,
                success(res) {
                    // Génère un message de succès selon les cas
                    let message;
                    if (res) {
                        if (typeof res === 'string') {
                            message = res;
                        } else if (res.message) {
                            message = res.message;
                        }
                    }
                    if (message) {
                        const result = { code: '200', message: message };
                        // Affiche le message
                        scope.log(result);
                    }
                    resolve(res);
                },
                error(xhr) {
                    // Génère et affiche le message d'erreur
                    const result = { code: xhr.status, message: xhr.responseText };
                    scope.log(result);
                    reject(xhr);
                }
            };

            // Définit les options de la requête HTTP selon la méthode
            switch (method) {
                case this.http.GET:
                    request.dataType = 'json';
                    break;
                case this.http.POST:
                case this.http.PUT:
                case this.http.PATCH:
                case this.http.DELETE:
                    if (data) {
                        request.contentType = 'application/json; charset=utf-8';
                        request.data = JSON.stringify(data);
                    }
                    break;
                default:
                    console.error(`La méthode ${method} n'est pas supportée.`);
                    reject();
            }

            // Ajoute les options spécifiées à la requête
            if (options) {
                Object.keys(options).forEach((key) => {
                    request[key] = options[key];
                });
            }

            // Exécute la requête
            $.ajax(request);
        });
    }

    /**
     * Enumération des méthodes HTTP gérées
     */
    get http() {
        return {
            GET: 'GET',
            POST: 'POST',
            PUT: 'PUT',
            PATCH: 'PATCH',
            DELETE: 'DELETE'
        }
    }
}
