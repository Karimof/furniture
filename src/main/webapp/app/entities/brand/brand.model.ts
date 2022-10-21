export interface IBrand {
  id: number;
  name?: string | null;
}

export type NewBrand = Omit<IBrand, 'id'> & { id: null };
