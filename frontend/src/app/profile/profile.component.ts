import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, empty, of } from 'rxjs';
import { User } from '../protocols/model';
import { BackendService } from '../backend.service';
import { Config, Model } from '../config';
import { NGXLogger, LoggerConfig } from 'ngx-logger';
import { userInfo } from 'os';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  model: Model = Model.User;

  identity: string;

  user: User;

  userImage: string;

  isReadonly: boolean;

  constructor(
    private backendService: BackendService,
    private location: Location,
    private route: ActivatedRoute,
    private logger: NGXLogger) { }

  ngOnInit() {

    this.identity = this.route.snapshot.paramMap.get('id');

    if (this.identity) {
      this.isReadonly = true;
      this.getUser(this.identity).subscribe(u => this.updateUser(u));
    } else {
      this.isReadonly = false;
      this.getUser("b901f010-4546-11e9-97e9-594de5a6cf90").subscribe(u => this.updateUser(u));
    }
  }

  getUser(id: string): Observable<User> {
    return this.backendService.getModel<User>(Model.User, id);
  }

  updateUser(user: User) {
    this.user = user;
    this.logger.trace("user is updated:", this.user);
    if(this.user.fileRefs) {
      this.userImage = Config.imgServerUrl + '/' + this.user.identity;
    }
  }

  getGender(): string {
    switch (this.user.gender) {
      case "M":
      return "Male";
      case "F":
      return "Female";
      default:
      return "Undefined";
    }
  }

  onSave() {
    this.backendService.postModel<User>(Model.User, this.user)
      .subscribe(u => this.updateUser(u));
  }

  deleteIfExistPhoto(): Observable<any> {
    this.userImage = "/assets/img/emptyAvatar.png";
    let deleteIfExist: Observable<any> = this.user.fileRefs ?
        this.backendService.deleteFiles(this.user.fileRefs) : of(0);
    return deleteIfExist;
  }

  onDeletePhoto() {
    this.deleteIfExistPhoto().subscribe(r => this.user.fileRefs = undefined);
  }

  onUploadPhoto(files: FileList) {
    this.deleteIfExistPhoto().subscribe(r => this.backendService.addFile(this.user.identity, files).subscribe(fileRef => {
      this.user.fileRefs = [fileRef];
      this.userImage = Config.imgServerUrl + '/' + this.user.identity;
    }));
  }

  suffixReadonly(): string {
    return this.isReadonly ? "Readonly": "";
  }

}
