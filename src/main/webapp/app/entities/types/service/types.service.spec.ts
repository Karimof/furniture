import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypes } from '../types.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../types.test-samples';

import { TypesService } from './types.service';

const requireRestSample: ITypes = {
  ...sampleWithRequiredData,
};

describe('Types Service', () => {
  let service: TypesService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypes | ITypes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypesService);
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

    it('should create a Types', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const types = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(types).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Types', () => {
      const types = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(types).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Types', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Types', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Types', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypesToCollectionIfMissing', () => {
      it('should add a Types to an empty array', () => {
        const types: ITypes = sampleWithRequiredData;
        expectedResult = service.addTypesToCollectionIfMissing([], types);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(types);
      });

      it('should not add a Types to an array that contains it', () => {
        const types: ITypes = sampleWithRequiredData;
        const typesCollection: ITypes[] = [
          {
            ...types,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypesToCollectionIfMissing(typesCollection, types);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Types to an array that doesn't contain it", () => {
        const types: ITypes = sampleWithRequiredData;
        const typesCollection: ITypes[] = [sampleWithPartialData];
        expectedResult = service.addTypesToCollectionIfMissing(typesCollection, types);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(types);
      });

      it('should add only unique Types to an array', () => {
        const typesArray: ITypes[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typesCollection: ITypes[] = [sampleWithRequiredData];
        expectedResult = service.addTypesToCollectionIfMissing(typesCollection, ...typesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const types: ITypes = sampleWithRequiredData;
        const types2: ITypes = sampleWithPartialData;
        expectedResult = service.addTypesToCollectionIfMissing([], types, types2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(types);
        expect(expectedResult).toContain(types2);
      });

      it('should accept null and undefined values', () => {
        const types: ITypes = sampleWithRequiredData;
        expectedResult = service.addTypesToCollectionIfMissing([], null, types, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(types);
      });

      it('should return initial array if no Types is added', () => {
        const typesCollection: ITypes[] = [sampleWithRequiredData];
        expectedResult = service.addTypesToCollectionIfMissing(typesCollection, undefined, null);
        expect(expectedResult).toEqual(typesCollection);
      });
    });

    describe('compareTypes', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypes(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypes(entity1, entity2);
        const compareResult2 = service.compareTypes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypes(entity1, entity2);
        const compareResult2 = service.compareTypes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypes(entity1, entity2);
        const compareResult2 = service.compareTypes(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
