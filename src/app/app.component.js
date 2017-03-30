"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require("@angular/core");
var AppComponent = (function () {
    function AppComponent() {
        this.name = 'Angular';
    }
    return AppComponent;
}());
AppComponent = __decorate([
    core_1.Component({
        selector: 'my-app',
        template: "\n  <nav class=\"navbar navbar-default navbar-static-top\">\n      <div class=\"container\">\n        <div class=\"navbar-header\">\n     <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#navbar-collapse-1\" aria-expanded=\"false\">\n       <span class=\"sr-only\">Toggle navigation</span>\n       <span class=\"icon-bar\"></span>\n       <span class=\"icon-bar\"></span>\n       <span class=\"icon-bar\"></span>\n     </button>\n     <a class=\"navbar-brand\" href=\"#\">BOOKS SHARING</a>\n   </div>\n   <div class=\"collapse navbar-collapse\" id=\"navbar-collapse-1\">\n        <ul class=\"nav navbar-nav navbar-right\">\n          <li><a href=\"#\">Szukaj</a></li>\n          <li><a href=\"#\">Lista ksi\u0105\u017Cek</a></li>\n          <li><a href=\"#\">Dodaj ksi\u0105\u017Ck\u0119</a></li>\n          <li><a href=\"#\">Powiadomienia</a></li>\n          <li><a href=\"#\">Konto</a></li>\n        </ul>\n    </div>\n  </div>\n    </nav>\n    <div class=\"container main-content logging-section\">\n\n    </div>",
    })
], AppComponent);
exports.AppComponent = AppComponent;
//# sourceMappingURL=app.component.js.map