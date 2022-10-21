import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TypesFormService, TypesFormGroup } from './types-form.service';
import { ITypes } from '../types.model';
import { TypesService } from '../service/types.service';

@Component({
  selector: 'jhi-types-update',
  templateUrl: './types-update.component.html',
})
export class TypesUpdateComponent implements OnInit {
  isSaving = false;
  types: ITypes | null = null;

  editForm: TypesFormGroup = this.typesFormService.createTypesFormGroup();

  constructor(
    protected typesService: TypesService,
    protected typesFormService: TypesFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ types }) => {
      this.types = types;
      if (types) {
        this.updateForm(types);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const types = this.typesFormService.getTypes(this.editForm);
    if (types.id !== null) {
      this.subscribeToSaveResponse(this.typesService.update(types));
    } else {
      this.subscribeToSaveResponse(this.typesService.create(types));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypes>>): void {
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

  protected updateForm(types: ITypes): void {
    this.types = types;
    this.typesFormService.resetForm(this.editForm, types);
  }
}
