import { IBrand } from 'app/entities/brand/brand.model';
import { Installation } from 'app/entities/enumerations/installation.model';

export interface IFurniture {
  id: number;
  photo?: string | null;
  image?: string | null;
  name?: string | null;
  installation?: Installation | null;
  brand?: Pick<IBrand, 'id'> | null;
}

export type NewFurniture = Omit<IFurniture, 'id'> & { id: null };
