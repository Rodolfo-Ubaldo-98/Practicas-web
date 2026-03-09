import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Alumnos } from '../../services/alumnos';

@Component({
  selector: 'app-formulario',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './formulario.html',
  styleUrl: './formulario.css'
})
export class Formulario {
  constructor(private alumno: Alumnos) {}

  guardarAlumno(form: NgForm) {
    if (form.invalid) return;
    const nuevoAlumno = {
      nombre: form.value.nombre,
      edad: form.value.edad,
      carrera: form.value.carrera
    };
    this.alumno.agregarAlumno(nuevoAlumno).subscribe({
      next: () => {
        alert("Alumno agregado correctamente");
        form.reset();
      },
      error: (err) => {
        console.error("Error al insertar:", err);
      }
    });
  }
}