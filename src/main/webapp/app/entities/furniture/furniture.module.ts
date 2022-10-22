import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FurnitureComponent } from './list/furniture.component';
import { FurnitureDetailComponent } from './detail/furniture-detail.component';
import { FurnitureUpdateComponent } from './update/furniture-update.component';
import { FurnitureDeleteDialogComponent } from './delete/furniture-delete-dialog.component';
import { FurnitureRoutingModule } from './route/furniture-routing.module';

@NgModule({
  imports: [SharedModule, FurnitureRoutingModule],
  declarations: [FurnitureComponent, FurnitureDetailComponent, FurnitureUpdateComponent, FurnitureDeleteDialogComponent],
})
export class FurnitureModule {}
