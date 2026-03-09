import { Routes } from '@angular/router';
import { Consulta } from './components/consulta/consulta';
import { Formulario } from './components/formulario/formulario';

export const routes: Routes = [
  { path: '', component: Formulario },
  { path: 'consulta', component: Consulta },
  { path: 'formulario', component: Formulario }
];