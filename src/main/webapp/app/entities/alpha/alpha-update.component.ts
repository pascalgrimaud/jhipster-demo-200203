import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAlpha, Alpha } from 'app/shared/model/alpha.model';
import { AlphaService } from './alpha.service';

@Component({
  selector: 'jhi-alpha-update',
  templateUrl: './alpha-update.component.html'
})
export class AlphaUpdateComponent implements OnInit {
  isSaving = false;
  birthdayDp: any;

  editForm = this.fb.group({
    id: [],
    firstname: [null, [Validators.required, Validators.minLength(5)]],
    lastname: [],
    birthday: [null, [Validators.required]]
  });

  constructor(protected alphaService: AlphaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alpha }) => {
      this.updateForm(alpha);
    });
  }

  updateForm(alpha: IAlpha): void {
    this.editForm.patchValue({
      id: alpha.id,
      firstname: alpha.firstname,
      lastname: alpha.lastname,
      birthday: alpha.birthday
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alpha = this.createFromForm();
    if (alpha.id !== undefined) {
      this.subscribeToSaveResponse(this.alphaService.update(alpha));
    } else {
      this.subscribeToSaveResponse(this.alphaService.create(alpha));
    }
  }

  private createFromForm(): IAlpha {
    return {
      ...new Alpha(),
      id: this.editForm.get(['id'])!.value,
      firstname: this.editForm.get(['firstname'])!.value,
      lastname: this.editForm.get(['lastname'])!.value,
      birthday: this.editForm.get(['birthday'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlpha>>): void {
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
