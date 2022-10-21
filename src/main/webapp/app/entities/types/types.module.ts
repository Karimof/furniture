import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypesComponent } from './list/types.component';
import { TypesDetailComponent } from './detail/types-detail.component';
import { TypesUpdateComponent } from './update/types-update.component';
import { TypesDeleteDialogComponent } from './delete/types-delete-dialog.component';
import { TypesRoutingModule } from './route/types-routing.module';

@NgModule({
  imports: [SharedModule, TypesRoutingModule],
  declarations: [TypesComponent, TypesDetailComponent, TypesUpdateComponent, TypesDeleteDialogComponent],
})
export class TypesModule {}
