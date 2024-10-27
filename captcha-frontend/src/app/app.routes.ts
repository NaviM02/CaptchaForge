import { Routes } from '@angular/router';
import { IdeComponent } from './ide/ide.component';
import { ReportsComponent } from './reports/reports.component';
import { TableComponent } from './table/table.component';
import { CaptchaDisplayComponent } from './table/captcha-display/captcha-display.component';

export const routes: Routes = [
    {path: 'ide', component:IdeComponent},
    {path: 'reports', component:ReportsComponent},
    {path: 'table', component:TableComponent},
    {path: 'captcha-display/:id', component:CaptchaDisplayComponent},
    
    {path: '', redirectTo: '/ide', pathMatch: 'full'},
    {path: '**', redirectTo: '/ide'}
    
];
