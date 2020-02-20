import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { TotoComponent } from './toto.component';
import { TotoDetailComponent } from './toto-detail.component';
import { TotoUpdateComponent } from './toto-update.component';
import { TotoDeleteDialogComponent } from './toto-delete-dialog.component';
import { totoRoute } from './toto.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(totoRoute)],
  declarations: [TotoComponent, TotoDetailComponent, TotoUpdateComponent, TotoDeleteDialogComponent],
  entryComponents: [TotoDeleteDialogComponent]
})
export class JhipsterSampleApplicationTotoModule {}
