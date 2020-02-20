import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IToto } from 'app/shared/model/toto.model';
import { TotoService } from './toto.service';
import { TotoDeleteDialogComponent } from './toto-delete-dialog.component';

@Component({
  selector: 'jhi-toto',
  templateUrl: './toto.component.html'
})
export class TotoComponent implements OnInit, OnDestroy {
  totos?: IToto[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected totoService: TotoService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.totoService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IToto[]>) => (this.totos = res.body || []));
      return;
    }

    this.totoService.query().subscribe((res: HttpResponse<IToto[]>) => (this.totos = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTotos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IToto): string {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTotos(): void {
    this.eventSubscriber = this.eventManager.subscribe('totoListModification', () => this.loadAll());
  }

  delete(toto: IToto): void {
    const modalRef = this.modalService.open(TotoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.toto = toto;
  }
}
