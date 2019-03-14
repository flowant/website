import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { NGXLogger, LoggerConfig } from 'ngx-logger';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { IdCid, HasIdCid, IdToPath, Content, Reputation, reviver, FileRefs, RespWithLink, User } from './protocols/model';
import { MessageService } from './message.service';
import { Config, Model } from './config';

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

  constructor(
    private http: HttpClient,
    private messageService: MessageService,
    private logger: NGXLogger) { }

  getPopularItems<T>(model: Model, containerId: string): Observable<T[]> {

    let option = Object.assign({}, baseOptions);
    option.params = new HttpParams().set('cid', containerId);

    return this.http.get(Config.getUrl(model) + Config.popularPath, option).pipe(
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

  getModels<T>(model: Model, nextInfo: string, containerId?: string, page?: string, size?: string)
      : Observable<RespWithLink<T>> {

    page = page ? page : Config.defaultPage;
    size = size ? size: Config.defaultSize;

    let queryParams: string = nextInfo ? nextInfo : `?cid=${containerId}&page=${page}&size=${size}`;

    return this.http.get(Config.getUrl(model) + queryParams, baseOptions).pipe(
        map(r => RespWithLink.of<T>(JSON.parse(r.body, reviver), r.headers.get("link"))),
        tap(r => this.logger.trace('fetched mapped:', r)),
        catchError(this.handleError<RespWithLink<T>>(`getModels query:${queryParams}`)));
  }

  getModel<T>(model: Model, idToPath: IdToPath): Observable<T> {
    const url = Config.getUrl(model) + '/' + idToPath.toString();
    return this.http.get(url, baseOptions).pipe(
        map(r => JSON.parse(r.body, reviver)),
        tap(m => this.logger.trace('got model:', m)),
        catchError(this.handleError<T>(`getModel model=${model} idToPath=${idToPath}`)));
  }

  postModel<T>(model: Model, entity: T): Observable<T> {
    return this.http.post(Config.getUrl(model), entity, writeOptions).pipe(
        map(r => JSON.parse(r.body, reviver)),
        tap(m => this.logger.trace('posted model:', model, m)),
        catchError(this.handleError<T>('postModel'))
    );
  }

  /** DELETE: delete the t from the server */
  deleteModel<T extends HasIdCid>(model: Model, idToPath: IdToPath): Observable<any> {
    const url = Config.getUrl(model) + '/' + idToPath.toString();
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

      // TODO: better job of transforming error for user consumption
      this.messageService.add(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
