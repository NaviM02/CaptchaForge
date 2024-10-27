import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { CaptchaService } from '../service/captcha.service';
import * as parser from "../../parser/parser";
import { Captcha } from '../model/captcha';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-table',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './table.component.html',
  styleUrl: './table.component.css'
})

export class TableComponent implements OnInit{
  captchas: any[] = [];
  constructor(private captchaService: CaptchaService){

  }
  ngOnInit(): void {
    this.captchaService.getCaptchas()
      .subscribe({
        next: (text) => {
          const captchas = parser.parse(text);
          this.captchas = captchas;
        },
        error: (err) => {
          console.error('Error al obtener los captchas:', err);
        }
      });
  }

  navigateToHtml(captcha: Captcha) {
    
  }
}
