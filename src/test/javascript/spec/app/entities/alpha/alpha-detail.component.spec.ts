import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TotoTestModule } from '../../../test.module';
import { AlphaDetailComponent } from 'app/entities/alpha/alpha-detail.component';
import { Alpha } from 'app/shared/model/alpha.model';

describe('Component Tests', () => {
  describe('Alpha Management Detail Component', () => {
    let comp: AlphaDetailComponent;
    let fixture: ComponentFixture<AlphaDetailComponent>;
    const route = ({ data: of({ alpha: new Alpha(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TotoTestModule],
        declarations: [AlphaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AlphaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AlphaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load alpha on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.alpha).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
