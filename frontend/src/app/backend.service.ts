import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { IdCid, HasIdCid, Reputation, reviver, FileRefs, RespWithLink } from './protocols/model';
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

  getModels<T>(model: Model, nextInfo: string, containerId?: string, page?: string, size?: string)
      : Observable<RespWithLink<T>> {

    if (!page) { page = "0"; }
    if (!size) { size = "12"; }

    let queryParams: string = nextInfo ? nextInfo : `?cid=${containerId}&page=${page}&size=${size}`;

    return this.http.get(Config.getUrl(model) + queryParams, baseOptions).pipe(
        map(r => RespWithLink.of<T>(JSON.parse(r.body, reviver), r.headers.get("link"))),
        tap(r => this.logger.trace('fetched mapped:', r)),
        catchError(this.handleError<RespWithLink<T>>(`getModels query:${queryParams}`)));
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
        tap(m => this.logger.trace('posted model:', model, m)),
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

  onRepute(model: Model, idCid: IdCid, selected?: string, reputation?: Reputation): Observable<Reputation> {
    // TODO check User Id and already clicked by user
    this.logger.trace("onRepute:", model, selected, idCid);
    let rpt = reputation ? reputation : new Reputation();
    if (selected) {
      rpt.select(selected);
    }
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
