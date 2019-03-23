import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { ReactiveFormsModule } from '@angular/forms';
import { JwtModule } from '@auth0/angular-jwt';

import { AppRoutingModule } from './app-routing.module';

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
import { HomeComponent } from './home/home.component';
import { AdminComponent } from './admin/admin.component';
import { AuthErrorInterceptor } from './_interceptors';
import { getAccessToken } from './_services';
import { Config } from './config';
import { SignInComponent } from './sign-in/sign-in.component';
import { SignUpComponent } from './sign-up/sign-up.component';

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
    HomeComponent,
    AdminComponent,
    SignInComponent,
    SignUpComponent
  ],
  imports: [
    NgbModule,
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: getAccessToken,
        whitelistedDomains: [Config.gateway.domain + ':' + Config.gateway.port],
        blacklistedRoutes: [Config.gateway.domain + ':' + Config.gateway.port + Config.path.gateway + Config.path.auth]
      }
    }),
    LoggerModule.forRoot({
      // serverLoggingUrl: 'http://localhost:8888/debug.test',
      level: NgxLoggerLevel.TRACE,
      serverLogLevel: NgxLoggerLevel.TRACE
    })
  ],
  entryComponents: [
    NgbModalSendMessageComponent
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
