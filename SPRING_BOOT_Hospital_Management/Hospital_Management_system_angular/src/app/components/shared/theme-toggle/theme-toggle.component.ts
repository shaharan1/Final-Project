import { Component, inject } from '@angular/core';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-theme-toggle',
  standalone: true,
  imports: [],
  templateUrl: './theme-toggle.component.html',
  styleUrl: './theme-toggle.component.css',
})
export class ThemeToggleComponent {
  private readonly theme = inject(ThemeService);

  toggle(): void {
    this.theme.toggle();
  }

  protected get isDark(): boolean {
    return this.theme.theme() === 'dark';
  }
}
