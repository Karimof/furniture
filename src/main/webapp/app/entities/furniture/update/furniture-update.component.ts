import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FurnitureFormService, FurnitureFormGroup } from './furniture-form.service';
import { IFurniture } from '../furniture.model';
import { FurnitureService } from '../service/furniture.service';
import { ITypes } from 'app/entities/types/types.model';
import { TypesService } from 'app/entities/types/service/types.service';
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

  typesSharedCollection: ITypes[] = [];
  brandsSharedCollection: IBrand[] = [];

  editForm: FurnitureFormGroup = this.furnitureFormService.createFurnitureFormGroup();

  constructor(
    protected furnitureService: FurnitureService,
    protected furnitureFormService: FurnitureFormService,
    protected typesService: TypesService,
    protected brandService: BrandService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTypes = (o1: ITypes | null, o2: ITypes | null): boolean => this.typesService.compareTypes(o1, o2);

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

    this.typesSharedCollection = this.typesService.addTypesToCollectionIfMissing<ITypes>(this.typesSharedCollection, furniture.type);
    this.brandsSharedCollection = this.brandService.addBrandToCollectionIfMissing<IBrand>(this.brandsSharedCollection, furniture.brand);
  }

  protected loadRelationshipsOptions(): void {
    this.typesService
      .query()
      .pipe(map((res: HttpResponse<ITypes[]>) => res.body ?? []))
      .pipe(map((types: ITypes[]) => this.typesService.addTypesToCollectionIfMissing<ITypes>(types, this.furniture?.type)))
      .subscribe((types: ITypes[]) => (this.typesSharedCollection = types));

    this.brandService
      .query()
      .pipe(map((res: HttpResponse<IBrand[]>) => res.body ?? []))
      .pipe(map((brands: IBrand[]) => this.brandService.addBrandToCollectionIfMissing<IBrand>(brands, this.furniture?.brand)))
      .subscribe((brands: IBrand[]) => (this.brandsSharedCollection = brands));
  }
}
