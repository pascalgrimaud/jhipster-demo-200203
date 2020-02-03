import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAlpha } from 'app/shared/model/alpha.model';

@Component({
  selector: 'jhi-alpha-detail',
  templateUrl: './alpha-detail.component.html'
})
export class AlphaDetailComponent implements OnInit {
  alpha: IAlpha | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alpha }) => {
      this.alpha = alpha;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
