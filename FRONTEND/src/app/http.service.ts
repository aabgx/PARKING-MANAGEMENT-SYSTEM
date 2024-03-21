import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {PostConfiguration} from './post-configuration';

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  protected defaultHeaders = new HttpHeaders();
  protected configuration = new PostConfiguration();
  protected basePath = 'http://localhost:8081';

  constructor(private http: HttpClient) {
  }

  /**
   * Generic post operation - always POST with this service
   * @param postBody - body to send over
   * @param postUrl - URL to post to
   * @param observe - observing the ...
   * @param reportProgress - should it report the progress
   */
  public post(postBody: object, postUrl: string, observe?: 'body', reportProgress?: boolean): Observable<any>;
  public post(postBody: object, postUrl: string, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    if (postBody === null || postBody === undefined || !postUrl) {
      throw new Error('Required parameter was null or undefined when calling generic post service.');
    }

    let headers = this.getHeaders();
    return this.http.post<any>(`${this.basePath}/${postUrl}`,
      postBody,
      {
        withCredentials: this.configuration.withCredentials,
        headers,
        observe,
        reportProgress
      }
    );
  }

  public get(getUrl: string, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    let headers = this.getHeaders();
    return this.http.get<any>(`${this.basePath}/${getUrl}`, {
      headers: headers,
      observe: observe,
      reportProgress: reportProgress
    });
  }

  public put(putBody: object, putUrl: string, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    if (putBody === null || putBody === undefined || !putUrl) {
      throw new Error('Required parameter was null or undefined when calling generic post service.');
    }

    let headers = this.getHeaders();
    return this.http.put<any>(`${this.basePath}/${putUrl}`,
      putBody,
      {
        withCredentials: this.configuration.withCredentials,
        headers,
        observe,
        reportProgress
      }
    );
  }

  public delete(deleteUrl: string, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
    let headers = this.getHeaders();

    return this.http.delete<any>(`${this.basePath}/${deleteUrl}`, {
      headers: headers,
      observe: observe,
      reportProgress: reportProgress
    });
  }

  private getHeaders() : any {
    let headers = this.defaultHeaders;

    // to determine the Accept header
    const httpHeaderAccepts: string[] = [
      'application/json'
    ];
    const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
    if (httpHeaderAcceptSelected !== undefined) {
      headers = headers.set('Accept', httpHeaderAcceptSelected);
    }

    const authToken = localStorage.getItem("auth_token");
    if (authToken) {
      headers = headers.set('Authorization', `Bearer ${authToken}`);
    }

    // to determine the Content-Type header
    const consumes: string[] = [
      'application/json'
    ];
    const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
    if (httpContentTypeSelected !== undefined) {
      headers = headers.set('Content-Type', httpContentTypeSelected);
    }

    return headers;
  }

}
