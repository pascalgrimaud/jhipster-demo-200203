import { Moment } from 'moment';

export interface IAlpha {
  id?: number;
  firstname?: string;
  lastname?: string;
  birthday?: Moment;
}

export class Alpha implements IAlpha {
  constructor(public id?: number, public firstname?: string, public lastname?: string, public birthday?: Moment) {}
}
