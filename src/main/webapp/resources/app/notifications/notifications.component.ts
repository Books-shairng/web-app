import { Component, OnInit } from '@angular/core';
import { User } from '../models/index';
import { UserService } from '../services/index';

@Component({
    moduleId: module.id,
    templateUrl: 'notifications.component.html'
})

export class NotificationComponent implements OnInit {
  users: User[] = [];

   constructor(private userService: UserService) { }

   ngOnInit() {
       // get users from secure api end point
       this.userService.getUsers()
           .subscribe(users => {
               this.users = users;
           });
   }

}
