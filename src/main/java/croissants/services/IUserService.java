package croissants.services;

import croissants.business.Team;

/**
 * Interface du service de gestion des utilisateur.
 */
public interface IUserService {

    /**
     * Méthode qui renvoie tous les utilisateurs d'une équipe identifiée par idTeam.
     *
     * @param idTeam         Identifiant de l'équipe concernée
     * @return L'équipe demandée ou null si l'équipe n'est pas trouvée
     */
    Team getUsersByTeam(String idTeam);

    /**
     * Méthode qui ajoute un utilisateur dans l'équipe identifiée par idTeam
     *
     * @param idTeam         Identifiant de l'équipe concernée
     * @param userJSONString String JSON représentant un utilisateur à ajouter
     * @return un Boolean, true si l'utilisateur a bien été ajouté, false sinon
     */
    boolean addUserInTeam(String idTeam, String userJSONString);

    /**
     * Méthode qui modifie les informations d'un utilisateur dont le mail est passé en paramètre
     * dans l'équipe identifiée par idTeam.
     *
     * @param idTeam         Identifiant de l'équipe concernée
     * @param mail           le mail de l'utilisateur à modifier (identifiant unique)
     * @param userJSONString String JSON représentant l'utilisateur à modifier
     * @return un Boolean, true si l'utilisateur a bien été modifié, false sinon
     */
    boolean editUserInTeam(String idTeam, String mail, String userJSONString);

    /**
     * Méthode qui supprime un utilisateur dont le mail est passé en paramètre de l'équipe identifiée par idTeam.
     *
     * @param idTeam         Identifiant de l'équipe concernée
     * @param mail le mail de l'utilisateur à supprimer (identifiant unique)
     * @return un Boolean, true si l'utilisateur a bien été supprimé, false sinon
     */
    boolean deleteUserInTeam(String idTeam, String mail);

    /**
     * Méthode qui génère le planning des Croissants dans l'équipe identifiée par idTeam..
     *
     * @param  idTeam Identifiant de l'équipe concernée
     * @return un Boolean, true si le nouveau planning a bien été généré, false sinon
     */
    boolean generateRandomPlanningInTeam(String idTeam);
}
