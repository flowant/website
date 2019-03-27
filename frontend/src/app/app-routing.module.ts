import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

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
import { SignInComponent } from './sign-in/sign-in.component';
import { SignUpComponent } from './sign-up/sign-up.component';

const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' },
  { path: 'signin', component: SignInComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'admin', component: AdminComponent, canActivate: [AuthGuard], data: { a: Authority.Admin } },
  { path: 'search', component: SearchContentComponent },
  { path: 'search/:tag', component: SearchContentComponent },
  { path: 'content', component: ContentComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: 'content/:id/:cid', component: ContentComponent },
  { path: 'notification', component: NotificationComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: 'message', component: MessageComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: 'user/profile', component: UserProfileComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: 'user/profile/:id', component: UserProfileComponent, canActivate: [AuthGuard], data: { a: Authority.User } },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
