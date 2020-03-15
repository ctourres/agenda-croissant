package croissants.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import croissants.controller.UserController;
import croissants.dao.DataStore;
import croissants.business.Team;
import croissants.business.User;
import croissants.exceptions.CroissantException;
import croissants.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Service de gestion des utilisateurs
 * Classe faisant le lien entre le RestController et la DAO
 */
@Service
public class UserServiceImpl implements IUserService {

    /**
     * Logger de la classe UserController
     */
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    /**
     * Message d'erreur d'accès au fichier
     */
    private static final String ERREUR_D_ACCES_AU_FICHIER_MSG = "Erreur d'accès au fichier";

    /**
     * Message d'erreur du parsing JSON
     */
    private static final String JSON_PARSING_EXCEPTION = "Erreur lors du parsing du JSON";

    /**
     * Datastore
     */
    @Autowired
    private DataStore dataStore;

    /**
     * Méthode qui renvoie tous les utilisateurs d'une équipe identifiée par idTeam.
     *
     * @param idTeam         Identifiant de l'équipe concernée
     * @return L'équipe demandée ou null si l'équipe n'est pas trouvée
     */
    @Override
    public Team getUsersByTeam(String idTeam) {
        Team team = getTeamDatabase(idTeam);
        if (null != team){
            team.setMembers(team.computeActiveMembersList());
        }
        return team;
    }

    /**
     * Méthode qui ajoute un utilisateur dans l'équipe identifiée par idTeam
     *
     * @param idTeam         Identifiant de l'équipe concernée
     * @param userJSONString String JSON représentant un utilisateur à ajouter
     * @return un Boolean, true si l'utilisateur a bien été ajouté, false sinon
     */
    @Override
    public boolean addUserInTeam(String idTeam, String userJSONString) {
        User user = getUserFromJSONString(userJSONString);
        if("".equals(user.getName()) && "".equals(user.getForename()) && "".equals(user.getMail())) {
            return false;
        }
        else {
            Team team = getTeamDatabase(idTeam);
            if (null != team){
                team.addUser(user);
                return setTeamDatabase(team);
            }
            else {
                return false;
            }
        }
    }

    /**
     * Méthode qui modifie les informations d'un utilisateur dont le mail est passé en paramètre
     * dans l'équipe identifiée par idTeam.
     *
     * @param idTeam         Identifiant de l'équipe concernée
     * @param mail           le mail de l'utilisateur à modifier (identifiant unique)
     * @param userJSONString String JSON représentant l'utilisateur à modifier
     * @return un Boolean, true si l'utilisateur a bien été modifié, false sinon
     */
    @Override
    public boolean editUserInTeam(String idTeam, String mail, String userJSONString) {
        User user = getUserFromJSONString(userJSONString);
        Team team = getTeamDatabase(idTeam);
        boolean isEdited = false;
        try {
            if (null != team){
                team.editUser(user, mail);
                isEdited = setTeamDatabase(team);
            }
        }
        catch (CroissantException exception) {
            LOGGER.debug(exception);
        }
        return isEdited;
    }

    /**
     * Méthode qui supprime un utilisateur dont le mail est passé en paramètre de l'équipe identifiée par idTeam.
     *
     * @param idTeam         Identifiant de l'équipe concernée
     * @param mail le mail de l'utilisateur à supprimer (identifiant unique)
     * @return un Boolean, true si l'utilisateur a bien été supprimé, false sinon
     */
    @Override
    public boolean deleteUserInTeam(String idTeam, String mail) {
        Team team = getTeamDatabase(idTeam);
        boolean isDesactivated = false;
        try {
            if (null != team){
                team.delUser(mail);
                isDesactivated = setTeamDatabase(team);
            }
        }
        catch (CroissantException exception){
            LOGGER.debug(exception);
        }
        return isDesactivated;
    }


    /**
     * Méthode qui génère le planning des Croissants dans l'équipe identifiée par idTeam..
     *
     * @param  idTeam Identifiant de l'équipe concernée
     * @return un Boolean, true si le nouveau planning a bien été généré, false sinon
     */
    @Override
    public boolean generateRandomPlanningInTeam(String idTeam) {
        Team team = getTeamDatabase(idTeam);
        if (null != team){
            team = handlePlanningGeneration(team);
            return setTeamDatabase(team);
        }
        else {
            return false;
        }
    }

    /**
     * Gère la génération du planning des croissants.
     * Attribue une date de livraison des croissants aux utilisateurs
     * actifs qui n'en n'ont pas. Réattribue une date quand tout les utilisateurs
     * ont déjà livré.
     *
     * @param team l'équipe
     * @return l'équipe avec dates attribuées
     */
    private static Team handlePlanningGeneration(Team team) {
        Team updatedTeam = new Team();
        updatedTeam.setId(team.getId());
        updatedTeam.setName(team.getName());
        updatedTeam.setConfig(team.getConfig());
        updatedTeam.setMembers(new ArrayList<>(team.getMembers()));

        List<User> unassignedActiveMembers = team.computeUnassignedActiveMembersList();
        List<User> affectedMembers = new ArrayList<>();
        if (!unassignedActiveMembers.isEmpty()) {
            affectedMembers = affectCroissantDay(unassignedActiveMembers, team.computeLastAffectedUser(), team);
        }
        else if (team.doesLastAssignedBroughtCroissant()) {
            affectedMembers = affectCroissantDay(team.computeActiveMembersList(), null, team);
        }
        try {
            for (User user : affectedMembers) {
                user.setDidBringCroissants(false);
                updatedTeam.editUser(user, user.getMail());
            }
        }
        catch (CroissantException exception) {
            LOGGER.debug(exception);
        }
        return updatedTeam;
    }

