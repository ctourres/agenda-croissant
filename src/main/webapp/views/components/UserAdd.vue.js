/**
 * Vue contenant les champs d'ajout d'un utilisateur
 */
const UserAdd = {
  template: `
    <tr>
      <td style="padding-right: 40px; width: 17%;">
        <input v-model="name" type="text" class="form-control" placeholder="Nom"/>
      </td>
      <td style="padding-right: 40px; width: 17%;">
        <input v-model="forename" type="text" class="form-control" placeholder="Prénom"/>
      </td>
      <td style="padding-right: 40px; width: 30%;">
        <input v-model="mail" type="text" class="form-control" placeholder="Mail"/>
      </td>
      <td style="padding-right: 40px; width: 20%;">
        <div>
          <button id="addButton" v-on:click="addUser()" type="button" class="btn btn-danger">
            <i class="fas fa-user-plus"/>
          </button>
        </div>
      </td>
      <td style="width: 16%;"></td>
    </tr>
  `,
  props: {
    idTeam: {
        type: String,
        default: ''
    },
  },
  data() {
    return {
      userService: new UserService(this.idTeam),
      name: '',
      forename: '',
      mail: ''
    }
  },
  methods: {
    /**
     * Méthode qui récupère les valeurs du formulaire.
     *
     * @return User(name, forename, mail, date=null, isActive=true, didBringCroissant=false)
     */
    getNewUserFromForm() {
      return new User({"name":this.name, "forename":this.forename, "mail":this.mail,
                             "date":null, "isActive":true, "didBringCroissants":false});
           },
           /**
     * Méthode qui ajoute l'utilisateur par le biais de UserService.
     * Appelle le rafraichissement de la liste des utilisateurs dans UserList.
     *
     */
    addUser() {
      if (this.name != "" && this.forename != "" && this.mail != "") {
        return this.userService.addUser(this.getNewUserFromForm())
        .then((isAdded) => {
          if (isAdded) {
            // RaZ des champs et refresh
            this.name = "";
            this.forename = "";
            this.mail = "";
            this.$emit('refresh');
          }
        });
      }
    },
  }
}
