/**
 * Vue contenant une ligne utilisateur
 *
 * Affiche un utilisateur sur une ligne
 */
const UserDisplay = {
  template: `
    <user-edit v-bind:user="user" v-bind:idTeam="idTeam" v-if="isEdited" v-on:refresh="displayUser()"></user-edit>
    <tr v-else>
      <td :title="user.name"> {{ user.name }} </td>
      <td :title="user.forename"> {{ user.forename }} </td>
      <td :title="user.mail"> {{ user.mail }} </td>
      <td :title="translateDate(user.date)"> {{ translateDate(user.date) }} </td>
      <td class="buttons-style">
        <button type="button" class="btn btn-danger" v-on:click="editUser()">
          <i class="far fa-edit"></i>
        </button>
        <button type="button" class="btn btn-danger" v-on:click="confirmDelete()">
          <i class="fas fa-user-minus"></i>
        </button>
      </td>
    </tr>
  `,
  components: {
    'user-edit': UserEdit,
  },
  props: [
    'user',
    'idTeam'
  ],
  data() {
    return {
      isEdited: false,
    }
  },
  methods: {
    /**
     * Demande une confirmation avant de lancer la suppression
     */
    confirmDelete() {
      if (confirm("Voulez-vous supprimer " + this.user.forename + " " + this.user.name + " de la liste?")) {
        this.$emit('deleteUser');
      }
    },
    /**
     * Traduction de la date en français.
     */
    translateDate(date) {
      var options = { weekday: 'long' , year: 'numeric', month: 'long', day: 'numeric'}
      var translatedDate = new Date(date)
      if (date == null) {
        return ''
      }
      else {
        return translatedDate.toLocaleDateString('fr-FR', options);
      }
    },
    /**
     * Fonction qui permet l'édition d'un utilisateur
     */
    editUser() {
      this.isEdited = true;
    },
    /**
     * Fonction qui permet de sortir de l'édition d'un utilisateur
     */
    displayUser() {
      this.isEdited = false;
      this.$emit('refresh');
    }
  }
}
