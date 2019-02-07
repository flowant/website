import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Content, reviver, FileRefs } from './protocols/model';
import { MessageService } from './message.service';

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

  gatewayUrl = 'http://localhost:9091/api';
  contentUrl = this.gatewayUrl + '/content';
  fileUrl = this.gatewayUrl + '/files';

  constructor(private http: HttpClient,
    private messageService: MessageService,
    private logger: NGXLogger) { }

  getGatewayURL(): string {
    return this.gatewayUrl;
  }

  /** GET contents from the server */
  getContents(): Observable<Content[]> {
    return this.http.get(this.contentUrl, baseOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(r => this.logger.trace('fetched contents:', r)),
      catchError(this.handleError('getContents', []))
    );
  }

  /** GET content by id. Will 404 if id not found */
  getContent(id: string): Observable<Content> {
    const url = `${this.contentUrl}/${id}`;
    return this.http.get(url, baseOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(r => this.logger.trace('fetched content:', r)),
      catchError(this.handleError<Content>(`getContent id=${id}`)));
  }

  // /* GET contents whose name contains search term */
  // searchContents(term: string): Observable<Content[]> {
  //   if (!term.trim()) {
  //     // if not search term, return empty content array.
  //     return of([]);
  //   }
  //   return this.http.get<Content[]>(`${this.contentUrl}/?name=${term}`).pipe(
  //     tap(r => this.logger.trace('found contents matching:', term)),
  //     catchError(this.handleError<Content[]>('searchContents', []))
  //   );
  // }

  /** POST: add a new content to the server */
  addContent(content: Content): Observable<Content> {
    return this.http.post(this.contentUrl, content, writeOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(c => this.logger.trace('added content:', c)),
      catchError(this.handleError<Content>('addContent'))
    );
  }

  /** PUT: update the content on the server */
  updateContent(content: Content): Observable<any> {
    return this.http.put(this.contentUrl, content, writeOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(c => this.logger.trace('updated content:', c)),
      catchError(this.handleError<any>('updateContent'))
    );
  }

  /** DELETE: delete the content from the server */
  deleteContent(content: Content | string): Observable<any> {
    const id = typeof content === 'string' ? content : content.id;
    const url = `${this.contentUrl}/${id}`;

    return this.http.delete(url, writeOptions).pipe(
      tap(r => this.logger.trace(`deleted content id:${id}, resp:`, r)),
      catchError(this.handleError<string>('deleteContent'))
    );
  }

  addFiles(files: FileList): Observable<FileRefs[]> {
    let uploadData = new FormData();

    for (let i = 0; i < files.length; i++) {
      uploadData.append('attachment', files[i], files[i].name);
    }

    return this.http.post(this.fileUrl, uploadData, baseOptions).pipe(
      map(r => JSON.parse(r.body, reviver)),
      tap(r => this.logger.trace('added files:', r)),
      catchError(this.handleError<FileRefs[]>('addFiles', [])));
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
