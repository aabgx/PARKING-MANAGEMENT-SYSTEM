import {Component, OnInit} from '@angular/core';
import {User} from './user.model';
import {DataService} from "../data.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import * as _ from "lodash";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-users-dashboard',
  templateUrl: './users-dashboard.component.html',
  styleUrl: './users-dashboard.component.css'
})
export class UsersDashboardComponent implements OnInit {

  public userForm: FormGroup;

  // users: User[] = [
  //   {username: 'Marius', phone: '0771913087', email: 'marius.andreiasi@gmail.com', userType: 'ADMIN'},
  //   {username: 'Razvan', phone: '0778934513', email: 'xdberry29@gmail.com', userType: 'LONG_TERM'},
  //   {username: 'Andrei', phone: '0765890045', email: 'andrei.erhan@gmail.com', userType: 'SPECIAL'},
  //   {username: 'Ioana', phone: '0658933412', email: 'baciu_ioana23@gmail.com', userType: 'SIMPLE'},
  //   {username: 'Andrada', phone: '078659821', email: 'andrada.ard02@gmail.com', userType: 'SIMPLE'},
  //   {username: 'Leo', phone: '0534891729', email: 'leonard.balint@gmail.com', userType: 'ADMIN'},
  //   {username: 'Alessia', phone: '0879983461', email: 'alessia.bidian@gmail.com', userType: 'LONG_TERM'}
  // ];

  protected users: User[]=[];

  public usersTypes: string[] = [
    "All Users",
    "SIMPLE",
    "SPECIAL",
    "ADMIN"
  ]

  public userType: string = "All Users";

  public enteredEmail: string = "";
  public showSuccessToast: boolean = false;
  public showErrorToast: boolean = false;
  toastMessage: string = '';
  public showEditUserModal: boolean = false;
  public showAddUserButtonInUserModal: boolean = false;
  public showUpdateUserButtonInUserModal: boolean = false;
  public showDeleteConfirmationPopup: boolean=false;
  private usernameToDelete: string='';
  private filteredUsers: User[] = [];

  private formIsWaitingResponse: boolean = false;

  constructor(private dataService: DataService,
              formBuilder: FormBuilder,
              private toastService: ToastrService) {
    this.userForm = formBuilder.group({
      username: new FormControl('', [Validators.required]),
      phone: new FormControl('', [Validators.required, Validators.pattern("^07[0-9]{8}")]),
      email: new FormControl('', [Validators.required, Validators.email]),
      userType: new FormControl('', [Validators.required, Validators.pattern("^(SIMPLE|SPECIAL|ADMIN|LONG_TERM)$")])
    });
  }

  ngOnInit(): void {
    this.dataService.getAllUsers().subscribe(
      {
        next: users => {
          this.users = users;
          this.filteredUsers = users.map((user: User) => Object.assign({}, user));
        },
        error: err => {
          console.error('Eroare primita de la dataService:', err);
        }
      }
    )
  }

  public saveUser(): void {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    if(!this.formIsWaitingResponse) {
      this.formIsWaitingResponse = true;
      const userData: User = this.buildUserData();
      this.dataService.saveUser(userData).subscribe(
        {
          next: userCreated => {
            if (userCreated.toString() === "user created") {
              this.users.push(userData);
              this.filteredUsers.push(userData);
              this.getUsersBySelectedUserType();
              this.closeUserModal();
              this.showToast('User successfully saved', 'success');
              this.formIsWaitingResponse = false;
            }
          },
          error: err => {
            console.error('Eroare primita de la dataService:', err);
            this.closeUserModal();
            this.showToast('A server error has occurred.', 'error');
          }
        }
      )
    }
  }

  public modifyUser(): void {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }
    if(!this.formIsWaitingResponse) {
      this.formIsWaitingResponse = true;
      const userData: User = this.buildUserData();
      this.dataService.modifyUser(userData).subscribe(
        {
          next: updatedUser => {
            console.log(updatedUser);
            if (_.isEqual(updatedUser, userData)) {
              const indexUser = this.users.findIndex((user) => user.username === userData.username);
              this.users.splice(indexUser, 1, updatedUser);
              this.filteredUsers.splice(indexUser, 1, updatedUser);
              this.getUsersBySelectedUserType();
              this.closeUserModal();
              this.showToast('User successfully updated', 'success');
              this.formIsWaitingResponse = false;
            }
          },
          error: err => {
            console.error('Eroare primita de la dataService:', err);
            this.showToast('A server error has occurred.', 'error');
          }
        }
      )
    }
  }

  public openConfirmationPopup(username:string):void{
    this.showDeleteConfirmationPopup=true;
    this.usernameToDelete=username;
  }


  public onCancel():void{
    this.showDeleteConfirmationPopup=false;
    this.usernameToDelete='';
  }

  public deleteUser(): void {
    this.dataService.deleteUser(this.usernameToDelete).subscribe(
      {
        next: response => {
          if (response.toString() === this.usernameToDelete) {
            this.users = this.users.filter((user) => user.username !== this.usernameToDelete);
            this.filteredUsers = this.filteredUsers.filter((user) => user.username !== this.usernameToDelete);
            this.closeUserModal();
            this.onCancel();
            this.showToast('User successfully deleted','success');
          }
        },
        error: err => {
          console.error('Eroare primita de la dataService:', err);
          this.showToast('A server error has occurred.','error');
        }
      }
    )
  }

  public getUsersBySelectedUserType(): void {
    if (this.userType.toString() === "All Users") {
      this.users = this.filteredUsers.filter((user) => /^(SIMPLE|SPECIAL|ADMIN|LONG_TERM)$/.test(user.userType.toString()));
    }
    else {
      this.users = this.filteredUsers.filter((user) => user.userType.toString() === this.userType.toString())
    }
  }

  public searchByEmailInput(): void {
    this.users = this.filteredUsers.filter((user) => user.email.toString().includes(this.enteredEmail.toString()));
  }

  public showUserModalAndSetUserForEdit(user: User) {
    this.showEditUserModal = true;
    this.showUpdateUserButtonInUserModal = true
    this.userForm.get("username")?.setValue(user.username.toString());
    this.userForm.get("phone")?.setValue(user.phone.toString());
    this.userForm.get("email")?.setValue(user.email.toString());
    this.userForm.get("userType")?.setValue(user.userType.toString());
  }

  public closeUserModal(): void {
    this.showEditUserModal = false;
    this.showAddUserButtonInUserModal = false;
    this.showUpdateUserButtonInUserModal = false;
    this.userForm.reset();
  }

  private buildUserData(): User {
    return {
      username: this.userForm.get("username")?.value,
      phone: this.userForm.get("phone")?.value,
      email: this.userForm.get("email")?.value,
      userType: this.userForm.get("userType")?.value
    };
  }


  showToast(message: string, type: 'success' | 'error'): void {
    this.toastMessage = message;
    if (type === 'success') {
      this.showSuccessToast = true;
      setTimeout(() => this.showSuccessToast = false, 2500);
    } else if (type === 'error') {
      this.showErrorToast = true;
      setTimeout(() => this.showErrorToast = false, 2500);
    }
  }

}
