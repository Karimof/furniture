import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FurnitureDetailComponent } from './furniture-detail.component';

describe('Furniture Management Detail Component', () => {
  let comp: FurnitureDetailComponent;
  let fixture: ComponentFixture<FurnitureDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FurnitureDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ furniture: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FurnitureDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FurnitureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load furniture on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.furniture).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
