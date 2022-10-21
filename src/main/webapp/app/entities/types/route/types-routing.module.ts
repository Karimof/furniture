import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypesComponent } from '../list/types.component';
import { TypesDetailComponent } from '../detail/types-detail.component';
import { TypesUpdateComponent } from '../update/types-update.component';
import { TypesRoutingResolveService } from './types-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const typesRoute: Routes = [
  {
    path: '',
    component: TypesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypesDetailComponent,
    resolve: {
      types: TypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypesUpdateComponent,
    resolve: {
      types: TypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypesUpdateComponent,
    resolve: {
      types: TypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typesRoute)],
  exports: [RouterModule],
})
export class TypesRoutingModule {}
