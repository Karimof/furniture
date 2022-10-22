export interface IType {
  id: number;
  name?: string | null;
}

export type NewType = Omit<IType, 'id'> & { id: null };
