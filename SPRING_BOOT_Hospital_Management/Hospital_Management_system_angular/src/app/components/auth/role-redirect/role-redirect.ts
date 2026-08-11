import { Component } from '@angular/core';
import { StorageService } from '../../../services/storage.service';
import { Router } from '@angular/router';
import { NavigationService } from '../../../services/navigation.service';

@Component({
  selector: 'app-role-redirect',
  standalone: true,
  imports: [],
  templateUrl: './role-redirect.html',
  styleUrl: './role-redirect.css',
})
export class RoleRedirect {

  constructor(
    private storage: StorageService,
    private router: Router,
    private navService: NavigationService,
  ) {}

  ngOnInit(): void {
    this.router.navigate([this.navService.getDashboardRoute()]);
  }
}
