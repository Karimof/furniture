import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFurniture, NewFurniture } from '../furniture.model';

export type PartialUpdateFurniture = Partial<IFurniture> & Pick<IFurniture, 'id'>;

export type EntityResponseType = HttpResponse<IFurniture>;
export type EntityArrayResponseType = HttpResponse<IFurniture[]>;

@Injectable({ providedIn: 'root' })
export class FurnitureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/furnitures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(furniture: NewFurniture): Observable<EntityResponseType> {
    return this.http.post<IFurniture>(this.resourceUrl, furniture, { observe: 'response' });
  }

  update(furniture: IFurniture): Observable<EntityResponseType> {
    return this.http.put<IFurniture>(`${this.resourceUrl}/${this.getFurnitureIdentifier(furniture)}`, furniture, { observe: 'response' });
  }

  partialUpdate(furniture: PartialUpdateFurniture): Observable<EntityResponseType> {
    return this.http.patch<IFurniture>(`${this.resourceUrl}/${this.getFurnitureIdentifier(furniture)}`, furniture, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFurniture>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFurniture[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFurnitureIdentifier(furniture: Pick<IFurniture, 'id'>): number {
    return furniture.id;
  }

  compareFurniture(o1: Pick<IFurniture, 'id'> | null, o2: Pick<IFurniture, 'id'> | null): boolean {
    return o1 && o2 ? this.getFurnitureIdentifier(o1) === this.getFurnitureIdentifier(o2) : o1 === o2;
  }

  addFurnitureToCollectionIfMissing<Type extends Pick<IFurniture, 'id'>>(
    furnitureCollection: Type[],
    ...furnituresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const furnitures: Type[] = furnituresToCheck.filter(isPresent);
    if (furnitures.length > 0) {
      const furnitureCollectionIdentifiers = furnitureCollection.map(furnitureItem => this.getFurnitureIdentifier(furnitureItem)!);
      const furnituresToAdd = furnitures.filter(furnitureItem => {
        const furnitureIdentifier = this.getFurnitureIdentifier(furnitureItem);
        if (furnitureCollectionIdentifiers.includes(furnitureIdentifier)) {
          return false;
        }
        furnitureCollectionIdentifiers.push(furnitureIdentifier);
        return true;
      });
      return [...furnituresToAdd, ...furnitureCollection];
    }
    return furnitureCollection;
  }
}
