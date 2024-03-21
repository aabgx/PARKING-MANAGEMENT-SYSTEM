import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainLayoutComponent } from './main-layout/main-layout.component';
import { ParkAdminComponent } from './park-admin/park-admin.component';
import { ParkBookingComponent } from './park-booking/park-booking.component';
import { LoginComponent } from './login/login.component';
import {UsersDashboardComponent} from "./users-dashboard/users-dashboard.component";
import {UserSettingsComponent} from "./user-settings/user-settings.component";
import { RedirectComponent } from './redirect/redirect.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'redirect', component: RedirectComponent},
  {path:'',
  component:MainLayoutComponent,
  children: [
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    {path: 'configure', component: ParkAdminComponent},
    {path: 'book', component:ParkBookingComponent },
    {path: 'users', component:UsersDashboardComponent },
    {path: 'settings', component:UserSettingsComponent}
  ]
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
