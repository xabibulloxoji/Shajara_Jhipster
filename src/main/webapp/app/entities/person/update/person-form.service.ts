import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPerson, NewPerson } from '../person.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPerson for edit and NewPersonFormGroupInput for create.
 */
type PersonFormGroupInput = IPerson | PartialWithRequiredKeyOf<NewPerson>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPerson | NewPerson> = Omit<T, 'born' | 'death'> & {
  born?: string | null;
  death?: string | null;
};

type PersonFormRawValue = FormValueOf<IPerson>;

type NewPersonFormRawValue = FormValueOf<NewPerson>;

type PersonFormDefaults = Pick<NewPerson, 'id' | 'born' | 'death' | 'people' | 'spouses'>;

type PersonFormGroupContent = {
  id: FormControl<PersonFormRawValue['id'] | NewPerson['id']>;
  divorced: FormControl<PersonFormRawValue['divorced']>;
  name: FormControl<PersonFormRawValue['name']>;
  img: FormControl<PersonFormRawValue['img']>;
  gender: FormControl<PersonFormRawValue['gender']>;
  born: FormControl<PersonFormRawValue['born']>;
  death: FormControl<PersonFormRawValue['death']>;
  country: FormControl<PersonFormRawValue['country']>;
  nationality: FormControl<PersonFormRawValue['nationality']>;
  father: FormControl<PersonFormRawValue['father']>;
  mother: FormControl<PersonFormRawValue['mother']>;
  people: FormControl<PersonFormRawValue['people']>;
  spouses: FormControl<PersonFormRawValue['spouses']>;
};

export type PersonFormGroup = FormGroup<PersonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PersonFormService {
  createPersonFormGroup(person: PersonFormGroupInput = { id: null }): PersonFormGroup {
    const personRawValue = this.convertPersonToPersonRawValue({
      ...this.getFormDefaults(),
      ...person,
    });
    return new FormGroup<PersonFormGroupContent>({
      id: new FormControl(
        { value: personRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      divorced: new FormControl(personRawValue.divorced),
      name: new FormControl(personRawValue.name),
      img: new FormControl(personRawValue.img),
      gender: new FormControl(personRawValue.gender),
      born: new FormControl(personRawValue.born),
      death: new FormControl(personRawValue.death),
      country: new FormControl(personRawValue.country),
      nationality: new FormControl(personRawValue.nationality),
      father: new FormControl(personRawValue.father),
      mother: new FormControl(personRawValue.mother),
      people: new FormControl(personRawValue.people ?? []),
      spouses: new FormControl(personRawValue.spouses ?? []),
    });
  }

  getPerson(form: PersonFormGroup): IPerson | NewPerson {
    return this.convertPersonRawValueToPerson(form.getRawValue() as PersonFormRawValue | NewPersonFormRawValue);
  }

  resetForm(form: PersonFormGroup, person: PersonFormGroupInput): void {
    const personRawValue = this.convertPersonToPersonRawValue({ ...this.getFormDefaults(), ...person });
    form.reset(
      {
        ...personRawValue,
        id: { value: personRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PersonFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      born: currentTime,
      death: currentTime,
      people: [],
      spouses: [],
    };
  }

  private convertPersonRawValueToPerson(rawPerson: PersonFormRawValue | NewPersonFormRawValue): IPerson | NewPerson {
    return {
      ...rawPerson,
      born: dayjs(rawPerson.born, DATE_TIME_FORMAT),
      death: dayjs(rawPerson.death, DATE_TIME_FORMAT),
    };
  }

  private convertPersonToPersonRawValue(
    person: IPerson | (Partial<NewPerson> & PersonFormDefaults)
  ): PersonFormRawValue | PartialWithRequiredKeyOf<NewPersonFormRawValue> {
    return {
      ...person,
      born: person.born ? person.born.format(DATE_TIME_FORMAT) : undefined,
      death: person.death ? person.death.format(DATE_TIME_FORMAT) : undefined,
      people: person.people ?? [],
      spouses: person.spouses ?? [],
    };
  }
}
