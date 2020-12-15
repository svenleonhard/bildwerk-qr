import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BildwerkQrTestModule } from '../../../test.module';
import { UserQrCodeExposedComponent } from 'app/entities/user-qr-code-exposed/user-qr-code-exposed.component';
import { UserQrCodeExposedService } from 'app/entities/user-qr-code-exposed/user-qr-code-exposed.service';
import { UserQrCodeExposed } from 'app/shared/model/user-qr-code-exposed.model';

describe('Component Tests', () => {
  describe('UserQrCodeExposed Management Component', () => {
    let comp: UserQrCodeExposedComponent;
    let fixture: ComponentFixture<UserQrCodeExposedComponent>;
    let service: UserQrCodeExposedService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BildwerkQrTestModule],
        declarations: [UserQrCodeExposedComponent],
      })
        .overrideTemplate(UserQrCodeExposedComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserQrCodeExposedComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserQrCodeExposedService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new UserQrCodeExposed(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.userQrCodeExposeds && comp.userQrCodeExposeds[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
