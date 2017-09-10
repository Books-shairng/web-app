"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var router_1 = require("@angular/router");
var login_component_1 = require("./login/login.component");
var registration_component_1 = require("./registration/registration.component");
var search_component_1 = require("./search/search.component");
var index_1 = require("./notifications/index");
var index_2 = require("./guards/index");
var index_3 = require("./addbook/index");
var index_4 = require("./bookinfo/index");
var index_5 = require("./bookslist/index");
var index_6 = require("./settings/index");
var router = [
    { path: '', component: index_1.NotificationComponent, canActivate: [index_2.AuthGuard] },
    { path: 'login', component: login_component_1.LoginComponent },
    { path: 'registration', component: registration_component_1.RegistrationComponent },
    { path: 'search', component: search_component_1.SearchComponent },
    { path: 'addbook', component: index_3.AddBookComponent },
    { path: 'bookinfo', component: index_4.BookInfoComponent },
    { path: 'bookslist', component: index_5.BooksListComponent },
    { path: 'settings', component: index_6.SettingsComponent },
    { path: '**', redirectTo: '' },
];
exports.routes = router_1.RouterModule.forRoot(router);
//# sourceMappingURL=app.routes.js.map