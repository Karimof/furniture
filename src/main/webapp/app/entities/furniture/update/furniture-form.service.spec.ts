import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../furniture.test-samples';

import { FurnitureFormService } from './furniture-form.service';

describe('Furniture Form Service', () => {
  let service: FurnitureFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FurnitureFormService);
  });

  describe('Service methods', () => {
    describe('createFurnitureFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFurnitureFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            photo: expect.any(Object),
            image: expect.any(Object),
            name: expect.any(Object),
            installation: expect.any(Object),
            brand: expect.any(Object),
          })
        );
      });

      it('passing IFurniture should create a new form with FormGroup', () => {
        const formGroup = service.createFurnitureFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            photo: expect.any(Object),
            image: expect.any(Object),
            name: expect.any(Object),
            installation: expect.any(Object),
            brand: expect.any(Object),
          })
        );
      });
    });

    describe('getFurniture', () => {
      it('should return NewFurniture for default Furniture initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFurnitureFormGroup(sampleWithNewData);

        const furniture = service.getFurniture(formGroup) as any;

        expect(furniture).toMatchObject(sampleWithNewData);
      });

      it('should return NewFurniture for empty Furniture initial value', () => {
        const formGroup = service.createFurnitureFormGroup();

        const furniture = service.getFurniture(formGroup) as any;

        expect(furniture).toMatchObject({});
      });

      it('should return IFurniture', () => {
        const formGroup = service.createFurnitureFormGroup(sampleWithRequiredData);

        const furniture = service.getFurniture(formGroup) as any;

        expect(furniture).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFurniture should not enable id FormControl', () => {
        const formGroup = service.createFurnitureFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFurniture should disable id FormControl', () => {
        const formGroup = service.createFurnitureFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