    /**
     * Affecte une date de livraison de croissant à tout les utilisateurs passés
     * en paramètre, avec pour première date la prochaine date de livraison.
     *
     * @param users la liste des utilisateurs sans dates attribuées
     * @param lastAffected le dernier utilisateur affecté.
     * @param team l'équipe
     * @return une liste d'utilisateurs avec dates attribuées
     */


    private static List<User> affectCroissantDay(List<User> users, User lastAffected, Team team) {
        int memberToAffect;
        int nbPersonne = team.getConfig().getNbPerDay();
        Date currentDay;
        List<User> affectedMembers = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        if (lastAffected != null && (lastAffected.getDate().compareTo(calendar.getTime()) >= 0)) {
            currentDay = lastAffected.getDate();
        }
        else {
            currentDay = calendar.getTime();
        }
        while (!users.isEmpty()) {
            currentDay = nextCroissantsDay(currentDay, team);
            int i =  usersLast(currentDay, team);
            do {
                memberToAffect = (int) (Math.random() * users.size());
                users.get(memberToAffect).setDate(currentDay);
                affectedMembers.add(users.get(memberToAffect));
                users.remove(memberToAffect);
                i++;
            }
            while (i < nbPersonne && !users.isEmpty());
        }
        return affectedMembers;
    }

    /**
     * Renvoie la date du prochain jour où seront livrés les croissants
     *
     * @param date actuelle
     * @param team l'équipe
     * @return date du prochain jour des croissants
     */
    private static Date nextCroissantsDay(Date date, Team team) {
        Calendar nextCroissantsDay = Calendar.getInstance();
        nextCroissantsDay.setTime(date);
        int nbPerDay = team.getConfig().getNbPerDay();
        int nbUserLastDate = usersLast(date, team);
        if (nbUserLastDate>=nbPerDay) {
            nextCroissantsDay.add(Calendar.DATE, 1);
        }
        while (nextCroissantsDay.get(Calendar.DAY_OF_WEEK) != (team.getConfig().getCroissantDay()+1)) {
            nextCroissantsDay.add(Calendar.DATE, 1);
        }
        return nextCroissantsDay.getTime();
    }

    /**
     * Renvoie le nombre de personne affectées à la dernière date de livraison des croissants
     *
     * @param date dernière date de livraison des croissants
     * @param team l'équipe
     * @return nombre de personne affectées à la dernière date de livraison des croissants
     */

    private static int  usersLast (Date date, Team team) {
        List<User> usersLast = new ArrayList<>();
        for (User user : team.computeAssignedActiveMembersList()) {
            if (user.getDate().compareTo(date)==0) {
                usersLast.add(user);
            }
        }
        return usersLast.size();
    }


    /**
     * Méthode qui récupère l'équipe correspondant à l'id stockée en base de données.
     *
     * @return l'équipe correspondant à l'id en base de données ou
     * une équipe égale à 'null' si l'id n'est pas trouvé en base de données
     */
    private Team getTeamDatabase(String idTeam) {
        Team team = null;
        try {
            team = dataStore.getTeam(idTeam);
        }
        catch (CroissantException exception) {
            LOGGER.debug(ERREUR_D_ACCES_AU_FICHIER_MSG);
            LOGGER.debug(exception);
        }
        return team;
    }

    /**
     * Méthode qui enregistre la nouvelle équipe dans la base de données.
     *
     * @param team la nouvelle base utilisateur à enregistrer dans la base de données
     * @return un boolean, true si la base de données a bien été mise à jour, false sinon
     */
    private boolean setTeamDatabase(Team team) {
        boolean databaseReWritten = false;
        try {
            dataStore.saveTeam(team);
            databaseReWritten = true;
        }
        catch (CroissantException exception) {
            LOGGER.debug(ERREUR_D_ACCES_AU_FICHIER_MSG);
            LOGGER.debug(exception);
        }
        return databaseReWritten;
    }

    /**
     * Méthode qui parse le JSON représentant un utilisateur.
     *
     * @param userJSONString String JSON représentant un utilisateur
     * @return un utilisateur
     */
    private static User getUserFromJSONString(String userJSONString) {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User();
        try {
            user = mapper.readValue(userJSONString, User.class);
        }
        catch (IOException exception) {
            LOGGER.debug(JSON_PARSING_EXCEPTION);
            LOGGER.debug(exception);
        }
        return user;
    }
}
