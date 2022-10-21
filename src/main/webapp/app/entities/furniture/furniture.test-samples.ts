import { Installation } from 'app/entities/enumerations/installation.model';

import { IFurniture, NewFurniture } from './furniture.model';

export const sampleWithRequiredData: IFurniture = {
  id: 7605,
  photo: 'reboot hybrid knowledge',
  image: 'seize Shoes Cheese',
  name: 'payment Avon Specialist',
  installation: Installation['HORIZONTAL'],
};

export const sampleWithPartialData: IFurniture = {
  id: 69279,
  photo: 'IB',
  image: 'Senior Towels',
  name: 'transmitting CSS Computers',
  installation: Installation['VERTICAL'],
};

export const sampleWithFullData: IFurniture = {
  id: 8030,
  photo: 'Forward copy background',
  image: 'orange District Steel',
  name: 'Cambridgeshire SSL action-items',
  installation: Installation['HORIZONTAL'],
};

export const sampleWithNewData: NewFurniture = {
  photo: 'Trinidad',
  image: 'Georgia Account',
  name: 'Dollar Computers Inverse',
  installation: Installation['VERTICAL'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
