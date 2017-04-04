"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require("@angular/core");
var LoginComponent = (function () {
    function LoginComponent() {
    }
    return LoginComponent;
}());
LoginComponent = __decorate([
    core_1.Component({
        selector: 'login-app',
        template: "\n    <form class=\"login-form\">\n      <label for=\"email-adress \">Adres e-mail</label>\n      <input type=\"text\" name=\"email-adress\" id=\"email-adress\" value=\"\" placeholder=\"twojadres@buziaczek.pl\" required>\n      <label for=\"password\">Has\u0142o</label>\n      <input type=\"text\" name=\"password\" id=\"password\" value=\"\" placeholder=\"Has\u0142o\" required>\n      <button type=\"submit\" class=\"btn btn-primary btn-lg btn-login active\">Zaloguj</button>\n      <p>Nie masz konta? <a href=\"#\">Zarejestruj</a></p>\n    </form>",
    })
], LoginComponent);
exports.LoginComponent = LoginComponent;
//# sourceMappingURL=login.component.js.map