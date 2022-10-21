import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFurniture } from '../furniture.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../furniture.test-samples';

import { FurnitureService } from './furniture.service';

const requireRestSample: IFurniture = {
  ...sampleWithRequiredData,
};

describe('Furniture Service', () => {
  let service: FurnitureService;
  let httpMock: HttpTestingController;
  let expectedResult: IFurniture | IFurniture[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FurnitureService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Furniture', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const furniture = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(furniture).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Furniture', () => {
      const furniture = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(furniture).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Furniture', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Furniture', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Furniture', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFurnitureToCollectionIfMissing', () => {
      it('should add a Furniture to an empty array', () => {
        const furniture: IFurniture = sampleWithRequiredData;
        expectedResult = service.addFurnitureToCollectionIfMissing([], furniture);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(furniture);
      });

      it('should not add a Furniture to an array that contains it', () => {
        const furniture: IFurniture = sampleWithRequiredData;
        const furnitureCollection: IFurniture[] = [
          {
            ...furniture,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFurnitureToCollectionIfMissing(furnitureCollection, furniture);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Furniture to an array that doesn't contain it", () => {
        const furniture: IFurniture = sampleWithRequiredData;
        const furnitureCollection: IFurniture[] = [sampleWithPartialData];
        expectedResult = service.addFurnitureToCollectionIfMissing(furnitureCollection, furniture);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(furniture);
      });

      it('should add only unique Furniture to an array', () => {
        const furnitureArray: IFurniture[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const furnitureCollection: IFurniture[] = [sampleWithRequiredData];
        expectedResult = service.addFurnitureToCollectionIfMissing(furnitureCollection, ...furnitureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const furniture: IFurniture = sampleWithRequiredData;
        const furniture2: IFurniture = sampleWithPartialData;
        expectedResult = service.addFurnitureToCollectionIfMissing([], furniture, furniture2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(furniture);
        expect(expectedResult).toContain(furniture2);
      });

      it('should accept null and undefined values', () => {
        const furniture: IFurniture = sampleWithRequiredData;
        expectedResult = service.addFurnitureToCollectionIfMissing([], null, furniture, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(furniture);
      });

      it('should return initial array if no Furniture is added', () => {
        const furnitureCollection: IFurniture[] = [sampleWithRequiredData];
        expectedResult = service.addFurnitureToCollectionIfMissing(furnitureCollection, undefined, null);
        expect(expectedResult).toEqual(furnitureCollection);
      });
    });

    describe('compareFurniture', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFurniture(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFurniture(entity1, entity2);
        const compareResult2 = service.compareFurniture(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFurniture(entity1, entity2);
        const compareResult2 = service.compareFurniture(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFurniture(entity1, entity2);
        const compareResult2 = service.compareFurniture(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
