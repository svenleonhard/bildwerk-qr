import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BildwerkQrTestModule } from '../../../test.module';
import { UserQrCodeDetailComponent } from 'app/entities/user-qr-code/user-qr-code-detail.component';
import { UserQrCode } from 'app/shared/model/user-qr-code.model';

describe('Component Tests', () => {
  describe('UserQrCode Management Detail Component', () => {
    let comp: UserQrCodeDetailComponent;
    let fixture: ComponentFixture<UserQrCodeDetailComponent>;
    const route = ({ data: of({ userQrCode: new UserQrCode(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BildwerkQrTestModule],
        declarations: [UserQrCodeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(UserQrCodeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserQrCodeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load userQrCode on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userQrCode).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
