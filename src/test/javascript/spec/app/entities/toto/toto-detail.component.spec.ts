import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { TotoDetailComponent } from 'app/entities/toto/toto-detail.component';
import { Toto } from 'app/shared/model/toto.model';

describe('Component Tests', () => {
  describe('Toto Management Detail Component', () => {
    let comp: TotoDetailComponent;
    let fixture: ComponentFixture<TotoDetailComponent>;
    const route = ({ data: of({ toto: new Toto('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [TotoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TotoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TotoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load toto on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.toto).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
