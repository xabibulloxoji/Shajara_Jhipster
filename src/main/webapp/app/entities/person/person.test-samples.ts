import dayjs from 'dayjs/esm';

import { Gender } from 'app/entities/enumerations/gender.model';

import { IPerson, NewPerson } from './person.model';

export const sampleWithRequiredData: IPerson = {
  id: 31094,
};

export const sampleWithPartialData: IPerson = {
  id: 14129,
  divorced: 'blue',
  name: 'Fantastic',
  gender: Gender['MALE'],
  death: dayjs('2023-03-06T14:11'),
  country: 'New Caledonia',
};

export const sampleWithFullData: IPerson = {
  id: 36050,
  divorced: 'concept',
  name: 'Dominican',
  img: 'capacitor Sleek Kwacha',
  gender: Gender['FEMALE'],
  born: dayjs('2023-03-07T01:38'),
  death: dayjs('2023-03-06T21:18'),
  country: 'Botswana',
  nationality: 'Product',
};

export const sampleWithNewData: NewPerson = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
