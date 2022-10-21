import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFurniture } from '../furniture.model';
import { FurnitureService } from '../service/furniture.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './furniture-delete-dialog.component.html',
})
export class FurnitureDeleteDialogComponent {
  furniture?: IFurniture;

  constructor(protected furnitureService: FurnitureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.furnitureService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
