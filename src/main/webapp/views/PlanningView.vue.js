/**
 * Vue du planning
 *
 * Affiche la liste des utilisateurs avec le planning prévu
 */
const PlanningView = {
  template: `
  <div>
    <user-list
      v-bind:usersRemaining="usersRemaining"
      v-bind:usersBroughtCroissant="usersBroughtCroissant"
      v-bind:idTeam="idTeam"
      v-on:deleteUser="deleteUser($event)"
      v-on:refresh="refresh()">
    </user-list>

    <!-- BOUTON AFFECTER PLANNING ALEATOIREMENT -->
    <div align="center" class="mt-5">
      <button type="button" class="btn btn-danger" v-on:click="generateRandomPlanning()">
        Affecter aléatoirement
      </button>
    </div>
  </div>
  `,
  components: {
    'user-list': UserList,
  },
  props: {
    idTeam: {
        type: String,
        default: ''
    },
  },
  data() {
    return{
      userService: new UserService(this.idTeam),
      users: [],
      usersRemaining: [],
      usersBroughtCroissant: []
    }
  },
  mounted() {
    this.refresh();
  },
  methods: {
    /**
     * Rafraichit la liste des utilisateurs avec le service userService
     *
     * La liste est triée dans l'ordre chronologique à chaque refresh
     * En cas de date identique, le tri se fait en utilisant l'id du tableau de users
     */
    refresh() {
      this.userService.getAll()
        .then((data) => {
          this.users = data.sort( (a,b) => {return a.date - b.date} );
          this.separateUsers();
        })
    },
    /**
     * Supprime un utilisateur avec le service userService
     *
     * La liste est rafraîchie après la suppression si elle a eu lieu
     */
    deleteUser(user) {
      this.userService.delUser(user)
        .then((isRemoved) => {
          if (isRemoved) {
            this.refresh();
          }
        });
    },
    /**
    * Génère un nouveau planning avec le service userService
    *
    * La liste est rafraîchie après la génération si elle a eu lieu
    */
    generateRandomPlanning() {
      this.userService.generateRandomPlanning()
        .then((isGenerated) => {
          if (isGenerated) {
            this.refresh();
          }
        });
    },
    /**
    * Méthode permettant de séparer les utilisateurs ayant déjà amené les croissants de ceux ne l'ayant pas fait
    *
    * Une personne passe dans la liste des usersBroughtCroissant seulement le lendemain de sa date.
    */
    separateUsers() {
      let now = new Date();
      let today = new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime();
      this.usersBroughtCroissant.length = 0;
      this.usersRemaining.length = 0;
      this.users.forEach ((user) => {
        if (user.date === null || today <= user.date.getTime()){
          this.usersRemaining.push(user);
        } else {
          this.usersBroughtCroissant.push(user);
        }
      })
    },
  }
}
