import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import  {NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { ContentComponent } from './content/content.component';
import { ProfileComponent } from './profile/profile.component';
import { SearchContentComponent } from './search-content/search-content.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { ReviewComponent } from './review/review.component';
import { TopbarComponent } from './topbar/topbar.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { ReputationComponent } from './reputation/reputation.component';
import { MessageComponent } from './message/message.component';
import { NotificationComponent } from './notification/notification.component';
import { UserComponent } from './user/user.component';

@NgModule({
  declarations: [
    AppComponent,
    ContentComponent,
    ProfileComponent,
    SearchContentComponent,
    PageNotFoundComponent,
    ReviewComponent,
    TopbarComponent,
    SidebarComponent,
    ReputationComponent,
    MessageComponent,
    NotificationComponent,
    UserComponent
  ],
  imports: [
    NgbModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    LoggerModule.forRoot({serverLoggingUrl: 'http://localhost:8888/debug.test', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.TRACE})
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
