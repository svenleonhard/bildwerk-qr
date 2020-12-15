export interface IUserQrCodeExposed {
  id?: number;
  code?: string;
}

export class UserQrCodeExposed implements IUserQrCodeExposed {
  constructor(public id?: number, public code?: string) {}
}
