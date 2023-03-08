import dayjs from 'dayjs/esm';

import { Gender } from 'app/entities/enumerations/gender.model';

import { IPerson, NewPerson } from './person.model';

export const sampleWithRequiredData: IPerson = {
  id: 31094,
};

export const sampleWithPartialData: IPerson = {
  id: 48306,
  name: 'Intelligent',
  img: 'Cotton payment',
  born: dayjs('2023-03-07T01:13'),
  country: 'Georgia',
  nationality: 'Avon',
};

export const sampleWithFullData: IPerson = {
  id: 22115,
  name: 'Developer Program',
  img: 'Wooden Designer',
  gender: Gender['MALE'],
  born: dayjs('2023-03-06T21:18'),
  death: dayjs('2023-03-07T02:40'),
  country: 'Dominica',
  nationality: 'client-driven Phased program',
};

export const sampleWithNewData: NewPerson = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
