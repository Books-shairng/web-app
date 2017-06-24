import { Component, OnInit } from '@angular/core';
import { User } from '../models/index';
import { UserService } from '../services/index';

@Component({
    moduleId: module.id,
    templateUrl: 'notifications.component.html'
})

export class NotificationComponent implements OnInit {
  users: User[] = [];
  currentUser: User;

   constructor(private userService: UserService) {
   this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
 }
   ngOnInit() {
       // get users from secure api end point
       console.log(this.currentUser); //name blank ??
       this.userService.getUsers()
           .subscribe(users => {
               this.users = users;
           });
   }

}
