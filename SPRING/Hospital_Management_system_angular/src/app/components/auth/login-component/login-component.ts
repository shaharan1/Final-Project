import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { StorageService } from '../../../services/storage.service';

@Component({
  selector: 'app-login-component',
  imports: [CommonModule, FormsModule,],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
})
export class LoginComponent {

  showPassword = false;
  loading = false;
  errorMessage: string | null = null;

  loginData = {
    email: '',
    password: ''
  };

  constructor(private authService: AuthService, private router: Router) { }

  login() {

    this.authService.login(this.loginData).subscribe({

      next: (res) => {

        console.log(res);
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },

      error: (err) => {

        this.loading = false;
        this.errorMessage =
          err.status === 401
            ? 'Invalid email or password.'
            : err.status === 403
              ? 'Your account is not verified or has been disabled.'
              : 'Something went wrong. Please try again.';

      }

    });

  }

}
