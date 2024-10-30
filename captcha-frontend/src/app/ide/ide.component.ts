import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ModalFileComponent } from './modal-file/modal-file.component';
import * as parser from "../../parser/parser";
import { CaptchaService } from '../service/captcha.service';

@Component({
  selector: 'app-ide',
  standalone: true,
  imports: [FormsModule, CommonModule, ModalFileComponent],
  templateUrl: './ide.component.html',
  styleUrl: './ide.component.css'
})
export class IdeComponent {
  fileName = '';
  showModal: boolean = false;

  constructor(private captchaService: CaptchaService) { }

  // Método para crear un nuevo archivo
  newFile() {
    const confirmSave = confirm('Asegurate de guardar el archivo actual.\n¿Ya esta guardado?');
    if (!confirmSave) {
      return;
    }
    this.showModal = true;
  }
  createNewFile(newFileName: string) {
    this.solicitude = ''; // Limpia el contenido del textarea
    this.showModal = false; // Cierra el modal
    this.fileName = newFileName; // Limpia el nombre del archivo
  }
  onModalClosed() {
    this.showModal = false; // Cierra el modal sin realizar ninguna acción
  }
  getFileNameWithCcExtension(fileName: string): string {
    const lastDotIndex = fileName.lastIndexOf('.');
    const baseName = lastDotIndex !== -1 ? fileName.slice(0, lastDotIndex) : fileName;

    return `${baseName}.cc`;
  }
  // Método para guardar un archivo
  saveFile() {
    const blob = new Blob([this.solicitude], { type: 'text/plain' });
    const url = window.URL.createObjectURL(blob);

    // Crear un enlace temporal
    const a = document.createElement('a');
    a.href = url;
    a.download = this.getFileNameWithCcExtension(this.fileName); // Usar el método para agregar la extensión .cc

    // Simular un clic en el enlace
    document.body.appendChild(a);
    a.click();

    // Limpiar
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }
  // Método para guardar como un archivo
  async saveAsFile() {
    try {
      // Verificamos si la API está soportada
      if (!this.isFileSystemAPISupported()) {
        alert('Tu navegador no soporta la File System Access API.');
        return;
      }

      // Mostrar el diálogo de guardado de archivo
      const handle = await window.showSaveFilePicker({
        suggestedName: this.fileName || 'mi-archivo.cc',
        types: [
          {
            description: 'CC Files',
            accept: { 'text/plain': ['.cc'] },
          },
        ],
      });

      // Crear un stream para escribir en el archivo
      const writableStream = await handle.createWritable();

      // Escribir el contenido del textarea en el archivo
      await writableStream.write(this.solicitude);

      // Cerrar el stream
      await writableStream.close();

      alert('Archivo guardado correctamente.');
    } catch (err) {
      console.error('Error guardando el archivo:', err);
    }
  }

  isFileSystemAPISupported(): boolean {
    return 'showSaveFilePicker' in window && 'showOpenFilePicker' in window;
  }
  // Método para abrir un archivo
  async openFile() {
    const confirmSave = confirm('Asegurate de guardar el archivo actual.\n¿Ya esta guardado?');
    if (!confirmSave) {
      return;
    }
    try {
      // Verificamos si la API está soportada
      if (!this.isFileSystemAPISupported()) {
        alert('Tu navegador no soporta la File System Access API.');
        return;
      }

      // Mostrar el diálogo para seleccionar un archivo
      const [fileHandle] = await window.showOpenFilePicker({
        types: [
          {
            description: 'CC Files',
            accept: { 'text/plain': ['.cc'] },
          },
        ],
      });

      // Obtener el archivo seleccionado
      const file = await fileHandle.getFile();

      // Leer el contenido del archivo
      const fileContents = await file.text();
      this.fileName = this.getFileNameWithCcExtension(file.name);
      // Colocar el contenido en el textarea
      this.solicitude = fileContents;
      this.updateLineNumbers();
    } catch (err) {
      console.error('Error abriendo el archivo:', err);
    }
  }

  solicitude: string = '';
  serverResponse: string = ''; // Aquí puedes actualizar la respuesta del servidor
  lineNumbers: string = '1';
  lineNumbersR: string = '1';
  cursorPosition: string = 'Línea 1, Columna 1';

  updateLineNumbers() {
    const lines = this.solicitude.split('\n').length;
    this.lineNumbers = Array.from({ length: lines }, (_, i) => i + 1).join('<br>');
  }
  updateLineNumbersR() {
    const lines = this.serverResponse.split('\n').length;
    this.lineNumbersR = Array.from({ length: lines }, (_, i) => i + 1).join('<br>');
  }

  syncScroll(event: Event) {
    const textarea = event.target as HTMLTextAreaElement;
    const lineNumbersElement = document.querySelector('.line-numbers') as HTMLElement;
    lineNumbersElement.scrollTop = textarea.scrollTop;
  }

  updateCursorPosition(event: KeyboardEvent | MouseEvent) {
    const textarea = event.target as HTMLTextAreaElement;
    const cursorPosition = textarea.selectionStart;
    const textBeforeCursor = textarea.value.substring(0, cursorPosition);
    const lineNumber = textBeforeCursor.split('\n').length;
    const columnNumber = cursorPosition - textBeforeCursor.lastIndexOf('\n');
    this.cursorPosition = `Línea ${lineNumber}, Columna ${columnNumber}`;
  }

  submitSolicitud() {
    console.log('Solicitud enviada:', this.solicitude);
    this.captchaService.insertCaptcha(this.solicitude).subscribe({
      next: (text) => {
        this.serverResponse += `${text}\n`;

        // Desplazar el textarea automáticamente hasta el final
        setTimeout(() => {
          const textarea = document.getElementById('serverResponse') as HTMLTextAreaElement;
          textarea.scrollTop = textarea.scrollHeight;
        }, 0);
      }
    });
  }

  handleKeydown(event: KeyboardEvent) {
    const textarea = event.target as HTMLTextAreaElement;

    if (event.key === 'Tab') {
      event.preventDefault(); // Evita el comportamiento predeterminado de cambiar de foco

      const start = textarea.selectionStart;
      const end = textarea.selectionEnd;

      // Insertar 4 espacios en la posición actual del cursor
      textarea.value = textarea.value.substring(0, start) + '    ' + textarea.value.substring(end);

      // Colocar el cursor después de los 4 espacios
      textarea.selectionStart = textarea.selectionEnd = start + 4;

      // Actualizar el modelo ngModel manualmente si es necesario
      this.solicitude = textarea.value;
    }
  }
}
