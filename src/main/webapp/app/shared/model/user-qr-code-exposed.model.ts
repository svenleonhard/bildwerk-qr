export interface IUserQrCodeExposed {
  id?: number;
  code?: string;
  url?: string;
}

export class UserQrCodeExposed implements IUserQrCodeExposed {
  constructor(public id?: number, public code?: string, public url?: string) {}
}
