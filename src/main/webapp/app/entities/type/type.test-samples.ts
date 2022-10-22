import { IType, NewType } from './type.model';

export const sampleWithRequiredData: IType = {
  id: 60387,
};

export const sampleWithPartialData: IType = {
  id: 21401,
  name: 'up Chicken intelligence',
};

export const sampleWithFullData: IType = {
  id: 22348,
  name: 'Credit',
};

export const sampleWithNewData: NewType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
