import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypeComponent } from '../list/type.component';
import { TypeDetailComponent } from '../detail/type-detail.component';
import { TypeUpdateComponent } from '../update/type-update.component';
import { TypeRoutingResolveService } from './type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const typeRoute: Routes = [
  {
    path: '',
    component: TypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeDetailComponent,
    resolve: {
      type: TypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeUpdateComponent,
    resolve: {
      type: TypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeUpdateComponent,
    resolve: {
      type: TypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeRoute)],
  exports: [RouterModule],
})
export class TypeRoutingModule {}
