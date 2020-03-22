/**
 * Vue contenant la liste des utilisateurs
 *
 * Affiche la liste des utilisateurs avec le planning prévu
 */
const UserList = {
  template: `
  <div class="container">
    <div class="row">
      <div id="background-users">
        <h5 class="m-3">Ajouter une personne</h5>
        <table class="table table-borderless table-striped table-style">
          <user-add v-bind:idTeam="idTeam" v-on:refresh="$emit('refresh')"></user-add>
          <tr>
            <td colspan="5">
                <hr>
                <h5>A venir</h5>
            </td>
          </tr>
          <tr>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Mail</th>
            <th>Date</th>
            <th></th>
          </tr>
          <user-display v-for="elem in usersRemaining" v-bind:user="elem" v-bind:idTeam="idTeam" v-on:refresh="$emit('refresh')" v-on:deleteUser="$emit('deleteUser', elem)"></user-display>
          <tr>
            <td colspan="5">
                <hr>
                <h5>Décorés de la légion d'honneur du Croissant pour service rendu</h5>
            </td>
          </tr>
          <tr>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Mail</th>
            <th>Date</th>
            <th></th>
          </tr>
          <user-display v-for="elem in usersBroughtCroissant" v-bind:user="elem" v-bind:idTeam="idTeam" v-on:refresh="$emit('refresh')" v-on:deleteUser="$emit('deleteUser', elem)"></user-display>
        </table>
      </div>
    </div>
  </div>
  `,
  props: [
    'usersRemaining',
    'usersBroughtCroissant',
    'idTeam'
  ],
  components: {
     'user-add': UserAdd,
     'user-display': UserDisplay,
  },
}
