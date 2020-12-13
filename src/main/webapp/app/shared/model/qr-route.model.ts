import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IQrRoute {
  id?: number;
  description?: string;
  code?: string;
  url?: string;
  enabled?: boolean;
  startDate?: Moment;
  endDate?: Moment;
  user?: IUser;
}

export class QrRoute implements IQrRoute {
  constructor(
    public id?: number,
    public description?: string,
    public code?: string,
    public url?: string,
    public enabled?: boolean,
    public startDate?: Moment,
    public endDate?: Moment,
    public user?: IUser
  ) {
    this.enabled = this.enabled || false;
  }
}
