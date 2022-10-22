import {IBrand, NewBrand} from './brand.model';

export const sampleWithRequiredData: IBrand = {
  id: 69581,
};

export const sampleWithPartialData: IBrand = {
  id: 92003,
  name: 'Assistant District synthesize',
};

export const sampleWithFullData: IBrand = {
  id: 79302,
  name: 'mission-critical Pataca Metal',
};

export const sampleWithNewData: NewBrand = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
