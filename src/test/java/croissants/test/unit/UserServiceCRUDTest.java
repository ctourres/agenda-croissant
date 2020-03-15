package croissants.test.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import croissants.Application;
import croissants.business.Team;
import croissants.business.User;
import croissants.dao.DataStore;
import croissants.exceptions.CroissantException;
import croissants.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceCRUDTest {

    private final static String TEAM_ID = "Team 1";
    private final static String EMAIL = "clement.tourres@hotmail.com";

    /**
     * Déclaration de la variable team à tester
     */
    private Team teamTest;
    private Team teamUserActive;
    private Team teamUserInactive;
    private Team teamUserActiveAndInactive;
    private String emptyUserString= "";
    private String activeUserString="";
    private Team teamUserBroughtCroissant;
    private Team teamUnassigedUser;

    /**
     * Ressource contenant des équipes et de utilisateurs
     */
    @Value("classpath:unit/teamWithActiveUser.json")
    private Resource teamWithActiveUserResource;

    @Value("classpath:unit/teamWithInactiveUser.json")
    private Resource teamWithInactiveUserResource;

    @Value("classpath:unit/teamWithActiveAndInactiveUsers.json")
    private Resource teamWithActiveAndInactiveUsers;

    @Value("classpath:unit/emptyUser.json")
    private Resource emptyUserResource;

    @Value("classpath:unit/activeUser.json")
    private Resource activeUserResource;

    @Value("classpath:unit/teamAllUsersBroughtCroissant.json")
    private Resource teamAllUsersBroughtCroissant;

    @Value("classpath:unit/teamWithUnassignedUser.json")
    private Resource teamWithUnassignedUser;

    /**
     * Captor utilisé pour vérifier les appels
     */
    @Captor
    private ArgumentCaptor<Team> teamCaptor;

    @Autowired
    private IUserService userService;

    @MockBean
    private DataStore dataStore;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        teamTest = new Team();
        teamTest.setId(TEAM_ID);
        try {
            teamUserActive = mapper.readValue(teamWithActiveUserResource.getFile(), Team.class);
            teamUserInactive = mapper.readValue(teamWithInactiveUserResource.getFile(), Team.class);
            teamUserActiveAndInactive = mapper.readValue(teamWithActiveAndInactiveUsers.getFile(), Team.class);
            teamUserBroughtCroissant = mapper.readValue(teamAllUsersBroughtCroissant.getFile(), Team.class);
            teamUnassigedUser = mapper.readValue(teamWithUnassignedUser.getFile(), Team.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            emptyUserString = new String(Files.readAllBytes(emptyUserResource.getFile().toPath()));
            activeUserString = new String(Files.readAllBytes(activeUserResource.getFile().toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * L'appel de la méthode getUsersByTeam retourne une équipe
     *
     * @throws CroissantException
     */
    @Test
    public void whenGetAllUsersByTeam_thenReturnNonNullValueOfTypeTeam() throws CroissantException {
        // Mise en place
        when(dataStore.getTeam(TEAM_ID)).thenReturn(teamTest);

        // Excution du test
        Team team = userService.getUsersByTeam(TEAM_ID);

        // Vérification que l'appel renvoie un objet TEAM non null
        assertThat(team, is(notNullValue(Team.class)));
    }

    /**
     * L'appel de la méthode getUsersByTeam retourne l'équipe associée à l'id demandé
     *
     * @throws CroissantException
     */
    @Test
    public void whenGetAllUsersByTeam_thenReturnTeamWithRightId() throws CroissantException {
        // Mise en place
        when(dataStore.getTeam(TEAM_ID)).thenReturn(teamTest);

        // Excution du test
        Team team = userService.getUsersByTeam(TEAM_ID);

        // Vérification que l'appel renvoie un objet TEAM avec le bon id
        assertThat(team.getId(), is(TEAM_ID));
    }

    /**
     *  L'appel de la méthode getUsersByTeam retourne l'équipe avec l'utilisateur actif
     *
     * @throws CroissantException
     */
    @Test
    public void whenGetAllUsersByTeam_thenReturnActiveUser() throws CroissantException {
        // Mise en place
        when(dataStore.getTeam(TEAM_ID)).thenReturn(teamUserActive);

        // Excution du test
        Team team = userService.getUsersByTeam(TEAM_ID);

        // Vérification que l'appel renvoie un objet TEAM avec le user actif
        assertThat(team.getMembers(), is(teamUserActive.getMembers()));
    }

    /**
     *  L'appel de la méthode getUsersByTeam retourne l'équipe sans l'utilisateur inactif
     *
     * @throws CroissantException
     */
    @Test
    public void whenGetAllUsersByTeam_thenDoNotReturnInactiveUser() throws CroissantException {
        // Mise en place
        when(dataStore.getTeam(TEAM_ID)).thenReturn(teamUserInactive);

        // Excution du test
        Team team = userService.getUsersByTeam(TEAM_ID);

        // Vérification que l'appel renvoie un objet TEAM sans le user inactif
        assertThat(team.getMembers().isEmpty(), is(true));
    }

    /**
     *  L'appel de la méthode addUserInTeam retourne false quand le nom, le prénom et le mail de l'utilisateur
     *  à ajouter ne sont pas renseignés
     *
     * @throws CroissantException
     */
    @Test
    public void whenAddUserInTeam_thenReturnFalse() throws CroissantException {
        // Excution du test
        Boolean userIsAdded = userService.addUserInTeam(TEAM_ID, emptyUserString);

        // Vérification que l'appel renvoie un boolean false
        assertThat(userIsAdded, is(false));
    }

    /**
     *  L'appel de la méthode editUserInTeam retourne true quand le mail a été trouvé en base de données
     *
     * @throws CroissantException
     */
    @Test
    public void whenEditUserInTeam_thenReturnTrue() throws CroissantException {
        // Mise en place
        when(dataStore.getTeam(TEAM_ID)).thenReturn(teamUserActiveAndInactive);
        doNothing().when(dataStore).saveTeam(teamUserActiveAndInactive);

        // Excution du test
        Boolean userIsEdited = userService.editUserInTeam(TEAM_ID, EMAIL, activeUserString);

        // Vérification que l'appel renvoie un boolean true
        assertThat(userIsEdited, is(true));
    }

    /**
     *  L'appel de la méthode editUserInTeam retourne false quand le mail renseigné n'existe pas dans
     *  la base de données
     *
     * @throws CroissantException
     */
    @Test
    public void whenEditUserInTeam_thenReturnFalse() throws CroissantException {
        // Mise en place
        when(dataStore.getTeam(TEAM_ID)).thenReturn(teamUserInactive);
        doNothing().when(dataStore).saveTeam(teamUserActiveAndInactive);

        // Excution du test
        Boolean userIsEdited = userService.editUserInTeam(TEAM_ID, EMAIL, activeUserString);

        // Vérification que l'appel renvoie un boolean true
        assertThat(userIsEdited, is(false));
    }
    /**
     *  L'appel de la méthode delUserInTeam retourne true quand le mail a été trouvé en base de données
     *  la base de données
     *
     * @throws CroissantException
     */
    @Test
    public void whenDeleteUserInTeam_thenReturnTrue() throws CroissantException {
        // Mise en place
        when(dataStore.getTeam(TEAM_ID)).thenReturn(teamUserActiveAndInactive);
        doNothing().when(dataStore).saveTeam(any(Team.class));

        // Excution du test
        Boolean userIsDeleted = userService.deleteUserInTeam(TEAM_ID, EMAIL);

        // Vérification que l'appel renvoie un boolean true
        assertTrue(userIsDeleted);
        // Vérification qu'il n'y a plus d'utilisateur actif
        verify(dataStore).saveTeam(teamCaptor.capture());
        assertTrue(teamCaptor.getValue().computeActiveMembersList().isEmpty());
    }

    /**
     *  L'appel de la méthode delUserInTeam retourne false quand le mail renseigné n'existe pas dans
     *  la base de données
     *
     * @throws CroissantException
     */
    @Test
    public void whenDeleteUserInTeam_thenReturnFalse() throws CroissantException {
        // Mise en place
        when(dataStore.getTeam(TEAM_ID)).thenReturn(teamUserInactive);
        doNothing().when(dataStore).saveTeam(teamUserActiveAndInactive);

        // Excution du test
        Boolean userIsDeleted = userService.deleteUserInTeam(TEAM_ID, EMAIL);

        // Vérification que l'appel renvoie un boolean true
        assertThat(userIsDeleted, is(false));
    }


    /**
     * L'appel de la méthode computeUnassignedActiveMembersList retourne false quand il y a des
     * utilisateurs actifs et non assignés
     *
     * @throws CroissantException
     */
    @Test
    public void whenComputeUnassignedActiveMembersList_thenReturnFalse () throws CroissantException, IOException {
        // Mise en place
        Team teamUnassignedUser = new ObjectMapper(new YAMLFactory()).readValue(teamWithUnassignedUser.getFile(), Team.class);

        // Excution du test
        List<User> users = teamUnassignedUser.computeUnassignedActiveMembersList();

        // Vérification que l'appel renvoie un boolean false
        assertThat(users.size(), is(2));
    }
}
