import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BildwerkQrTestModule } from '../../../test.module';
import { UserQrCodeExposedDetailComponent } from 'app/entities/user-qr-code-exposed/user-qr-code-exposed-detail.component';
import { UserQrCodeExposed } from 'app/shared/model/user-qr-code-exposed.model';

describe('Component Tests', () => {
  describe('UserQrCodeExposed Management Detail Component', () => {
    let comp: UserQrCodeExposedDetailComponent;
    let fixture: ComponentFixture<UserQrCodeExposedDetailComponent>;
    const route = ({ data: of({ userQrCodeExposed: new UserQrCodeExposed(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BildwerkQrTestModule],
        declarations: [UserQrCodeExposedDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(UserQrCodeExposedDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserQrCodeExposedDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load userQrCodeExposed on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userQrCodeExposed).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
