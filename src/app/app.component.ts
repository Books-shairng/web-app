import { Component } from '@angular/core';

@Component({
  selector: 'my-app',
  template: `
  <nav class="navbar navbar-default navbar-static-top">
      <div class="container">
        <div class="navbar-header">
     <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse-1" aria-expanded="false">
       <span class="sr-only">Toggle navigation</span>
       <span class="icon-bar"></span>
       <span class="icon-bar"></span>
       <span class="icon-bar"></span>
     </button>
     <a class="navbar-brand" href="#">BOOKS SHARING</a>
   </div>
  </div>
    </nav>
    <div class="container main-content logging-section">
      <login-app></login-app>
    </div>`,
})
export class AppComponent  { name = 'Angular'; }
