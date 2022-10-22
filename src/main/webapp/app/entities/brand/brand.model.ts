import {IType} from 'app/entities/type/type.model';

export interface IBrand {
  id: number;
  name?: string | null;
  type?: Pick<IType, 'id'> | null;
}

export type NewBrand = Omit<IBrand, 'id'> & { id: null };
