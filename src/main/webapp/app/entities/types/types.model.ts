export interface ITypes {
  id: number;
  name?: string | null;
}

export type NewTypes = Omit<ITypes, 'id'> & { id: null };
