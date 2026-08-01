import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ReaExpressService } from '../shared/rea-express.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: FormGroup;
  loading = false;
  errorMessage = '';
  private returnUrl = '/espace-client';

  constructor(
    private fb: FormBuilder,
    private reaService: ReaExpressService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  ngOnInit(): void {
    const raw = this.route.snapshot.queryParamMap.get('returnUrl');
    if (raw && raw.startsWith('/') && !raw.startsWith('//')) {
      this.returnUrl = raw;
    }
  }

  onSubmit(): void {
    this.errorMessage = '';
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.reaService.login(this.form.value).subscribe({
      next: (user) => {
        this.loading = false;
        if (user.roles?.includes('ROLE_ADMIN') && this.returnUrl === '/espace-client') {
          this.router.navigate(['/dashbord']);
        } else {
          this.router.navigateByUrl(this.returnUrl);
        }
      },
      error: (err) => {
        this.loading = false;
        if (err.status === 403) {
          this.errorMessage = 'Compte inactif. Contactez un administrateur.';
        } else if (err.status === 401) {
          this.errorMessage = 'Identifiant ou mot de passe incorrect.';
        } else {
          this.errorMessage = 'Connexion impossible. Vérifiez que le backend est démarré.';
        }
      }
    });
  }
}
