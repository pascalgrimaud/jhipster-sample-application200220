import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IToto, Toto } from 'app/shared/model/toto.model';
import { TotoService } from './toto.service';
import { TotoComponent } from './toto.component';
import { TotoDetailComponent } from './toto-detail.component';
import { TotoUpdateComponent } from './toto-update.component';

@Injectable({ providedIn: 'root' })
export class TotoResolve implements Resolve<IToto> {
  constructor(private service: TotoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IToto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((toto: HttpResponse<Toto>) => {
          if (toto.body) {
            return of(toto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Toto());
  }
}

export const totoRoute: Routes = [
  {
    path: '',
    component: TotoComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.toto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TotoDetailComponent,
    resolve: {
      toto: TotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.toto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TotoUpdateComponent,
    resolve: {
      toto: TotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.toto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TotoUpdateComponent,
    resolve: {
      toto: TotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.toto.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
