import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './login/login.component';
import { SearchContentComponent } from './search-content/search-content.component';
import { ContentComponent } from './content/content.component';
import { NotificationComponent } from './notification/notification.component';
import { MessageComponent } from './message/message.component';
import { HomeComponent } from './home/home.component';
import { AdminComponent } from './admin/admin.component';
import { AuthGuard } from './_guards';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { Authority } from './_models';

const routes: Routes = [
  { path: '', redirectTo: '/search', pathMatch: 'full' },
  // { path: '', component: HomeComponent, pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'admin', component: AdminComponent, canActivate: [AuthGuard], data: { a: Authority.Admin } },
  { path: 'search', component: SearchContentComponent },
  { path: 'search/:tag', component: SearchContentComponent },
  { path: 'content', component: ContentComponent },
  { path: 'content/:id/:cid', component: ContentComponent },
  { path: 'notification', component: NotificationComponent },
  { path: 'notification/:id/:cid', component: NotificationComponent },
  { path: 'message', component: MessageComponent },
  { path: 'message/:id/:cid', component: MessageComponent },
  { path: 'user/profile', component: UserProfileComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: 'user/profile/:id', component: UserProfileComponent, canActivate: [AuthGuard], data: { a: Authority.User } },
  { path: '**', redirectTo: '' }
  // { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
