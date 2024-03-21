import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatIconModule} from '@angular/material/icon';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MainLayoutComponent} from './main-layout/main-layout.component';
import {HttpClientModule} from '@angular/common/http';
import {GridsterComponent, GridsterItemComponent} from 'angular-gridster2';
import {ParkAdminComponent} from './park-admin/park-admin.component';
import {ParkBookingComponent} from './park-booking/park-booking.component';
import {MatDialogModule} from '@angular/material/dialog';
import {BookDialogComponent} from './book-dialog/book-dialog.component';
import {LoginComponent} from './login/login.component';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatCardModule} from '@angular/material/card';
import {UsersDashboardComponent} from './users-dashboard/users-dashboard.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatSelectModule} from "@angular/material/select";
import {ToastrModule} from 'ngx-toastr';
import { UserSettingsComponent } from './user-settings/user-settings.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { RedirectComponent } from './redirect/redirect.component';
import { AddSectionDialogComponent } from './add-section-dialog/add-section-dialog.component';
import { SaveDoneComponent } from './save-done/save-done.component';



@NgModule({
  declarations: [
    AppComponent,
    MainLayoutComponent,
    ParkAdminComponent,
    ParkBookingComponent,
    BookDialogComponent,
    LoginComponent,
    UsersDashboardComponent,
    UserSettingsComponent,
    RedirectComponent,
    AddSectionDialogComponent,
    SaveDoneComponent
  ],
  imports: [
    MatDatepickerModule,
    MatNativeDateModule,
    GridsterComponent,
    GridsterItemComponent,
    MatButtonModule,
    MatDialogModule,
    MatIconModule,
    BrowserModule,
    MatSidenavModule,
    AppRoutingModule,
    HttpClientModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatSelectModule,
    ToastrModule.forRoot({
      positionClass: 'toast-top-center'
    }),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
