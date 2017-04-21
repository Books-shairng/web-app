"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require("@angular/core");
var platform_browser_1 = require("@angular/platform-browser");
var app_routes_1 = require("./app.routes");
var http_1 = require("@angular/http");
var forms_1 = require("@angular/forms");
var app_component_1 = require("./app.component");
var login_component_1 = require("./login/login.component");
var registration_component_1 = require("./registration/registration.component");
var search_component_1 = require("./search/search.component");
var index_1 = require("./notification/index");
var index_2 = require("./guards/index");
var index_3 = require("./services/index");
var AppModule = (function () {
    function AppModule() {
    }
    return AppModule;
}());
AppModule = __decorate([
    core_1.NgModule({
        imports: [platform_browser_1.BrowserModule, forms_1.FormsModule, app_routes_1.routes, http_1.HttpModule],
        declarations: [app_component_1.AppComponent, login_component_1.LoginComponent, registration_component_1.RegistrationComponent, search_component_1.SearchComponent, index_1.NotificationComponent],
        providers: [index_2.AuthGuard, index_3.AuthenticationService, index_3.UserService, fakeBackendProvider, MockBackend, BaseRequestOptions],
        bootstrap: [app_component_1.AppComponent]
    })
], AppModule);
exports.AppModule = AppModule;
//# sourceMappingURL=app.module.js.map