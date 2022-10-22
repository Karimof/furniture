import { Installation } from 'app/entities/enumerations/installation.model';

import { IFurniture, NewFurniture } from './furniture.model';

export const sampleWithRequiredData: IFurniture = {
  id: 7605,
};

export const sampleWithPartialData: IFurniture = {
  id: 26721,
  photo: 'virtual Tasty',
  image: 'Supervisor Digitized',
  name: 'alarm Investment',
  installation: Installation['VERTICAL'],
};

export const sampleWithFullData: IFurniture = {
  id: 99110,
  photo: 'e-enable',
  image: 'IB',
  name: 'Senior Towels',
  installation: Installation['HORIZONTAL'],
};

export const sampleWithNewData: NewFurniture = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
