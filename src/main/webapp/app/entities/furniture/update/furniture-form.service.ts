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
      photo: new FormControl(furnitureRawValue.photo),
      image: new FormControl(furnitureRawValue.image),
      name: new FormControl(furnitureRawValue.name),
      installation: new FormControl(furnitureRawValue.installation),
      brand: new FormControl(furnitureRawValue.brand, {
        validators: [Validators.required],
      }),
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
