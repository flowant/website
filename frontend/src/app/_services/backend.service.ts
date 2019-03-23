import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { catchError, map, tap, concatMap } from 'rxjs/operators';
import { IdCid, HasIdCid, IdToPath, Content, Reputation, reviver, FileRefs } from '../_models';
import { RespWithLink, User, Relation, Auth } from '../_models';
import { Config, Model } from '../config';

interface TextResponseOption {
  headers?: HttpHeaders | {
    [header: string]: string | string[];
  };
  observe: 'response';
  params?: HttpParams | {
    [param: string]: string | string[];
  };
  reportProgress?: boolean;
  responseType: 'text';
  withCredentials?: boolean;
}

const writeOptions: TextResponseOption = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  observe: 'response',
  responseType: 'text'
};

const baseOptions: TextResponseOption = {
  observe: 'response',
  responseType: 'text'
};

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  private userSubject: BehaviorSubject<User>;

  private relationSubject: BehaviorSubject<Relation>;

  constructor(
    private http: HttpClient,
    private logger: NGXLogger) {

    this.userSubject = new BehaviorSubject<User>(User.guest());
    this.relationSubject = new BehaviorSubject<Relation>(Relation.empty);
  }

  getUserValue(): User {
    return this.userSubject.getValue();
  }

  getUser(identity?: string): Observable<User> {
    if (identity) { // request other user for inquery
      return this.getModel<User>(Model.User, identity);
    } else {
      return this.userSubject;
    }
  }

  changeUser(username?: string): Observable<User> {
    this.logger.trace("changeUser, username:", username);
    if (username) {
      return this.getModel<User>(Model.User, null, 'un', username).pipe(
        map(user => user as User),
        tap(user => this.userSubject.next(user)),
        concatMap(user => this.getModel<Relation>(Model.Relation, user.identity)),
        tap(relation => this.relationSubject.next(relation)),
        map(_ => this.userSubject.value)
      );
    } else {
      this.userSubject.next(User.guest());
      this.relationSubject.next(Relation.empty);
      return this.userSubject;
    }
  }

  signUpUser(user: User) {
    return this.http.post(Config.signupUrl, user, writeOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(m => this.logger.trace('signUp User:', user, m)),
    );
  }

  postUser(user: User): Observable<User> {
    return this.postModel<User>(Model.User, user).pipe(
      tap(user => this.userSubject.next(user))
    );
  }

  getRelation(): Observable<Relation> {
    return this.relationSubject;
  }

  postRelation(follow:boolean, followerId: string, followeeId: string) {

    if (User.isGuest(this.userSubject.value)) {
      this.logger.warn("Guest user's postRelation request is ignored");
      return;
    }

    let url = Config.relationUrl + (follow ? Config.path.follow : Config.path.unfollow)
        + "/" + followerId + "/" + followeeId;

    return this.http.post(url, null, writeOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(relation => this.relationSubject.next(relation)),
      tap(relation => this.logger.trace('posted Relation:', relation)),
      catchError(this.handleError<any>('postRelation'))
    );
  }

  getPopularItems<T>(model: Model, containerId: string): Observable<T[]> {

    let option = Object.assign({}, baseOptions);
    option.params = new HttpParams().set('cid', containerId);

    return this.http.get(Config.getUrl(model) + Config.path.popular, option).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(r => this.logger.trace('fetched popular items:', r)),
      catchError(this.handleError('getPopulars', []))
    );
  }

  getSearch(nextInfo: string, tag?: string, page?: string, size?: string)
      : Observable<RespWithLink<Content>> {

    page = page ? page : Config.defaultPage;
    size = size ? size: Config.defaultSize;

    let queryParams: string = nextInfo ? nextInfo : `?tag=${tag}&page=${page}&size=${size}`;

    return this.http.get(Config.searchUrl + queryParams, baseOptions).pipe(
      map(r => RespWithLink.of<Content>(JSON.parse(r.body, reviver), r.headers.get("link"))),
      tap(r => this.logger.trace('fetched mapped:', r)),
      catchError(this.handleError<RespWithLink<Content>>(`getSearch query:${queryParams}`)));
  }

  getModels<T>(model: Model, nextInfo: string, queryName: string, queryValue: string, page?: string, size?: string)
      : Observable<RespWithLink<T>> {

    page = page ? page : Config.defaultPage;
    size = size ? size: Config.defaultSize;

    let queryParams: string = nextInfo ? nextInfo : `?${queryName}=${queryValue}&page=${page}&size=${size}`;

    return this.http.get(Config.getUrl(model) + queryParams, baseOptions).pipe(
      map(r => RespWithLink.of<T>(JSON.parse(r.body, reviver), r.headers.get("link"))),
      tap(r => this.logger.trace('fetched mapped:', r)),
      catchError(this.handleError<RespWithLink<T>>(`getModels query:${queryParams}`)));
  }

  getModel<T>(model: Model, idToPath?: IdToPath, queryName?: string, queryValue?: string): Observable<T> {

    let url = Config.getUrl(model);

    if (idToPath) {
      url += '/' + idToPath.toString();
    }

    if (queryName && queryValue) {
      url += '?' + queryName + '=' + queryValue;
    }

    return this.http.get(url, baseOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(m => this.logger.trace('got model:', m)),
      catchError(this.handleError<T>(`getModel model=${model} idToPath=${idToPath}`)));
  }

  postModel<T>(model: Model, entity: T, urlSuffix?: string): Observable<T> {
    let url = Config.getUrl(model);
    url = urlSuffix ? url + urlSuffix : url;

    return this.http.post(url, entity, writeOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(m => this.logger.trace('posted model:', model, m)),
      catchError(this.handleError<T>('postModel'))
    );
  }

  deleteModel<T extends HasIdCid>(model: Model, idToPath: IdToPath, urlSuffix?: string): Observable<any> {
    let url = Config.getUrl(model) + '/' + idToPath.toString();
    url = urlSuffix ? url + '/' + urlSuffix : url;

    this.logger.trace("deleteModel url:", url);
    return this.http.delete(url, writeOptions).pipe(
      tap(r => this.logger.trace(`deleted model idToPath:${idToPath}, resp:`, r)),
      catchError(this.handleError<string>('deleteModel'))
    );
  }

  // FileList contains only one element when identity is used.
  // identity is the same as user identity, it enable to find the user picture by user id.
  addFile(identity: string, file: FileList): Observable<FileRefs> {
    let uploadData = new FormData();

    uploadData.append('attachment', file[0], file[0].name);

    return this.http.post(Config.fileUrl + '/' + identity, uploadData, baseOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(r => this.logger.trace('added files:', r)),
      catchError(this.handleError<FileRefs>('addFile')));
  }

  addFiles(files: FileList): Observable<FileRefs[]> {
    let uploadData = new FormData();

    for (let i = 0; i < files.length; i++) {
      uploadData.append('attachment', files[i], files[i].name);
    }

    return this.http.post(Config.fileUrl, uploadData, baseOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(r => this.logger.trace('added files:', r)),
      catchError(this.handleError<FileRefs[]>('addFiles', [])));
  }

  deleteFiles(files: FileRefs[]): Observable<any> {
    return this.http.post(Config.fileDeletesUrl, files, writeOptions).pipe(
      tap(r => this.logger.trace('deleted files:', r)),
      catchError(this.handleError<FileRefs[]>('deleteFiles')));
  }

  acculateRepute(model: Model, idCid: IdCid, reputation: Reputation): Observable<Reputation> {
    this.logger.trace("acculateRepute:", model, idCid, reputation);
    let rpt = Object.assign({}, reputation);
    rpt.idCid = idCid;
    return this.postModel<Reputation>(Config.toRptModel(model), rpt);
  }

  authorize(username: string, password: string): Observable<Auth> {
    let httpHeaders = new HttpHeaders()
      .set('Authorization', 'Basic ' + window.btoa(Config.auth.clientId + ':' + Config.auth.clientPass));

    let qeury = `?grant_type=password&username=${username}&password=${password}`;

    return this.http.post<Auth>(Config.authUrl + qeury, null, {headers: httpHeaders});
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // send the error to remote logging infrastructure
      this.logger.error(error);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
