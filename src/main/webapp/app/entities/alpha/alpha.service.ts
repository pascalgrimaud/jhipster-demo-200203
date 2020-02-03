import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAlpha } from 'app/shared/model/alpha.model';

type EntityResponseType = HttpResponse<IAlpha>;
type EntityArrayResponseType = HttpResponse<IAlpha[]>;

@Injectable({ providedIn: 'root' })
export class AlphaService {
  public resourceUrl = SERVER_API_URL + 'api/alphas';

  constructor(protected http: HttpClient) {}

  create(alpha: IAlpha): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alpha);
    return this.http
      .post<IAlpha>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(alpha: IAlpha): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alpha);
    return this.http
      .put<IAlpha>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAlpha>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAlpha[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(alpha: IAlpha): IAlpha {
    const copy: IAlpha = Object.assign({}, alpha, {
      birthday: alpha.birthday && alpha.birthday.isValid() ? alpha.birthday.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.birthday = res.body.birthday ? moment(res.body.birthday) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((alpha: IAlpha) => {
        alpha.birthday = alpha.birthday ? moment(alpha.birthday) : undefined;
      });
    }
    return res;
  }
}
