/**
 * Vue du planning
 *
 * Affiche la liste des utilisateurs avec le planning prévu
 */
const AdminView = {
  template: `
  <div align = "center" id="background-admin">

    <tr >
        <td id="form-admin">
            <legend align="center"> Nombre de personne par jour</legend>
            <input type="number" min="1" v-model="nbPerDay" id="nb-personne"/>

        </td>
        <td align="left" >
            <legend align="center"> Jour de croissants</legend>
            <form id="form-admin">
                <input type="radio" name="jour" value="1" v-model="croissantDayStr"/> Lundi<br>
                <input type="radio" name="jour" value="2" v-model="croissantDayStr"/> Mardi<br>
                <input type="radio" name="jour" value="3" v-model="croissantDayStr"/> Mercredi<br>
                <input type="radio" name="jour" value="4" v-model="croissantDayStr"/> Jeudi<br>
                <input type="radio" name="jour" value="5" v-model="croissantDayStr"/> Vendredi<br>
            </form>
        </td>
    </tr>

    <!-- BOUTON GENERATION CONFIG -->
    <div align="center">
      <button type="button" class="btn btn-danger" v-on:click="editConfig()">
        Valider la configuration
      </button>
    </div>
  </div>
  `,

  components: {
  },

  props: {
    idTeam: {
        type: String,
        default: ''
    },
  },
  data(){
    return {
        configAdminService: new ConfigAdminService(this.idTeam),
        croissantDay: 5,
        nbPerDay: 1,
        }
  },
  computed: {
    croissantDayStr: {
      get() {
        return `${this.croissantDay}`;
      },
      set(croissantDay) {
        this.croissantDay = parseInt(croissantDay);
      }
    }

  },
  created(){
    this.configAdminService.getConfig()
    .then((oldConf) => {
        this.croissantDay = oldConf.croissantDay;
        this.nbPerDay = oldConf.nbPerDay;
    });
  },

  methods: {

    dayInString(croissantDay){
        let res = "";
        switch (croissantDay) {
            case 1:
              res = "lundi";
              break;
            case 2:
              res = "mardi";
              break;
            case 3:
              res = "mercredi";
              break;
            case 4:
              res = "jeudi"; 
              break;
            case 5:
              res = "vendredi"; 
              break;
            default:;
        }
        return res;
    },
    /**
    * Méthode qui créer une nouvelle config par le biais de AdminService.
    *
    */
    editConfig(){
        if ( confirm("Voulez-vous valider cette configuration : "  + this.nbPerDay + " personne(s) chaque " + this.dayInString(this.croissantDay) + " ?" )) {
          this.configAdminService.editConfig({croissantDay: this.croissantDay, nbPerDay: this.nbPerDay});
          router.push(`/${this.idTeam}`);
        }
    },
  }
}
