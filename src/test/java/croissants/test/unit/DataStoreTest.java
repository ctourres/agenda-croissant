package croissants.test.unit;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import croissants.Application;
import croissants.business.Team;
import croissants.conf.CroissantConfiguration;
import croissants.dao.DataStore;
import croissants.exceptions.CroissantException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DataStoreTest {

    private final static String TEAM_ID = "TEAM1";
    private List<Team> databaseTestList;
    private Team[] databaseTest;


    @Value("classpath:unit/DatabaseTest.yaml")
    private Resource databaseTestResource;

    @Autowired
    private DataStore dataStore;

    @MockBean
    private CroissantConfiguration configuration;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            databaseTest = mapper.readValue(databaseTestResource.getFile(), Team[].class);
            databaseTestList= Arrays.asList(databaseTest);
        } catch (IOException e) {

        }

    }

    /**
     * L'appel de la méthode getTeam retourne l'équipe correspondant au bon id avec les utilisateurs
     *
     * @throws CroissantException
     */
    @Test
    public void whenGetExistingTeam_thenReturnTeamWithUser() throws CroissantException, IOException {
        // Mise en place
        doReturn(databaseTestResource.getFile()).when(configuration).getDatabaseFile();

        // Excution du test
        Team team = dataStore.getTeam(TEAM_ID);

        // Vérification que l'appel renvoie un objet TEAM non null
        assertThat(team, is(notNullValue(Team.class)));
        assertThat(team.getId(), is(TEAM_ID));
        assertThat(team.getMembers().size(), is(1));
    }

    /**
     * L'appel de la méthode getTeam retourne null quand l'id de l'équipe ne correspond pas dans la base de données
     *
     * @throws CroissantException
     */
    @Test
    public void whenGetNonExistingTeam_thenReturnNull() throws CroissantException, IOException {
        // Mise en place
        doReturn(databaseTestResource.getFile()).when(configuration).getDatabaseFile();

        // Excution du test
        Team team = dataStore.getTeam("TEST");

        // Vérification que l'appel renvoie un objet TEAM non null
        assertThat(team, is(nullValue(Team.class)));
    }

}
