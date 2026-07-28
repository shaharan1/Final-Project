import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, RouterModule } from '@angular/router';
import { filter, map } from 'rxjs/operators';
import { StorageService } from '../../../../services/storage.service';
import { AuthService } from '../../../../services/auth.service';
import { NavigationService } from '../../../../services/navigation.service';
import { NavGroup } from '../../../../models/nav-item.model';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class LayoutComponent implements AfterViewInit, OnInit {

  pageTitle = 'Dashboard';

  navGroups: NavGroup[] = [];
  userName = '';
  userRole = '';
  userInitials = '';
  currentYear = new Date().getFullYear();
  greeting = '';

  constructor(
    private storage: StorageService,
    private auth: AuthService,
    private navService: NavigationService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    const user = this.storage.getUser();
    this.userName = user?.name ?? 'User';
    this.userRole = user?.role ?? '';
    this.userInitials = this.getInitials(this.userName);
    this.navGroups = this.navService.getNavGroups();
    this.greeting = this.getGreeting();

    this.route.firstChild?.data.subscribe(data => {
      this.pageTitle = data['title'] ?? 'Dashboard';
    });
  }

  ngAfterViewInit(): void {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    const menuBtn = document.getElementById('menuBtn');
    const sidebarToggle = document.getElementById('sidebarToggle');

    const openSidebar = () => {
      sidebar?.classList.add('open');
      overlay?.classList.add('show');
    };

    const closeSidebar = () => {
      sidebar?.classList.remove('open');
      overlay?.classList.remove('show');
    };

    menuBtn?.addEventListener('click', openSidebar);
    overlay?.addEventListener('click', closeSidebar);
    sidebarToggle?.addEventListener('click', closeSidebar);
  }

  logout(): void {
    this.auth.logout();
  }

  private getInitials(name: string): string {
    return name
      .split(' ')
      .map(w => w[0])
      .join('')
      .toUpperCase()
      .substring(0, 2);
  }

  private getGreeting(): string {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good Morning';
    if (hour < 17) return 'Good Afternoon';
    return 'Good Evening';
  }
}
