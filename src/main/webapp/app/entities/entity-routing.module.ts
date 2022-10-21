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
        path: 'types',
        data: { pageTitle: 'furnitureApp.types.home.title' },
        loadChildren: () => import('./types/types.module').then(m => m.TypesModule),
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
