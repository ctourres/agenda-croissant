package croissants.dao;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import croissants.business.Team;
import croissants.conf.CroissantConfiguration;
import croissants.exceptions.CroissantException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Cette classe contient les fonctions de lecture écriture sur le fichier
 * Database.yaml
 */
@Repository
public class DataStore {
    /**
     * Logger de la classe DataStore
     */
    private static final Logger LOGGER = LogManager.getLogger(DataStore.class);

    @Autowired
    private CroissantConfiguration configuration;

    /**
     * Va chercher l'équipe concernée dans la database, et les transforme en objets Team avec jackson
     *
     * @return l'équipe recherchée
     * Erreurs possibles :
     * - structure JSON dans le fichier YAML
     * - accès au fichier YAML
     */
    public Team getTeam(String idTeam) throws CroissantException {
        LOGGER.debug("entrée dans la fonction : getTeam");
        Team loadedTeam = null;

        List<Team> databaseList = getTeams();
        for (Team team : databaseList) {
            if (idTeam.equals(team.getId())) {
                loadedTeam = team;
                break;
            }
        }

        return loadedTeam;
    }

    /**
     * Renvoie le contenu de la base de données
     *
     * @return La liste des équipes
     * @throws IOException Erreurs possibles :
     *                     - accès au fichier YAML
     */
    private List<Team> getTeams() throws CroissantException {
        List<Team> teams = null;

        try {
            // Chargement via l'object mapper
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            File databaseFileInstance = configuration.getDatabaseFile();
            Team[] databaseArray = mapper.readValue(databaseFileInstance, Team[].class);
            teams = Arrays.asList(databaseArray);
        }
        catch (IOException e) {
            LOGGER.error("Impossible d'accéder au fichier de données.");
            LOGGER.debug(e);
            throw new CroissantException(e.getMessage());
        }

        return teams;
    }

    /**
     * Cette fonction réécrit la base de données avec l'équipe à modifier.
     *
     * @param newTeam la liste d'utilisateurs à écrire dans Database.yaml
     * @throws IOException Erreurs possibles :
     *                     - erreur d'écriture dans le fichier YAML
     */
    public void saveTeam(Team newTeam) throws CroissantException {
        LOGGER.debug("entrée dans la fonction : saveTeam");

        if (null != newTeam && null != newTeam.getId()) {
            try {
                // Recherche de l'équipe à modifier
                List<Team> teams = getTeams();
                for (int i = 0; i < teams.size(); i++) {
                    if (newTeam.getId().equals(teams.get(i).getId())) {
                        // Remplacement par la nouvelle équipe
                        teams.set(i, newTeam);
                        break;
                    }
                }
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                mapper.writeValue(configuration.getDatabaseFile(), teams);
                LOGGER.debug("Réecriture de l'équipe à modifier effectuée.");
            }
            catch (IOException e) {
                LOGGER.error("Impossible d'écrire dans le fichier de données");
                LOGGER.debug(e);
                throw new CroissantException("Impossible d'écrire dans le fichier de données.");
            }
        }
    }
}
