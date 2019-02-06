import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Content, reviver } from './protocols/model';
import { MessageService } from './message.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  // private contentUrl = 'api/content';
  private contentUrl = 'http://localhost:9091/api/content';

  constructor(private http: HttpClient,
    private messageService: MessageService,
    private logger: NGXLogger) { }

  /** GET contents from the server */
  getContents(): Observable<Content[]> {
    return this.http.get(this.contentUrl, {observe: 'response', responseType: 'text'})
      .pipe(
        map(r => JSON.parse(r.body, reviver)),
        tap(r => this.logger.trace('fetched contents:', r)),
        catchError(this.handleError('getContents', []))
      );
  }

  /** GET content by id. Will 404 if id not found */
  getContent(id: string): Observable<Content> {
    const url = `${this.contentUrl}/${id}`;
    return this.http.get(url, {observe: 'response', responseType: 'text'})
      .pipe(
        map(r => JSON.parse(r.body, reviver)),
        tap(r => this.logger.trace('fetched content:', r)),
        catchError(this.handleError<Content>(`getContent id=${id}`)));
  }

  /* GET contents whose name contains search term */
  searchContents(term: string): Observable<Content[]> {
    if (!term.trim()) {
      // if not search term, return empty content array.
      return of([]);
    }
    return this.http.get<Content[]>(`${this.contentUrl}/?name=${term}`).pipe(
      tap(r => this.logger.trace('found contents matching:', term)),
      catchError(this.handleError<Content[]>('searchContents', []))
    );
  }

  /** POST: add a new content to the server */
  addContent (content: Content): Observable<Content> {
    return this.http.post<Content>(this.contentUrl, content, httpOptions).pipe(
      tap(c => this.logger.trace('added content:', c)),
      catchError(this.handleError<Content>('addContent'))
    );
  }

  /** DELETE: delete the content from the server */
  deleteContent (content: Content | string): Observable<Content> {
    const id = typeof content === 'string' ? content : content.id;
    const url = `${this.contentUrl}/${id}`;

    return this.http.delete<Content>(url, httpOptions).pipe(
      tap(_ => this.logger.trace('deleted content id:', id)),
      catchError(this.handleError<Content>('deleteContent'))
    );
  }

  /** PUT: update the content on the server */
  updateContent (content: Content): Observable<any> {
    return this.http.put(this.contentUrl, content, httpOptions).pipe(
      tap(c => this.logger.trace('updated content:', c)),
      catchError(this.handleError<any>('updateContent'))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T> (operation = 'operation', result?: T) {
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
