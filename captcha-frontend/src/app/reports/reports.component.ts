import { CommonModule } from '@angular/common';
import { Component, OnInit} from '@angular/core';
import { Captcha } from '../model/captcha';
import { CaptchaService } from '../service/captcha.service';
import * as parser from "../../parser/parser";

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.css'
})
export class ReportsComponent implements OnInit{
  captchas: Captcha[] = [];

  constructor(private captchaService: CaptchaService){

  }

  ngOnInit():void{
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
}
