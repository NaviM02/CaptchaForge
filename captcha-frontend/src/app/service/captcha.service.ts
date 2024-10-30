import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Captcha } from '../model/captcha';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CaptchaService {
  url: string = 'http://localhost/CaptchaForge';
  
  constructor(private http: HttpClient) { }

  getCaptchas():Observable<string>{
    return this.http.get(`${this.url}/captchas`, {responseType: 'text'});
  }
  getCaptcha(id:string):Observable<string>{
    return this.http.get(`${this.url}/captchas/${id}`, {responseType: 'text'});
  }
  insertCaptcha(text:string){
    return this.http.post(`${this.url}/captchas/`, text, {responseType: 'text'});
  }
  updateCaptcha(captcha: Captcha):Observable<string>{
    return this.http.put(`${this.url}/captchas/${captcha.id}`, captcha.dbString(), {responseType: 'text'})
  }
}
