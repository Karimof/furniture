import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypes, NewTypes } from '../types.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypes for edit and NewTypesFormGroupInput for create.
 */
type TypesFormGroupInput = ITypes | PartialWithRequiredKeyOf<NewTypes>;

type TypesFormDefaults = Pick<NewTypes, 'id'>;

type TypesFormGroupContent = {
  id: FormControl<ITypes['id'] | NewTypes['id']>;
  name: FormControl<ITypes['name']>;
};

export type TypesFormGroup = FormGroup<TypesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypesFormService {
  createTypesFormGroup(types: TypesFormGroupInput = { id: null }): TypesFormGroup {
    const typesRawValue = {
      ...this.getFormDefaults(),
      ...types,
    };
    return new FormGroup<TypesFormGroupContent>({
      id: new FormControl(
        { value: typesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(typesRawValue.name, {
        validators: [Validators.required, Validators.maxLength(32)],
      }),
    });
  }

  getTypes(form: TypesFormGroup): ITypes | NewTypes {
    return form.getRawValue() as ITypes | NewTypes;
  }

  resetForm(form: TypesFormGroup, types: TypesFormGroupInput): void {
    const typesRawValue = { ...this.getFormDefaults(), ...types };
    form.reset(
      {
        ...typesRawValue,
        id: { value: typesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TypesFormDefaults {
    return {
      id: null,
    };
  }
}
