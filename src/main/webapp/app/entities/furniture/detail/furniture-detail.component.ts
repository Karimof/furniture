import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFurniture } from '../furniture.model';

@Component({
  selector: 'jhi-furniture-detail',
  templateUrl: './furniture-detail.component.html',
})
export class FurnitureDetailComponent implements OnInit {
  furniture: IFurniture | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ furniture }) => {
      this.furniture = furniture;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
