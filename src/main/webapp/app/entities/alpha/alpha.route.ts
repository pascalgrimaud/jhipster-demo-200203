import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAlpha, Alpha } from 'app/shared/model/alpha.model';
import { AlphaService } from './alpha.service';
import { AlphaComponent } from './alpha.component';
import { AlphaDetailComponent } from './alpha-detail.component';
import { AlphaUpdateComponent } from './alpha-update.component';

@Injectable({ providedIn: 'root' })
export class AlphaResolve implements Resolve<IAlpha> {
  constructor(private service: AlphaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAlpha> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((alpha: HttpResponse<Alpha>) => {
          if (alpha.body) {
            return of(alpha.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Alpha());
  }
}

export const alphaRoute: Routes = [
  {
    path: '',
    component: AlphaComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'totoApp.alpha.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AlphaDetailComponent,
    resolve: {
      alpha: AlphaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'totoApp.alpha.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AlphaUpdateComponent,
    resolve: {
      alpha: AlphaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'totoApp.alpha.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AlphaUpdateComponent,
    resolve: {
      alpha: AlphaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'totoApp.alpha.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
