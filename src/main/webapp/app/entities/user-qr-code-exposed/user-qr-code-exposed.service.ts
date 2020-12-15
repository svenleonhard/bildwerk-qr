import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUserQrCodeExposed } from 'app/shared/model/user-qr-code-exposed.model';

type EntityResponseType = HttpResponse<IUserQrCodeExposed>;
type EntityArrayResponseType = HttpResponse<IUserQrCodeExposed[]>;

@Injectable({ providedIn: 'root' })
export class UserQrCodeExposedService {
  public resourceUrl = SERVER_API_URL + 'api/user-qr-code-exposeds';

  constructor(protected http: HttpClient) {}

  create(userQrCodeExposed: IUserQrCodeExposed): Observable<EntityResponseType> {
    return this.http.post<IUserQrCodeExposed>(this.resourceUrl, userQrCodeExposed, { observe: 'response' });
  }

  update(userQrCodeExposed: IUserQrCodeExposed): Observable<EntityResponseType> {
    return this.http.put<IUserQrCodeExposed>(this.resourceUrl, userQrCodeExposed, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserQrCodeExposed>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserQrCodeExposed[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
