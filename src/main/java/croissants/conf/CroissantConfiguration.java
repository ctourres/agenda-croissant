package croissants.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * Cette classe stocke les paramètres de configuration générale du projet.
 */
@Configuration
public class CroissantConfiguration {

    /**
     * Chemin vers la base de données.
     */
    @Value("${databasePath}")
    private String databasePath;

    /**
     * Retourne la base de données.
     *
     * @return File de la base de données
     */
    public File getDatabaseFile() {
        return new File(databasePath);
    }
}