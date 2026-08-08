import { Injectable, signal, WritableSignal } from '@angular/core';

export type AppTheme = 'light' | 'dark';

const THEME_KEY = 'cm_theme';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  readonly theme: WritableSignal<AppTheme> = signal<AppTheme>(this.resolveInitial());

  constructor() {
    this.apply(this.theme());
  }

  toggle(): AppTheme {
    const next: AppTheme = this.theme() === 'dark' ? 'light' : 'dark';
    this.theme.set(next);
    this.apply(next);
    return next;
  }

  setTheme(theme: AppTheme): void {
    this.theme.set(theme);
    this.apply(theme);
  }

  private apply(theme: AppTheme): void {
    const root = document.documentElement;
    root.setAttribute('data-bs-theme', theme);
    root.setAttribute('data-theme', theme);
    root.style.colorScheme = theme;
    localStorage.setItem(THEME_KEY, theme);

    root.classList.add('theme-switching');
    window.setTimeout(() => root.classList.remove('theme-switching'), 350);
  }

  private resolveInitial(): AppTheme {
    const saved = localStorage.getItem(THEME_KEY);
    if (saved === 'light' || saved === 'dark') {
      return saved;
    }
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      return 'dark';
    }
    return 'light';
  }
}
