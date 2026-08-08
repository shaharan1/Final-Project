import { Component } from '@angular/core';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-theme-toggle',
  standalone: true,
  imports: [],
  templateUrl: './theme-toggle.component.html',
  styleUrl: './theme-toggle.component.css',
})
export class ThemeToggleComponent {
  protected readonly themeService = this.theme;

  constructor(private theme: ThemeService) {}

  toggle(): void {
    this.themeService.toggle();
  }
}
