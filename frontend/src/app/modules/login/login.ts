import { Component, inject, signal } from '@angular/core';
import { AuthService } from './auth.service';
import { LoginDto } from './login-dto';
import { form, email, required, FormField } from '@angular/forms/signals';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormField],
  templateUrl: './login.html',
  styleUrl: './login.css',
  standalone: true,
})
export class Login {
  private authService: AuthService = inject(AuthService);
  private router : Router = inject(Router);

  private loginModel = signal<LoginDto>({
    email: '',
    password: '',
  });

  public loginForm = form(this.loginModel, (path) => {
    required(path.email, { message: 'El correo es obligatorio' });
    email(path.email, { message: 'El correo no es valido' });
    required(path.password, { message: 'La contraseña es obligatoria' });
  });

  onSubmit(e : Event) {
    e.preventDefault();
    this.authService.login(this.loginModel()).subscribe({
      next : (resp) => {
        this.router.navigate(['/main'])
      }
    });
  }
}
