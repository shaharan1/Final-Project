import { CommonModule } from '@angular/common';
import { AfterViewInit, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements AfterViewInit {

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

}
