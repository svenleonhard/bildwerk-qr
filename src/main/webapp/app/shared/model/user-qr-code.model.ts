import { IUser } from 'app/core/user/user.model';

export interface IUserQrCode {
  id?: number;
  code?: string;
  user?: IUser;
}

export class UserQrCode implements IUserQrCode {
  constructor(public id?: number, public code?: string, public user?: IUser) {}
}
