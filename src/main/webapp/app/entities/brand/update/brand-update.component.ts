import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize, map} from 'rxjs/operators';

import {BrandFormGroup, BrandFormService} from './brand-form.service';
import {IBrand} from '../brand.model';
import {BrandService} from '../service/brand.service';
import {IType} from 'app/entities/type/type.model';
import {TypeService} from 'app/entities/type/service/type.service';

@Component({
  selector: 'jhi-brand-update',
  templateUrl: './brand-update.component.html',
})
export class BrandUpdateComponent implements OnInit {
  isSaving = false;
  brand: IBrand | null = null;

  typesSharedCollection: IType[] = [];

  editForm: BrandFormGroup = this.brandFormService.createBrandFormGroup();

  constructor(
    protected brandService: BrandService,
    protected brandFormService: BrandFormService,
    protected typeService: TypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareType = (o1: IType | null, o2: IType | null): boolean => this.typeService.compareType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ brand }) => {
      this.brand = brand;
      if (brand) {
        this.updateForm(brand);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const brand = this.brandFormService.getBrand(this.editForm);
    if (brand.id !== null) {
      this.subscribeToSaveResponse(this.brandService.update(brand));
    } else {
      this.subscribeToSaveResponse(this.brandService.create(brand));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBrand>>): void {
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

  protected updateForm(brand: IBrand): void {
    this.brand = brand;
    this.brandFormService.resetForm(this.editForm, brand);

    this.typesSharedCollection = this.typeService.addTypeToCollectionIfMissing<IType>(this.typesSharedCollection, brand.type);
  }

  protected loadRelationshipsOptions(): void {
    this.typeService
      .query()
      .pipe(map((res: HttpResponse<IType[]>) => res.body ?? []))
      .pipe(map((types: IType[]) => this.typeService.addTypeToCollectionIfMissing<IType>(types, this.brand?.type)))
      .subscribe((types: IType[]) => (this.typesSharedCollection = types));
  }
}
