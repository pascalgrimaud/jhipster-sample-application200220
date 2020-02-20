import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IToto, Toto } from 'app/shared/model/toto.model';
import { TotoService } from './toto.service';

@Component({
  selector: 'jhi-toto-update',
  templateUrl: './toto-update.component.html'
})
export class TotoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: []
  });

  constructor(protected totoService: TotoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ toto }) => {
      this.updateForm(toto);
    });
  }

  updateForm(toto: IToto): void {
    this.editForm.patchValue({
      id: toto.id,
      name: toto.name
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const toto = this.createFromForm();
    if (toto.id !== undefined) {
      this.subscribeToSaveResponse(this.totoService.update(toto));
    } else {
      this.subscribeToSaveResponse(this.totoService.create(toto));
    }
  }

  private createFromForm(): IToto {
    return {
      ...new Toto(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IToto>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
