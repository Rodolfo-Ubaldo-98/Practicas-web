import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Alumnos } from '../../services/alumnos';

@Component({
  selector: 'app-consulta',
  imports: [CommonModule],
  templateUrl: './consulta.html',
  styleUrl: './consulta.css'
})
export class Consulta implements OnInit {
  listaAlumnos: any[] = [];
  
  constructor(private alumnos: Alumnos) {}

  ngOnInit(): void {
    this.alumnos.obtenerAlumnos().subscribe(data => {
      this.listaAlumnos = data as any[];
    });
  }
}