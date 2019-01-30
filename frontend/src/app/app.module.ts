import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { ContentComponent } from './content/content.component';
import { ProfileComponent } from './profile/profile.component';
import { SearchContentComponent } from './search-content/search-content.component';

@NgModule({
  declarations: [
    AppComponent,
    ContentComponent,
    ProfileComponent,
    SearchContentComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
