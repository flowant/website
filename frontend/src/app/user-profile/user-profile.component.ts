import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { filter, concatMap, tap, defaultIfEmpty } from 'rxjs/operators';
import { User, Gender } from '../protocols/model';
import { BackendService } from '../backend.service';
import { Config, Model } from '../config';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  identity: string;

  user: User;

  userImageUri: string;

  isReadonly: boolean;

  genderKeys = Object.keys(Gender);

  genders = Gender;

  suffixReadonly(): string {
    return this.isReadonly ? "Readonly": "";
  }

  constructor(
    private backendService: BackendService,
    private location: Location,
    private route: ActivatedRoute,
    private logger: NGXLogger) { }

  ngOnInit() {

    this.identity = this.route.snapshot.paramMap.get('id');

    if (this.identity) {
      this.isReadonly = true;
      this.backendService.getUser(this.identity).subscribe(u => this.updateUser(u));
    } else {
      this.isReadonly = false;
      //TODO login user based
      this.backendService.getUser().subscribe(u => this.updateUser(u));
    }
  }

  updateUser(user?: User): User {

    if (user) {
      this.user = user;
    }

    if(this.user.fileRefs) {
      this.userImageUri = Config.imgServerUrl + '/' + this.user.identity;
    } else {
      this.userImageUri = "/assets/img/emptyAvatar.png";
    }

    this.logger.trace("userImageUri and user are updated:", this.userImageUri, this.user);

    return this.user;
  }

  onSave() {
    this.backendService.postUser(this.user)
        .subscribe(u => this.updateUser(u));
  }

  deleteIfExistPhoto(): Observable<any> {
    return of(this.user.fileRefs).pipe(
        filter(refs => Boolean(refs)),
        concatMap(refs => this.backendService.deleteFiles(refs)),
        tap(_ => this.user.fileRefs = undefined),
        tap(_ => this.updateUser()),
        defaultIfEmpty(undefined)
    );
  }

  onDeletePhoto() {
    this.deleteIfExistPhoto().subscribe();
  }

  onUploadPhoto(files: FileList) {
    this.deleteIfExistPhoto()
        .pipe(concatMap(_ => this.backendService.addFile(this.user.identity, files)))
        .subscribe(fileRef => {
          this.user.fileRefs = [fileRef];
          this.updateUser();
        });
  }

}