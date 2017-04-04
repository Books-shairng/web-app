import { Component } from '@angular/core';

@Component({
  selector: 'login-app',
  template: `
    <form class="login-form">
      <label for="email-adress ">Adres e-mail</label>
      <input type="text" name="email-adress" id="email-adress" value="" placeholder="twojadres@buziaczek.pl" required>
      <label for="password">Hasło</label>
      <input type="text" name="password" id="password" value="" placeholder="Hasło" required>
      <button type="submit" class="btn btn-primary btn-lg btn-login active">Zaloguj</button>
      <p>Nie masz konta? <a href="#">Zarejestruj</a></p>
    </form>`,
})
export class LoginComponent  { }
