package croissants.controller;

import croissants.business.Team;
import croissants.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * Classe représentant le service REST d'affichage des utilisateurs.
 */
@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * Implémentation du GET /users de l'API REST.
     * Renvoie tous les utilisateurs de la base de données d'une équipe.
     *
     * @param team Id de l'équipe à gérer
     * @return la liste des utilisateurs.
     */
    @GetMapping(value = "{team}/users")
    @CrossOrigin
    public ResponseEntity<Team> getUsers(@PathVariable("team") final String team) {
        return new ResponseEntity<>(userService.getUsersByTeam(team), HttpStatus.OK);
    }

    /**
     * Méthode qui ajoute un utilisateur à liste des utilisateurs d'une équipe
     * prend un JSON représentant un utilisateur.
     *
     * @param team Id de l'équipe à gérer
     * @return true si les utilisateurs sont bien récupérés, false sinon.
     * Erreur possible : erreur d'accès
     */
    @PostMapping(value = "{team}/users")
    @CrossOrigin
    public ResponseEntity<Boolean> addUser(@PathVariable("team") final String team,
                                           @RequestBody final String userJSONString) {
        return new ResponseEntity<>(userService.addUserInTeam(team, userJSONString), HttpStatus.OK);
    }

    /**
     * Méthode qui modifie les informations d'un utilisateur dans la liste des utilisateurs.
     * récupère le JSON représentant un utilisateur et les écrits dans la base de données.
     *
     * @param team Id de l'équipe à gérer
     * @return true si l'utilisateur et bien modifié, false sinon.
     * Erreurs possible :
     * - accès
     * - mail invalide
     * - ustilisateur introuvable
     */
    @PutMapping(value = "{team}/users/{mail}")
    @CrossOrigin
    public ResponseEntity<Boolean> editUser(@PathVariable("team") final String team,
                                            @PathVariable("mail") final String mail,
                                            @RequestBody final String userEditionJSONString) {
        return new ResponseEntity<>(userService.editUserInTeam(team, mail, userEditionJSONString), HttpStatus.OK);
    }

    /**
     * Méthode qui supprime un utilisateur a liste des utilisateurs,
     * prend le mail comme identifiant et supprime l'utilisateur de la base de données.
     *
     * @param team Id de l'équipe à gérer
     * @return true si l'utilisateur est bien supprimé, false sinon
     * Erreur possible : erreur d'accès
     */
    @DeleteMapping(value = "{team}/users/{mail}")
    @CrossOrigin
    public ResponseEntity<Boolean> deleteUser(@PathVariable("team") final String team,
                                              @PathVariable("mail") final String mail) {
        return new ResponseEntity<>(userService.deleteUserInTeam(team, mail), HttpStatus.OK);
    }

    /**
     * Méthode qui attribue aléatoirement à chaque membre une date pour amener les
     * croissants.
     * Appelée après appuit sur le bouton "génerer aléatoirement"
     *
     * @param team Id de l'équipe à gérer
     * @return true si la db est bien mise à jour, false sinon.
     * Erreur possible : erreur d'accès
     */
    @GetMapping(value = "{team}/planningGeneration")
    @CrossOrigin
    public ResponseEntity<Boolean> generateRandomPlanning(@PathVariable("team") final String team) {
        return new ResponseEntity<>(userService.generateRandomPlanningInTeam(team), HttpStatus.OK);
    }
}
