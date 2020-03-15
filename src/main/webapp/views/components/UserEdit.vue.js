/**
 * Vue pour la modification d'un utilisateur
 */
const UserEdit = {
  template: `
    <tr>
      <td>
        <input v-model="user.name" type="text" class="form-control"/>
      </td>
      <td>
        <input v-model="user.forename" type="text" class="form-control"/>
      </td>
      <td></td>
      <td>
        <input v-model="formattedDate" type="date" class="form-control"/>
      </td>
      <td>
        <div align="center">
          <button class="btn btn-danger" v-on:click="editUser()" type="submit">
            <i class="fa fa-check"></i>
          </button>
          <button class="btn btn-danger" v-on:click="cancelUser()" type="button">
            <i class="fa fa-times"></i>
          </button>
        </div>
      </td>
    </tr>
  `,
  props: [
    'user',
    'idTeam'
  ],
  data() {
    return {
      userService: new UserService(this.idTeam),
      oldName: '',
      oldForename: '',
      oldDate:'',
    }
  },
  computed: {
    formattedDate: {
        get() {
          return (this.user.date && null !== this.user.date) ? this.user.date.toISOString().split('T')[0] : "";
        },
        set(date){
          this.user.date = new Date(date);
        }
    }
  },
  mounted() {
    this.oldName = this.user.name;
    this.oldForename = this.user.forename;
    this.oldDate = this.user.date;
  },
  methods: {
    /**
     * Méthode pour modifier un utilisateur.
     *
     * @return un boolean, si OK ou KO
     */
     editUser() {
       if (this.user.name != "" && this.user.forename != "" && this.user.date != "") {
         return this.userService.editUser(this.user)
         .then((isEdited) => {
           if (isEdited) {
             this.$emit('refresh');
             this.oldName = this.user.name;
             this.oldForename = this.user.forename;
             this.oldDate = this.user.date;
           }
         });
       }
     },
    /**
     * Méthode pour modifier un utilisateur.
     *
     * @return un boolean, si OK ou KO
     */
     cancelUser() {
       this.user.name = this.oldName;
       this.user.forename = this.oldForename;
       this.user.date = this.oldDate;
       this.$emit('refresh'); // inutile de faire un refresh ici
     }
  }
}