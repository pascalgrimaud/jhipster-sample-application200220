import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IToto } from 'app/shared/model/toto.model';

@Component({
  selector: 'jhi-toto-detail',
  templateUrl: './toto-detail.component.html'
})
export class TotoDetailComponent implements OnInit {
  toto: IToto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ toto }) => (this.toto = toto));
  }

  previousState(): void {
    window.history.back();
  }
}
