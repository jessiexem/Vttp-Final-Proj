import { NgModule } from "@angular/core";
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'
import { MatInputModule } from '@angular/material/input'
import { MatFormFieldModule } from '@angular/material/form-field'
import {  MatMenuModule } from '@angular/material/menu'
import {  MatListModule } from '@angular/material/list'
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatChipsModule } from '@angular/material/chips';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatSelectModule} from '@angular/material/select';
import {MatProgressBarModule} from '@angular/material/progress-bar';

const matModules: any[] = [
    MatToolbarModule, MatButtonModule, MatListModule,
    MatIconModule, MatInputModule, MatFormFieldModule,
    MatMenuModule, MatCardModule, MatGridListModule, MatChipsModule,
    MatSidenavModule, MatSelectModule, MatProgressBarModule
  ]
  
  @NgModule({
    imports: matModules,
    exports: matModules
  })
  export class MaterialModule {}