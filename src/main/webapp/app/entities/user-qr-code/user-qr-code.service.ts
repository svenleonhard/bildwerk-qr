import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUserQrCode } from 'app/shared/model/user-qr-code.model';

type EntityResponseType = HttpResponse<IUserQrCode>;
type EntityArrayResponseType = HttpResponse<IUserQrCode[]>;

@Injectable({ providedIn: 'root' })
export class UserQrCodeService {
  public resourceUrl = SERVER_API_URL + 'api/user-qr-codes';

  constructor(protected http: HttpClient) {}

  create(userQrCode: IUserQrCode): Observable<EntityResponseType> {
    return this.http.post<IUserQrCode>(this.resourceUrl, userQrCode, { observe: 'response' });
  }

  update(userQrCode: IUserQrCode): Observable<EntityResponseType> {
    return this.http.put<IUserQrCode>(this.resourceUrl, userQrCode, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserQrCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserQrCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
