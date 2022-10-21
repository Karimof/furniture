import { ITypes, NewTypes } from './types.model';

export const sampleWithRequiredData: ITypes = {
  id: 73468,
  name: 'Intelligent virtual',
};

export const sampleWithPartialData: ITypes = {
  id: 30038,
  name: 'Loan International SCSI',
};

export const sampleWithFullData: ITypes = {
  id: 69151,
  name: 'Manager Park',
};

export const sampleWithNewData: NewTypes = {
  name: 'Technician',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
