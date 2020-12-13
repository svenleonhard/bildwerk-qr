import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BildwerkQrTestModule } from '../../../test.module';
import { QrRouteDetailComponent } from 'app/entities/qr-route/qr-route-detail.component';
import { QrRoute } from 'app/shared/model/qr-route.model';

describe('Component Tests', () => {
  describe('QrRoute Management Detail Component', () => {
    let comp: QrRouteDetailComponent;
    let fixture: ComponentFixture<QrRouteDetailComponent>;
    const route = ({ data: of({ qrRoute: new QrRoute(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BildwerkQrTestModule],
        declarations: [QrRouteDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(QrRouteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(QrRouteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load qrRoute on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.qrRoute).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
