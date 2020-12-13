import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BildwerkQrTestModule } from '../../../test.module';
import { UserQrCodeComponent } from 'app/entities/user-qr-code/user-qr-code.component';
import { UserQrCodeService } from 'app/entities/user-qr-code/user-qr-code.service';
import { UserQrCode } from 'app/shared/model/user-qr-code.model';

describe('Component Tests', () => {
  describe('UserQrCode Management Component', () => {
    let comp: UserQrCodeComponent;
    let fixture: ComponentFixture<UserQrCodeComponent>;
    let service: UserQrCodeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BildwerkQrTestModule],
        declarations: [UserQrCodeComponent],
      })
        .overrideTemplate(UserQrCodeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserQrCodeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserQrCodeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new UserQrCode(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.userQrCodes && comp.userQrCodes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
