import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'alpha',
        loadChildren: () => import('./alpha/alpha.module').then(m => m.TotoAlphaModule)
      },
      {
        path: 'project',
        loadChildren: () => import('./project/project.module').then(m => m.TotoProjectModule)
      },
      {
        path: 'label',
        loadChildren: () => import('./label/label.module').then(m => m.TotoLabelModule)
      },
      {
        path: 'ticket',
        loadChildren: () => import('./ticket/ticket.module').then(m => m.TotoTicketModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class TotoEntityModule {}
