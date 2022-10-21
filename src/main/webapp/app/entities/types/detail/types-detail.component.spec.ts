import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypesDetailComponent } from './types-detail.component';

describe('Types Management Detail Component', () => {
  let comp: TypesDetailComponent;
  let fixture: ComponentFixture<TypesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TypesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ types: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TypesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load types on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.types).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
