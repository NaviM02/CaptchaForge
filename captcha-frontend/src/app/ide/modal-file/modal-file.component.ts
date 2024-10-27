import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-modal-file',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './modal-file.component.html',
  styleUrl: './modal-file.component.css'
})
export class ModalFileComponent {
  fileName: string = '';

  @Output() fileCreated: EventEmitter<string> = new EventEmitter<string>();
  @Output() modalClosed: EventEmitter<void> = new EventEmitter<void>();

  createFile() {
    if (this.fileName.trim()) {
      this.fileCreated.emit(this.fileName);
      this.fileName = '';
    }
  }

  cancel() {
    this.modalClosed.emit(); // Emitimos el evento de cierre sin acción
  }
}
