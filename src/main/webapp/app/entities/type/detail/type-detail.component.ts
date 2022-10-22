import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IType } from '../type.model';

@Component({
  selector: 'jhi-type-detail',
  templateUrl: './type-detail.component.html',
})
export class TypeDetailComponent implements OnInit {
  type: IType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ type }) => {
      this.type = type;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
