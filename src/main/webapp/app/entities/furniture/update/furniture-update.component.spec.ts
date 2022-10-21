import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FurnitureFormService } from './furniture-form.service';
import { FurnitureService } from '../service/furniture.service';
import { IFurniture } from '../furniture.model';
import { ITypes } from 'app/entities/types/types.model';
import { TypesService } from 'app/entities/types/service/types.service';
import { IBrand } from 'app/entities/brand/brand.model';
import { BrandService } from 'app/entities/brand/service/brand.service';

import { FurnitureUpdateComponent } from './furniture-update.component';

describe('Furniture Management Update Component', () => {
  let comp: FurnitureUpdateComponent;
  let fixture: ComponentFixture<FurnitureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let furnitureFormService: FurnitureFormService;
  let furnitureService: FurnitureService;
  let typesService: TypesService;
  let brandService: BrandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FurnitureUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FurnitureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FurnitureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    furnitureFormService = TestBed.inject(FurnitureFormService);
    furnitureService = TestBed.inject(FurnitureService);
    typesService = TestBed.inject(TypesService);
    brandService = TestBed.inject(BrandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Types query and add missing value', () => {
      const furniture: IFurniture = { id: 456 };
      const type: ITypes = { id: 84036 };
      furniture.type = type;

      const typesCollection: ITypes[] = [{ id: 86785 }];
      jest.spyOn(typesService, 'query').mockReturnValue(of(new HttpResponse({ body: typesCollection })));
      const additionalTypes = [type];
      const expectedCollection: ITypes[] = [...additionalTypes, ...typesCollection];
      jest.spyOn(typesService, 'addTypesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ furniture });
      comp.ngOnInit();

      expect(typesService.query).toHaveBeenCalled();
      expect(typesService.addTypesToCollectionIfMissing).toHaveBeenCalledWith(
        typesCollection,
        ...additionalTypes.map(expect.objectContaining)
      );
      expect(comp.typesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Brand query and add missing value', () => {
      const furniture: IFurniture = { id: 456 };
      const brand: IBrand = { id: 22807 };
      furniture.brand = brand;

      const brandCollection: IBrand[] = [{ id: 81079 }];
      jest.spyOn(brandService, 'query').mockReturnValue(of(new HttpResponse({ body: brandCollection })));
      const additionalBrands = [brand];
      const expectedCollection: IBrand[] = [...additionalBrands, ...brandCollection];
      jest.spyOn(brandService, 'addBrandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ furniture });
      comp.ngOnInit();

      expect(brandService.query).toHaveBeenCalled();
      expect(brandService.addBrandToCollectionIfMissing).toHaveBeenCalledWith(
        brandCollection,
        ...additionalBrands.map(expect.objectContaining)
      );
      expect(comp.brandsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const furniture: IFurniture = { id: 456 };
      const type: ITypes = { id: 29297 };
      furniture.type = type;
      const brand: IBrand = { id: 63729 };
      furniture.brand = brand;

      activatedRoute.data = of({ furniture });
      comp.ngOnInit();

      expect(comp.typesSharedCollection).toContain(type);
      expect(comp.brandsSharedCollection).toContain(brand);
      expect(comp.furniture).toEqual(furniture);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFurniture>>();
      const furniture = { id: 123 };
      jest.spyOn(furnitureFormService, 'getFurniture').mockReturnValue(furniture);
      jest.spyOn(furnitureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ furniture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: furniture }));
      saveSubject.complete();

      // THEN
      expect(furnitureFormService.getFurniture).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(furnitureService.update).toHaveBeenCalledWith(expect.objectContaining(furniture));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFurniture>>();
      const furniture = { id: 123 };
      jest.spyOn(furnitureFormService, 'getFurniture').mockReturnValue({ id: null });
      jest.spyOn(furnitureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ furniture: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: furniture }));
      saveSubject.complete();

      // THEN
      expect(furnitureFormService.getFurniture).toHaveBeenCalled();
      expect(furnitureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFurniture>>();
      const furniture = { id: 123 };
      jest.spyOn(furnitureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ furniture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(furnitureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypes', () => {
      it('Should forward to typesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typesService, 'compareTypes');
        comp.compareTypes(entity, entity2);
        expect(typesService.compareTypes).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBrand', () => {
      it('Should forward to brandService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(brandService, 'compareBrand');
        comp.compareBrand(entity, entity2);
        expect(brandService.compareBrand).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
