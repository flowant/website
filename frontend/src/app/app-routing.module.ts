import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SearchContentComponent } from './search-content/search-content.component';
import { ContentComponent } from './content/content.component'
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

const routes: Routes = [
  { path: '', redirectTo: '/search', pathMatch: 'full' },
  { path: 'search', component: SearchContentComponent },
  { path: 'content', component: ContentComponent },
  { path: 'content/:id/:cid', component: ContentComponent },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
