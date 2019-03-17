import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SearchContentComponent } from './search-content/search-content.component';
import { ContentComponent } from './content/content.component'
import { NotificationComponent } from './notification/notification.component'
import { MessageComponent } from './message/message.component'
import { UserProfileComponent } from './user-profile/user-profile.component'
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

const routes: Routes = [
  { path: '', redirectTo: '/search', pathMatch: 'full' },
  { path: 'search', component: SearchContentComponent },
  { path: 'search/:tag', component: SearchContentComponent },
  { path: 'content', component: ContentComponent },
  { path: 'content/:id/:cid', component: ContentComponent },
  { path: 'notification', component: NotificationComponent },
  { path: 'notification/:id/:cid', component: NotificationComponent },
  { path: 'message', component: MessageComponent },
  { path: 'message/:id/:cid', component: MessageComponent },
  { path: 'user/profile', component: UserProfileComponent },
  { path: 'user/profile/:id', component: UserProfileComponent },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
