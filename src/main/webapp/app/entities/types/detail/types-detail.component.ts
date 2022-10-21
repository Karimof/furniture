import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypes } from '../types.model';

@Component({
  selector: 'jhi-types-detail',
  templateUrl: './types-detail.component.html',
})
export class TypesDetailComponent implements OnInit {
  types: ITypes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ types }) => {
      this.types = types;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
