"use strict";
var router_1 = require("@angular/router");
var login_component_1 = require("./login/login.component");
var registration_component_1 = require("./registration/registration.component");
var search_component_1 = require("./search/search.component");
exports.router = [
    { path: '', component: login_component_1.LoginComponent },
    { path: 'login', component: login_component_1.LoginComponent },
    { path: 'registration', component: registration_component_1.RegistrationComponent },
    { path: 'search', component: search_component_1.SearchComponent },
    { path: '**', redirectTo: '' },
];
exports.routes = router_1.RouterModule.forRoot(exports.router);
//# sourceMappingURL=app.routes.js.map