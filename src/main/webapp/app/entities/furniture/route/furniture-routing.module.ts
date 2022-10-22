import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FurnitureComponent } from '../list/furniture.component';
import { FurnitureDetailComponent } from '../detail/furniture-detail.component';
import { FurnitureUpdateComponent } from '../update/furniture-update.component';
import { FurnitureRoutingResolveService } from './furniture-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const furnitureRoute: Routes = [
  {
    path: '',
    component: FurnitureComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FurnitureDetailComponent,
    resolve: {
      furniture: FurnitureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FurnitureUpdateComponent,
    resolve: {
      furniture: FurnitureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FurnitureUpdateComponent,
    resolve: {
      furniture: FurnitureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(furnitureRoute)],
  exports: [RouterModule],
})
export class FurnitureRoutingModule {}
