package croissants.controller;


import croissants.business.Config;
import croissants.business.Team;
import croissants.services.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Classe représentant le service REST d'accès aux configs.
 */
@RestController
public class AdminController {

    @Autowired
    private IAdminService teamService;

    /**
     * Méthode qui modifie la config d'une team.
     *
     * @param team Id de l'équipe à gérer
     * @return true si la configuration est bien modifié, false sinon.
     */
    @PutMapping(value = "{team}/admin/config")
    @CrossOrigin
    public ResponseEntity<Boolean> editConfig(@PathVariable("team") final String team,
                                            @RequestBody final Config config) {
        return new ResponseEntity<>(teamService.editConfig(team, config), HttpStatus.OK);
    }
    /**
     * Méthode qui reccupère la config d'une team.
     *
     * @param idTeam Id de l'équipe à gérer
     * @return true si la config existe, false sinon.
     */
    @GetMapping(value = "{team}/admin/config")
    @CrossOrigin
    public ResponseEntity<Config> getConfig(@PathVariable("team") final String idTeam) {
        return new ResponseEntity<>(teamService.getConfig(idTeam), HttpStatus.OK);
    }
}
