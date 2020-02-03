import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TotoSharedModule } from 'app/shared/shared.module';
import { AlphaComponent } from './alpha.component';
import { AlphaDetailComponent } from './alpha-detail.component';
import { AlphaUpdateComponent } from './alpha-update.component';
import { AlphaDeleteDialogComponent } from './alpha-delete-dialog.component';
import { alphaRoute } from './alpha.route';

@NgModule({
  imports: [TotoSharedModule, RouterModule.forChild(alphaRoute)],
  declarations: [AlphaComponent, AlphaDetailComponent, AlphaUpdateComponent, AlphaDeleteDialogComponent],
  entryComponents: [AlphaDeleteDialogComponent]
})
export class TotoAlphaModule {}
