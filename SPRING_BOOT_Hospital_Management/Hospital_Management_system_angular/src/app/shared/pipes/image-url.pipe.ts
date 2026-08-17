import { Pipe, PipeTransform } from '@angular/core';
import { environment } from '../../../environments/environment';

/**
 * Resolves a stored image reference to a loadable URL.
 *
 * The backend stores uploaded profile photos under several sub-folders and
 * serves them via the `/images/**` (and `/uploads/**`) static handler, e.g.
 *   /images/doctor/...      /images/nurse/...
 *   /images/office-staff/... /images/<file>
 * These are relative to the API origin, so we prefix them with
 * `environment.imageBaseUrl`. Local assets (assets/...) and already-absolute
 * (http/data:) URLs are returned unchanged.
 */
@Pipe({ name: 'imageUrl', standalone: true })
export class ImageUrlPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) {
      return this.placeholder();
    }
    // Already absolute or inline data URI -> use as-is.
    if (/^(https?:|data:|blob:)/i.test(value)) {
      return value;
    }
    // Backend-stored uploads live under /images/** or /uploads/**.
    if (value.startsWith('/images/') || value.startsWith('/uploads/')) {
      const base = environment.imageBaseUrl.replace(/\/+$/, '');
      const path = value.startsWith('/') ? value : '/' + value;
      return base + path;
    }
    // Local Angular assets (e.g. assets/images/doctor.png) -> use as-is.
    if (value.startsWith('assets/')) {
      return value;
    }
    // Anything else (e.g. a raw Windows file path left in the DB) cannot be
    // loaded in the browser, so show a neutral placeholder instead.
    return this.placeholder();
  }

  private placeholder(): string {
    const svg =
      `<svg xmlns='http://www.w3.org/2000/svg' width='88' height='88'>` +
      `<rect width='88' height='88' rx='44' fill='#e2e8f0'/>` +
      `<text x='50%' y='50%' dy='.35em' font-family='Arial,Helvetica,sans-serif' ` +
      `font-size='36' fill='#94a3b8' text-anchor='middle'>?</text></svg>`;
    return 'data:image/svg+xml;charset=utf-8,' + encodeURIComponent(svg);
  }
}
