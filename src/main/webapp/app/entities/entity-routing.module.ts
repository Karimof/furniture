import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'furniture',
        data: { pageTitle: 'furnitureApp.furniture.home.title' },
        loadChildren: () => import('./furniture/furniture.module').then(m => m.FurnitureModule),
      },
      {
        path: 'type',
        data: { pageTitle: 'furnitureApp.type.home.title' },
        loadChildren: () => import('./type/type.module').then(m => m.TypeModule),
      },
      {
        path: 'brand',
        data: { pageTitle: 'furnitureApp.brand.home.title' },
        loadChildren: () => import('./brand/brand.module').then(m => m.BrandModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
