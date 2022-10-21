import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypes, NewTypes } from '../types.model';

export type PartialUpdateTypes = Partial<ITypes> & Pick<ITypes, 'id'>;

export type EntityResponseType = HttpResponse<ITypes>;
export type EntityArrayResponseType = HttpResponse<ITypes[]>;

@Injectable({ providedIn: 'root' })
export class TypesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(types: NewTypes): Observable<EntityResponseType> {
    return this.http.post<ITypes>(this.resourceUrl, types, { observe: 'response' });
  }

  update(types: ITypes): Observable<EntityResponseType> {
    return this.http.put<ITypes>(`${this.resourceUrl}/${this.getTypesIdentifier(types)}`, types, { observe: 'response' });
  }

  partialUpdate(types: PartialUpdateTypes): Observable<EntityResponseType> {
    return this.http.patch<ITypes>(`${this.resourceUrl}/${this.getTypesIdentifier(types)}`, types, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypesIdentifier(types: Pick<ITypes, 'id'>): number {
    return types.id;
  }

  compareTypes(o1: Pick<ITypes, 'id'> | null, o2: Pick<ITypes, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypesIdentifier(o1) === this.getTypesIdentifier(o2) : o1 === o2;
  }

  addTypesToCollectionIfMissing<Type extends Pick<ITypes, 'id'>>(
    typesCollection: Type[],
    ...typesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const types: Type[] = typesToCheck.filter(isPresent);
    if (types.length > 0) {
      const typesCollectionIdentifiers = typesCollection.map(typesItem => this.getTypesIdentifier(typesItem)!);
      const typesToAdd = types.filter(typesItem => {
        const typesIdentifier = this.getTypesIdentifier(typesItem);
        if (typesCollectionIdentifiers.includes(typesIdentifier)) {
          return false;
        }
        typesCollectionIdentifiers.push(typesIdentifier);
        return true;
      });
      return [...typesToAdd, ...typesCollection];
    }
    return typesCollection;
  }
}
