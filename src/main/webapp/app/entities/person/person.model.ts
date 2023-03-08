import dayjs from 'dayjs/esm';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IPerson {
  id: number;
  name?: string | null;
  img?: string | null;
  gender?: Gender | null;
  born?: dayjs.Dayjs | null;
  death?: dayjs.Dayjs | null;
  country?: string | null;
  nationality?: string | null;
  father?: Pick<IPerson, 'id'> | null;
  mother?: Pick<IPerson, 'id'> | null;
  people?: Pick<IPerson, 'id' | 'name'>[] | null;
  spouses?: Pick<IPerson, 'id'>[] | null;
  divorcedPeople?: Pick<IPerson, 'id' | 'name'>[] | null;
  divorcees?: Pick<IPerson, 'id'>[] | null;
}

export type NewPerson = Omit<IPerson, 'id'> & { id: null };
