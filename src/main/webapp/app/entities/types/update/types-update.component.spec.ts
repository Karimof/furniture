import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TypesFormService } from './types-form.service';
import { TypesService } from '../service/types.service';
import { ITypes } from '../types.model';

import { TypesUpdateComponent } from './types-update.component';

describe('Types Management Update Component', () => {
  let comp: TypesUpdateComponent;
  let fixture: ComponentFixture<TypesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typesFormService: TypesFormService;
  let typesService: TypesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TypesUpdateComponent],
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
      .overrideTemplate(TypesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typesFormService = TestBed.inject(TypesFormService);
    typesService = TestBed.inject(TypesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const types: ITypes = { id: 456 };

      activatedRoute.data = of({ types });
      comp.ngOnInit();

      expect(comp.types).toEqual(types);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypes>>();
      const types = { id: 123 };
      jest.spyOn(typesFormService, 'getTypes').mockReturnValue(types);
      jest.spyOn(typesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ types });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: types }));
      saveSubject.complete();

      // THEN
      expect(typesFormService.getTypes).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typesService.update).toHaveBeenCalledWith(expect.objectContaining(types));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypes>>();
      const types = { id: 123 };
      jest.spyOn(typesFormService, 'getTypes').mockReturnValue({ id: null });
      jest.spyOn(typesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ types: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: types }));
      saveSubject.complete();

      // THEN
      expect(typesFormService.getTypes).toHaveBeenCalled();
      expect(typesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypes>>();
      const types = { id: 123 };
      jest.spyOn(typesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ types });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
