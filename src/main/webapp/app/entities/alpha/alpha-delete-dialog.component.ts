import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAlpha } from 'app/shared/model/alpha.model';
import { AlphaService } from './alpha.service';

@Component({
  templateUrl: './alpha-delete-dialog.component.html'
})
export class AlphaDeleteDialogComponent {
  alpha?: IAlpha;

  constructor(protected alphaService: AlphaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alphaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('alphaListModification');
      this.activeModal.close();
    });
  }
}
