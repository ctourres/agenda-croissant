package croissants.services;

import croissants.business.Config;
import croissants.business.Team;

/**
 * Interface du service de gestion des config.
 */
public interface IAdminService {

    /**
     * Méthode qui renvoie l'équipe identifiée par idTeam.
     *
     * @param idTeam         Identifiant de l'équipe concernée
     * @return L'équipe demandée ou null si l'équipe n'est pas trouvée
     */
    Team getTeam(String idTeam);

    /**
     * Méthode qui modifie les config de livraison d'une team dont le jour et le nombre de personne sont passés en paramètre
     * dans l'équipe identifiée par idTeam.
     *
     * @param idTeam         Identifiant de l'équipe concernée
     * @param config String JSON représentant la configuartion de l'équipe.
     * @return un Boolean, true si l'utilisateur a bien été modifié, false sinon
     */
    boolean editConfig(String idTeam, Config config);

    /**
     * Méthode qui reccupère la config d'une team
     * dans l'équipe identifiée par idTeam.
     *
     * @param idTeam         Identifiant de l'équipe concerné;
     * @return une config
     */
    Config getConfig(String idTeam);
}
