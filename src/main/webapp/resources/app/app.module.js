"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require("@angular/core");
var platform_browser_1 = require("@angular/platform-browser");
var http_1 = require("@angular/http");
var forms_1 = require("@angular/forms");
var app_component_1 = require("./app.component");
var app_routes_1 = require("./app.routes");
var index_1 = require("./login/index");
var index_2 = require("./registration/index");
var index_3 = require("./search/index");
var index_4 = require("./notifications/index");
var index_5 = require("./guards/index");
var index_6 = require("./services/index");
var index_7 = require("./addbook/index");
var index_8 = require("./bookinfo/index");
var index_9 = require("./bookslist/index");
var index_10 = require("./settings/index");
//used to create fake backend
var index_11 = require("./fakebackend/index");
var testing_1 = require("@angular/http/testing");
var http_2 = require("@angular/http");
;
var AppModule = (function () {
    function AppModule() {
    }
    return AppModule;
}());
AppModule = __decorate([
    core_1.NgModule({
        imports: [platform_browser_1.BrowserModule, forms_1.FormsModule, app_routes_1.routes, http_1.HttpModule],
        declarations: [app_component_1.AppComponent, index_1.LoginComponent, index_2.RegistrationComponent, index_3.SearchComponent, index_4.NotificationComponent, index_7.AddBookComponent, index_8.BookInfoComponent, index_9.BooksListComponent, index_10.SettingsComponent],
        providers: [index_5.AuthGuard, index_6.AuthenticationService, index_6.UserService,
            index_11.fakeBackendProvider,
            testing_1.MockBackend,
            http_2.BaseRequestOptions],
        bootstrap: [app_component_1.AppComponent]
    })
], AppModule);
exports.AppModule = AppModule;
//# sourceMappingURL=app.module.js.map