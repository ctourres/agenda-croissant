/**
 * Fichier principal de configuration du routeur VueJS et de l'application VueJS
 */

// Récupération de la configuration
const CroissantConfig = new ConfReader("resources/conf.json").getConfiguration();

const routes = [
    { path: '/:idTeam', component: PlanningView, props : (route) => ({ idTeam: route.params.idTeam })
    },
    { path: '/:idTeam/admin', component: AdminView, props : (route) => ({ idTeam: route.params.idTeam })
    },
];

const router = new VueRouter({
    routes
});

const app = new Vue({
    router
}).$mount('#app');
