import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Content } from './protocols/model';
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
    private messageService: MessageService) { }

  /** GET contents from the server */
  getContents(): Observable<Content[]> {
    return this.http.get<Content[]>(this.contentUrl)
      .pipe(
        tap(_ => this.log('fetched contents')),
        catchError(this.handleError('getContents', []))
      );
  }

  /** GET content by id. Return `undefined` when id not found */
  getContentNo404<Data>(id: number): Observable<Content> {
    const url = `${this.contentUrl}/${id}`;
    return this.http.get<Content[]>(url)
      .pipe(
        map(contents => contents[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? `fetched` : `did not find`;
          this.log(`${outcome} content id=${id}`);
        }),
        catchError(this.handleError<Content>(`getContent id=${id}`))
      );
  }

  /** GET content by id. Will 404 if id not found */
  getContent(id: number): Observable<Content> {
    const url = `${this.contentUrl}/${id}`;
    return this.http.get<Content>(url).pipe(
      tap(_ => this.log(`fetched content id=${id}`)),
      catchError(this.handleError<Content>(`getContent id=${id}`))
    );
  }

  /* GET contents whose name contains search term */
  searchContents(term: string): Observable<Content[]> {
    if (!term.trim()) {
      // if not search term, return empty content array.
      return of([]);
    }
    return this.http.get<Content[]>(`${this.contentUrl}/?name=${term}`).pipe(
      tap(_ => this.log(`found contents matching "${term}"`)),
      catchError(this.handleError<Content[]>('searchContents', []))
    );
  }

  //////// Save methods //////////

  /** POST: add a new content to the server */
  addContent (content: Content): Observable<Content> {
    return this.http.post<Content>(this.contentUrl, content, httpOptions).pipe(
      tap((content: Content) => this.log(`added content w/ id=${content.id}`)),
      catchError(this.handleError<Content>('addContent'))
    );
  }

  /** DELETE: delete the content from the server */
  deleteContent (content: Content | string): Observable<Content> {
    const id = typeof content === 'string' ? content : content.id;
    const url = `${this.contentUrl}/${id}`;

    return this.http.delete<Content>(url, httpOptions).pipe(
      tap(_ => this.log(`deleted content id=${id}`)),
      catchError(this.handleError<Content>('deleteContent'))
    );
  }

  /** PUT: update the content on the server */
  updateContent (content: Content): Observable<any> {
    return this.http.put(this.contentUrl, content, httpOptions).pipe(
      tap(_ => this.log(`updated content id=${content.id}`)),
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

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a ContentService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`ContentService: ${message}`);
  }
}
