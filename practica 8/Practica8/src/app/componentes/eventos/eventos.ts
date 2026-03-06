import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-eventos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './eventos.html'
})
export class Eventos implements OnInit {

  listaEventos: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<any[]>('http://127.0.0.1:5000/eventos')
      .subscribe(data => {
        console.log("DATOS:", data);
        this.listaEventos = data;
      });
  }
}