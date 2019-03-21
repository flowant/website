import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import  {NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { JwtInterceptor, AuthErrorInterceptor } from './_interceptors';

import { AppComponent } from './app.component';
import { ContentComponent } from './content/content.component';
import { SearchContentComponent } from './search-content/search-content.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { ReviewComponent } from './review/review.component';
import { TopbarComponent } from './topbar/topbar.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { ReputationComponent } from './reputation/reputation.component';
import { MessageComponent } from './message/message.component';
import { NotificationComponent } from './notification/notification.component';
import { UserRefComponent } from './user-ref/user-ref.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ModalsComponent } from './modals/modals.component';
import { NgbModalSendMessageComponent } from './ngb-modal-send-message/ngb-modal-send-message.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AdminComponent } from './admin/admin.component';

@NgModule({
  declarations: [
    AppComponent,
    ContentComponent,
    SearchContentComponent,
    PageNotFoundComponent,
    ReviewComponent,
    TopbarComponent,
    SidebarComponent,
    ReputationComponent,
    MessageComponent,
    NotificationComponent,
    UserRefComponent,
    UserProfileComponent,
    ModalsComponent,
    NgbModalSendMessageComponent,
    LoginComponent,
    HomeComponent,
    AdminComponent
  ],
  imports: [
    NgbModule,
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    LoggerModule.forRoot({serverLoggingUrl: 'http://localhost:8888/debug.test', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.TRACE})
  ],
  entryComponents: [
    NgbModalSendMessageComponent
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
