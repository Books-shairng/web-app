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
        template: "\n  <nav class=\"navbar navbar-default navbar-static-top\">\n      <div class=\"container\">\n        <div class=\"navbar-header\">\n     <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#navbar-collapse-1\" aria-expanded=\"false\">\n       <span class=\"sr-only\">Toggle navigation</span>\n       <span class=\"icon-bar\"></span>\n       <span class=\"icon-bar\"></span>\n       <span class=\"icon-bar\"></span>\n     </button>\n     <a class=\"navbar-brand\" href=\"#\">BOOKS SHARING</a>\n   </div>\n  </div>\n    </nav>\n    <div class=\"container main-content logging-section\">\n      <login-app></login-app>\n    </div>",
    })
], AppComponent);
exports.AppComponent = AppComponent;
//# sourceMappingURL=app.component.js.map