import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { IdCid, HasIdCid, reviver, FileRefs } from './protocols/model';
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

    let option = {...baseOptions};
    option.params = new HttpParams().set('cid', containerId);

    return this.http.get(Config.getUrl(model) + Config.popularPath, option).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(r => this.logger.trace('fetched popular items:', r)),
      catchError(this.handleError('getPopulars', []))
    );
  }

  //TODO
  /** GET ts from the server */
  getModels<T>(model: Model): Observable<T[]> {

    return this.http.get(Config.getUrl(model), baseOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(r => this.logger.trace('fetched ts:', r)),
      catchError(this.handleError('getModels', []))
    );
  }

  getModel<T>(model: Model, idCid: IdCid): Observable<T> {
    const url = Config.getUrl(model) + idCid.toPath();
    return this.http.get(url, baseOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(m => this.logger.trace('got model:', m)),
      catchError(this.handleError<T>(`getModel model=${model} idCid=${idCid}`)));
  }

  postModel<T>(model: Model, entity: T): Observable<T> {
    return this.http.post(Config.getUrl(model), entity, writeOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(m => this.logger.trace('posted model:', m)),
      catchError(this.handleError<T>('postModel'))
    );
  }

  /** DELETE: delete the t from the server */
  deleteModel<T extends HasIdCid>(model: Model, entity: T | IdCid): Observable<any> {
    const idCid = entity instanceof IdCid ? entity : entity.idCid;
    const url = Config.getUrl(model) + idCid.toPath();

    return this.http.delete(url, writeOptions).pipe(
      tap(r => this.logger.trace(`deleted model idCid:${idCid}, resp:`, r)),
      catchError(this.handleError<string>('deleteModel'))
    );
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
