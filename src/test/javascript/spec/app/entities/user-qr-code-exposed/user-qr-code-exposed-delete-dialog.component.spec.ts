import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BildwerkQrTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { UserQrCodeExposedDeleteDialogComponent } from 'app/entities/user-qr-code-exposed/user-qr-code-exposed-delete-dialog.component';
import { UserQrCodeExposedService } from 'app/entities/user-qr-code-exposed/user-qr-code-exposed.service';

describe('Component Tests', () => {
  describe('UserQrCodeExposed Management Delete Component', () => {
    let comp: UserQrCodeExposedDeleteDialogComponent;
    let fixture: ComponentFixture<UserQrCodeExposedDeleteDialogComponent>;
    let service: UserQrCodeExposedService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BildwerkQrTestModule],
        declarations: [UserQrCodeExposedDeleteDialogComponent],
      })
        .overrideTemplate(UserQrCodeExposedDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserQrCodeExposedDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserQrCodeExposedService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
