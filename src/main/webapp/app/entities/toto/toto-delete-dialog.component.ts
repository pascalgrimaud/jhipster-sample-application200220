import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IToto } from 'app/shared/model/toto.model';
import { TotoService } from './toto.service';

@Component({
  templateUrl: './toto-delete-dialog.component.html'
})
export class TotoDeleteDialogComponent {
  toto?: IToto;

  constructor(protected totoService: TotoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.totoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('totoListModification');
      this.activeModal.close();
    });
  }
}
