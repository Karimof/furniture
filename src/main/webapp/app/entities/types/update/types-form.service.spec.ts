import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../types.test-samples';

import { TypesFormService } from './types-form.service';

describe('Types Form Service', () => {
  let service: TypesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypesFormService);
  });

  describe('Service methods', () => {
    describe('createTypesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing ITypes should create a new form with FormGroup', () => {
        const formGroup = service.createTypesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getTypes', () => {
      it('should return NewTypes for default Types initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTypesFormGroup(sampleWithNewData);

        const types = service.getTypes(formGroup) as any;

        expect(types).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypes for empty Types initial value', () => {
        const formGroup = service.createTypesFormGroup();

        const types = service.getTypes(formGroup) as any;

        expect(types).toMatchObject({});
      });

      it('should return ITypes', () => {
        const formGroup = service.createTypesFormGroup(sampleWithRequiredData);

        const types = service.getTypes(formGroup) as any;

        expect(types).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypes should not enable id FormControl', () => {
        const formGroup = service.createTypesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypes should disable id FormControl', () => {
        const formGroup = service.createTypesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
