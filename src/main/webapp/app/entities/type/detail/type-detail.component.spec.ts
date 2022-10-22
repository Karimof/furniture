import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypeDetailComponent } from './type-detail.component';

describe('Type Management Detail Component', () => {
  let comp: TypeDetailComponent;
  let fixture: ComponentFixture<TypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ type: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load type on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.type).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
