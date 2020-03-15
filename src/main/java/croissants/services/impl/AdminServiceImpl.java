package croissants.services.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import croissants.business.Config;
import croissants.business.Team;
import croissants.controller.UserController;
import croissants.dao.DataStore;
import croissants.exceptions.CroissantException;
import croissants.services.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service de gestion des config
 * Classe faisant le lien entre le RestController et la DAO
 */
@Service
public class AdminServiceImpl implements IAdminService {


    /**
     * Datastore
     */
    @Autowired
    private DataStore dataStore;

    /**
     * Logger de la classe UserController
     */
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    /**
     * Message d'erreur d'accès au fichier
     */
    private static final String ERREUR_D_ACCES_AU_FICHIER_MSG = "Erreur d'accès au fichier";


    /**
     * Méthode qui renvoie l'équipe identifiée par idTeam.
     *
     * @param idTeam Identifiant de l'équipe concernée
     * @return L'équipe demandée ou null si l'équipe n'est pas trouvée
     */
    @Override
    public Team getTeam(String idTeam) {
        Team team = null;
        try {
            team = dataStore.getTeam(idTeam);
        } catch (CroissantException exception) {
            LOGGER.debug(ERREUR_D_ACCES_AU_FICHIER_MSG);
            LOGGER.debug(exception);
        }
        return team;
    }

    /**
     * Méthode qui réccupère la configuration d'une team identifiée par idTeam.
     *
     * @param idTeam Identifiant de l'équipe concernée
     * @return la configuration
     */
    @Override
    public Config getConfig(String idTeam){
        Team team = getTeam(idTeam);
        return team.getConfig();
    }

    /**
     * Méthode qui modifie la config de livraison d'une team dont la config est passée en paramètre
     * dans l'équipe identifiée par idTeam.
     *
     * @param idTeam Identifiant de l'équipe concernée
     * @param config String JSON représentant la configuration de l'équipe.
     * @return un Boolean, true si la config a bien été modifiée, false sinon
     */
    @Override
    public boolean editConfig(String idTeam, Config config) {
        Team team = getTeam(idTeam);
        if (null != team) {
            team.setConfig(config);
            try {
                dataStore.saveTeam(team);
            } catch (CroissantException e) {
                LOGGER.debug(ERREUR_D_ACCES_AU_FICHIER_MSG);
                LOGGER.debug(e);
            }
            return true;
        }
        return false;
    }
}
