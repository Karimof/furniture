import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFurniture } from '../furniture.model';
import { FurnitureService } from '../service/furniture.service';

@Injectable({ providedIn: 'root' })
export class FurnitureRoutingResolveService implements Resolve<IFurniture | null> {
  constructor(protected service: FurnitureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFurniture | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((furniture: HttpResponse<IFurniture>) => {
          if (furniture.body) {
            return of(furniture.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
