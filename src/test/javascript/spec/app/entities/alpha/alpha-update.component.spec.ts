import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TotoTestModule } from '../../../test.module';
import { AlphaUpdateComponent } from 'app/entities/alpha/alpha-update.component';
import { AlphaService } from 'app/entities/alpha/alpha.service';
import { Alpha } from 'app/shared/model/alpha.model';

describe('Component Tests', () => {
  describe('Alpha Management Update Component', () => {
    let comp: AlphaUpdateComponent;
    let fixture: ComponentFixture<AlphaUpdateComponent>;
    let service: AlphaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TotoTestModule],
        declarations: [AlphaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AlphaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AlphaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AlphaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Alpha(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Alpha();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
