package croissants.business;

import java.util.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import croissants.exceptions.CroissantException;


/**
 * Classe représentant la liste des utilisateurs
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {

    /**
     * Message d'erreur utilisateur introuvable
     */
    private static final String ERREUR_UTILISATEUR_INTROUVABLE = "Cet utilisateur est introuvable";

    /**
     * Logger de la classe Team
     */
    private static final Logger LOGGER = LogManager.getLogger(Team.class);

    /**
     * Id de l'équipe
     */
    private String id;

    /**
     * Nom de l'équipe
     */
    private String name;

    /**
     * configuration de la team
     */
    private Config config = new Config();

    /**
     * Liste des utilisateurs
     */
    private List<User> members = new ArrayList<>();

    /**
     * Récupération de l'identifiant de l'équipe
     *
     * @return L'identifiant de l'équipe
     */
    public String getId() {
        return id;
    }

    /**
     * Mutateur de l'identifiant de l'équipe
     *
     * @param id identifiant de l'équipe
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Récupération du nom de l'équipe
     *
     * @return Le nom de l'équipe
     */
    public String getName() {
        return name;
    }

    /**
     * Mutateur du nom de l'équipe
     *
     * @param name nom de l'équipe
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Récupération des membres d'une équipe
     *
     * @return List<User> membres d'une équipe
     */
    public List<User> getMembers() {
        return members;
    }

    /**
     * Mutateur de la liste des membres
     *
     * @param teamMembers équipe à ajouter
     */
    public void setMembers(List<User> teamMembers) {
        this.members = teamMembers;
    }

    /**
     * Récupération  de la config affectée a une team
     *
     * @return la config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Mutateur de configuration de l'equipe
     *
     * @param config nouvelle config a mettre à jour
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * Pour ajouter un nouvel utilisateur
     *
     * @param user un utilisateur
     */
    public void addUser(User user) {
        user.setDidBringCroissants(false);
        user.setIsActive(true);
        members.add(user);
    }

    /**
     * Pour modifier les informations d'utilisateur. L'utilisateur à modifier
     * est identifier grâce à son adresse mail.
     *
     * @param newUser le nouvel utilisateur
     * @param mail    ancienne adresse email de l'utilisateur
     */
    public void editUser(User newUser, String mail) throws CroissantException {
        User oldUser = null;
        for (User member : members) {
            if (member.getMail().equals(mail)) {
                oldUser = member;
            }
        }
        // si aucun utilisateur ne correspond à l'adresse email indiquée
        if (oldUser == null) {
            throw new CroissantException(ERREUR_UTILISATEUR_INTROUVABLE + " : " + mail);
        }
        // Met à jour les informations de l'utilisateur trouvé
        oldUser.setName(newUser.getName());
        oldUser.setForename(newUser.getForename());
        oldUser.setMail(newUser.getMail());
        oldUser.setDate(newUser.getDate());
    }

    /**
     * Pour supprimer un utilisateur
     *
     * @param mail adresse mail de l'utilisateur à supprimer
     */
    public void delUser(String mail) throws CroissantException {
        LOGGER.debug("entrée dans la fonction : delUser");
        boolean isEdited = false;
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMail().equals(mail)) {
                members.get(i).setIsActive(false);
                isEdited = true;
                break;
            }
        }
        if (!isEdited) {
            throw new CroissantException(ERREUR_UTILISATEUR_INTROUVABLE + " : " + mail);
        }
    }

    /**
     * Méthode permettant de récupérer la liste des utilisateurs actifs.
     *
     * @return la liste des utilisateurs actifs
     */
    public List<User> computeActiveMembersList() {
        List<User> users = new ArrayList<>();
        for (User user : getMembers()) {
            if (user.getIsActive()) {
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Méthode permettant de récupérer la liste des utilisateurs actifs et non assignés.
     *
     * @return la liste des utilisateurs actifs et non assignés
     */
    public List<User> computeUnassignedActiveMembersList() {
        Date currentDay;
        Calendar calendar = Calendar.getInstance();
        currentDay=calendar.getTime();
        List<User> users = new ArrayList<>();
        for (User user : computeActiveMembersList()) {
            //Si la date de l'utilisateur est nulle ou passée.
            if (user.getDate() == null || user.getDate().compareTo(currentDay) < 0) {
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Méthode permettant de récupérer la liste des utilisateurs actifs et assignés.
     *
     * @return la liste des utilisateurs actifs et assignés
     */
    public List<User> computeAssignedActiveMembersList() {
        List<User> users = new ArrayList<>();
        for (User user : computeActiveMembersList()) {
            if (user.getDate() != null) {
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Méthode qui détermine le dernier utilisateur affecté.
     * L'utilisateur avec la date la plus tardive.
     *
     * @return un utilisateur, null si aucune affectation
     */
    public User computeLastAffectedUser() {
        List<User> users = computeAssignedActiveMembersList();
        User lastAffectedUser = new User();
        int index = 0;
        // initialisation lastAffectedUser
        while (index < users.size() && users.get(index).getDate() == null) {
            index ++;
        }
        if (!users.isEmpty()) {
            lastAffectedUser = users.get(index);
        }
        // recherche du dernier affecté
        for (User user : users) {
            if (lastAffectedUser.getDate().before(user.getDate())) {
                lastAffectedUser = user;
            }
        }
        if (lastAffectedUser.getDate() == null) {
            return null;
        }
        else {
            return lastAffectedUser;
        }
    }

    /**
     * Méthode qui détermine si la date de livraison des croissants du dernier assigné
     * est passée.
     *
     * @return un boolean, true si oui, false sinon
     */
    public boolean doesLastAssignedBroughtCroissant() {
        Calendar calendar = Calendar.getInstance();
        Date currentDay = calendar.getTime();
        for (User user : computeActiveMembersList()) {
            if (currentDay.before(user.getDate())) {
                return false;
            }
        }
        return true;
    }
}
