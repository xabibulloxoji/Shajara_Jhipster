import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PersonFormService, PersonFormGroup } from './person-form.service';
import { IPerson } from '../person.model';
import { PersonService } from '../service/person.service';
import { Gender } from 'app/entities/enumerations/gender.model';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html',
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;
  person: IPerson | null = null;
  genderValues = Object.keys(Gender);

  peopleSharedCollection: IPerson[] = [];

  editForm: PersonFormGroup = this.personFormService.createPersonFormGroup();

  constructor(
    protected personService: PersonService,
    protected personFormService: PersonFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePerson = (o1: IPerson | null, o2: IPerson | null): boolean => this.personService.comparePerson(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.person = person;
      if (person) {
        this.updateForm(person);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.personFormService.getPerson(this.editForm);
    if (person.id !== null) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(person: IPerson): void {
    this.person = person;
    this.personFormService.resetForm(this.editForm, person);

    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing<IPerson>(
      this.peopleSharedCollection,
      person.father,
      person.mother,
      ...(person.people ?? []),
      ...(person.divorcedPeople ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(
        map((people: IPerson[]) =>
          this.personService.addPersonToCollectionIfMissing<IPerson>(
            people,
            this.person?.father,
            this.person?.mother,
            ...(this.person?.people ?? []),
            ...(this.person?.divorcedPeople ?? [])
          )
        )
      )
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));
  }
}
