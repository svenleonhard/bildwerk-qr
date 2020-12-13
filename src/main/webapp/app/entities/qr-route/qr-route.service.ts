import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IQrRoute } from 'app/shared/model/qr-route.model';

type EntityResponseType = HttpResponse<IQrRoute>;
type EntityArrayResponseType = HttpResponse<IQrRoute[]>;

@Injectable({ providedIn: 'root' })
export class QrRouteService {
  public resourceUrl = SERVER_API_URL + 'api/qr-routes';

  constructor(protected http: HttpClient) {}

  create(qrRoute: IQrRoute): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(qrRoute);
    return this.http
      .post<IQrRoute>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(qrRoute: IQrRoute): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(qrRoute);
    return this.http
      .put<IQrRoute>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IQrRoute>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IQrRoute[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(qrRoute: IQrRoute): IQrRoute {
    const copy: IQrRoute = Object.assign({}, qrRoute, {
      startDate: qrRoute.startDate && qrRoute.startDate.isValid() ? qrRoute.startDate.format(DATE_FORMAT) : undefined,
      endDate: qrRoute.endDate && qrRoute.endDate.isValid() ? qrRoute.endDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((qrRoute: IQrRoute) => {
        qrRoute.startDate = qrRoute.startDate ? moment(qrRoute.startDate) : undefined;
        qrRoute.endDate = qrRoute.endDate ? moment(qrRoute.endDate) : undefined;
      });
    }
    return res;
  }
}
