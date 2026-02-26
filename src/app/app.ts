import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from "./header/header";
import { Footer } from "./footer/footer";
import { CustomAlert } from "./shared/custom-alert/custom-alert";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Footer, CustomAlert],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('project-app');
}
