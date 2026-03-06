import { Routes } from '@angular/router';
import { Eventos } from './componentes/eventos/eventos';

export const routes: Routes = [
  { path: 'eventos', component: Eventos },
  { path: '', redirectTo: 'eventos', pathMatch: 'full' }
];