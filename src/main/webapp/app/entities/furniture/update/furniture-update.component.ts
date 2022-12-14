import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FurnitureFormService, FurnitureFormGroup } from './furniture-form.service';
import { IFurniture } from '../furniture.model';
import { FurnitureService } from '../service/furniture.service';
import { IBrand } from 'app/entities/brand/brand.model';
import { BrandService } from 'app/entities/brand/service/brand.service';
import { Installation } from 'app/entities/enumerations/installation.model';

@Component({
  selector: 'jhi-furniture-update',
  templateUrl: './furniture-update.component.html',
})
export class FurnitureUpdateComponent implements OnInit {
  isSaving = false;
  furniture: IFurniture | null = null;
  installationValues = Object.keys(Installation);

  brandsSharedCollection: IBrand[] = [];

  editForm: FurnitureFormGroup = this.furnitureFormService.createFurnitureFormGroup();

  constructor(
    protected furnitureService: FurnitureService,
    protected furnitureFormService: FurnitureFormService,
    protected brandService: BrandService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBrand = (o1: IBrand | null, o2: IBrand | null): boolean => this.brandService.compareBrand(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ furniture }) => {
      this.furniture = furniture;
      if (furniture) {
        this.updateForm(furniture);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const furniture = this.furnitureFormService.getFurniture(this.editForm);
    if (furniture.id !== null) {
      this.subscribeToSaveResponse(this.furnitureService.update(furniture));
    } else {
      this.subscribeToSaveResponse(this.furnitureService.create(furniture));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFurniture>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(furniture: IFurniture): void {
    this.furniture = furniture;
    this.furnitureFormService.resetForm(this.editForm, furniture);

    this.brandsSharedCollection = this.brandService.addBrandToCollectionIfMissing<IBrand>(this.brandsSharedCollection, furniture.brand);
  }

  protected loadRelationshipsOptions(): void {
    this.brandService
      .query()
      .pipe(map((res: HttpResponse<IBrand[]>) => res.body ?? []))
      .pipe(map((brands: IBrand[]) => this.brandService.addBrandToCollectionIfMissing<IBrand>(brands, this.furniture?.brand)))
      .subscribe((brands: IBrand[]) => (this.brandsSharedCollection = brands));
  }
}
