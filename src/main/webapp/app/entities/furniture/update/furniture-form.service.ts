import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFurniture, NewFurniture } from '../furniture.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFurniture for edit and NewFurnitureFormGroupInput for create.
 */
type FurnitureFormGroupInput = IFurniture | PartialWithRequiredKeyOf<NewFurniture>;

type FurnitureFormDefaults = Pick<NewFurniture, 'id'>;

type FurnitureFormGroupContent = {
  id: FormControl<IFurniture['id'] | NewFurniture['id']>;
  photo: FormControl<IFurniture['photo']>;
  image: FormControl<IFurniture['image']>;
  name: FormControl<IFurniture['name']>;
  installation: FormControl<IFurniture['installation']>;
  type: FormControl<IFurniture['type']>;
  brand: FormControl<IFurniture['brand']>;
};

export type FurnitureFormGroup = FormGroup<FurnitureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FurnitureFormService {
  createFurnitureFormGroup(furniture: FurnitureFormGroupInput = { id: null }): FurnitureFormGroup {
    const furnitureRawValue = {
      ...this.getFormDefaults(),
      ...furniture,
    };
    return new FormGroup<FurnitureFormGroupContent>({
      id: new FormControl(
        { value: furnitureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      photo: new FormControl(furnitureRawValue.photo, {
        validators: [Validators.required],
      }),
      image: new FormControl(furnitureRawValue.image, {
        validators: [Validators.required],
      }),
      name: new FormControl(furnitureRawValue.name, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      installation: new FormControl(furnitureRawValue.installation, {
        validators: [Validators.required],
      }),
      type: new FormControl(furnitureRawValue.type),
      brand: new FormControl(furnitureRawValue.brand),
    });
  }

  getFurniture(form: FurnitureFormGroup): IFurniture | NewFurniture {
    return form.getRawValue() as IFurniture | NewFurniture;
  }

  resetForm(form: FurnitureFormGroup, furniture: FurnitureFormGroupInput): void {
    const furnitureRawValue = { ...this.getFormDefaults(), ...furniture };
    form.reset(
      {
        ...furnitureRawValue,
        id: { value: furnitureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FurnitureFormDefaults {
    return {
      id: null,
    };
  }
}
