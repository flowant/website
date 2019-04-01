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
import { ContentViewerComponent } from './content-viewer/content-viewer.component';
import { ContentEditorComponent } from './content-editor/content-editor.component';
import { UserContentComponent } from './user-content/user-content.component';

const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' },
  { path: 'signin', component: SignInComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'admin', component: AdminComponent, canActivate: [AuthGuard], data: { a: Authority.Admin } },
  { path: 'search', component: SearchContentComponent },
  { path: 'search/:tag', component: SearchContentComponent },
  { path: 'content/edit', component: ContentEditorComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: 'content/edit/:id/:cid', component: ContentEditorComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: 'content/view/:id/:cid', component: ContentViewerComponent },
  { path: 'notification', component: NotificationComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: 'message', component: MessageComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: 'user/profile', component: UserProfileComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: 'user/profile/:id', component: UserProfileComponent, canActivate: [AuthGuard], data: { a: Authority.User } },
  { path: 'user/content', component: UserContentComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: 'user/content/:id', component: UserContentComponent, canActivate: [AuthGuard], data: { a: Authority.User} },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
