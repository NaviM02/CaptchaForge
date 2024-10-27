import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CaptchaService } from '../../service/captcha.service';
import * as parser from "../../../parser/parser";
import { Captcha } from '../../model/captcha';
import { SafeHtml } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-captcha-display',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './captcha-display.component.html',
  styleUrl: './captcha-display.component.css'
})
export class CaptchaDisplayComponent implements OnInit, OnDestroy{
  captchaId: string = "";
  captcha: any;
  url: string = "";
  showReport: boolean = false;
  public htmlContent: SafeHtml = '';
  constructor(
    private route: ActivatedRoute,
    private captchaService: CaptchaService
  ) { }

  ngOnInit(): void {
    this.loadCaptcha();
  }

  ngOnDestroy(): void {
    this.captchaId = "";
    this.captcha = null;
    if(this.url !== ""){
      window.location.href = this.url;
      this.url = "";
    }
  }
  loadCaptcha(){
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id !== null) {
        this.captchaId = id;
        this.captchaService.getCaptcha(id).subscribe({
          next: (text) => {
            const cap = parser.parse(text);
            this.captcha = cap[0];
            const html = this.decodeBase64(this.captcha.html);
            setTimeout(() => this.injectHtml(html), 0);
          },
          error: (err) => {
            console.error('Error al obtener el captcha:', err);
          }
        });
      }
    });
  }
  decodeBase64(input: string): string {
    try {
      const decoded = atob(input);
      return decodeURIComponent(
        decoded.split('').map((char) => '%' + ('00' + char.charCodeAt(0).toString(16)).slice(-2)).join('')
      );
    } catch (e) {
      console.error('Error al decodificar Base64:', e);
      return '';
    }
  }

  private injectHtml(html: string) {
    const container = document.getElementById('captcha-container');
    if (container) {
      // Crear un elemento temporal para procesar el HTML
      const tempDiv = document.createElement('div');
      tempDiv.innerHTML = html;

      // Inyectar el HTML en el contenedor
      container.innerHTML = tempDiv.innerHTML;

      // Ejecutar scripts después de inyectar
      this.runScripts(tempDiv);
      this.countCaptchaUse();
    }
  }

  countCaptchaUse(){
    var cap: Captcha = this.captcha
    cap.timesUsed++;
    cap.fails++;
    cap.lastUse = new Date();
    this.captchaService.updateCaptcha(this.captcha).subscribe({
      next:(text) =>{ }});
    
  }

  // Función para ejecutar scripts dentro del contenedor inyectado
  private runScripts(tempDiv: HTMLElement) {
    const scripts = tempDiv.getElementsByTagName('script');
    const scriptArray = Array.from(scripts);

    const existingScripts = document.querySelectorAll('script[src], script[data-injected]');
    existingScripts.forEach(script => script.remove());

    //agregar funcion al window
    (window as any).beforeRedirect = this.beforeRedirect.bind(this);

    for (let script of scriptArray) {
      const newScript = document.createElement('script');
      newScript.type = 'text/javascript';
      newScript.setAttribute('data-injected', 'true');
      if (script.src) {
        newScript.src = script.src;
        newScript.onload = () => console.log(`Script loaded: ${script.src}`);
      } else {
        newScript.textContent = script.innerHTML;
      }
      document.body.appendChild(newScript);
    }
  }

  beforeRedirect(url:string){
    var cap: Captcha = this.captcha;
    cap.successes++;
    cap.fails = cap.timesUsed - cap.successes;
    this.captchaService.updateCaptcha(this.captcha).subscribe({
      next:(text) =>{ 
        this.url = url;
        
      }});
  }

  toggleReport() {
    this.showReport = !this.showReport;
  }
}
