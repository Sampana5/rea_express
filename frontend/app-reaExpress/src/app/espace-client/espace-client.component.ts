import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReaExpressService } from '../shared/rea-express.service';
import { LoginResponse, UserModel } from '../shared/models';

@Component({
  selector: 'app-espace-client',
  templateUrl: './espace-client.component.html',
  styleUrls: ['./espace-client.component.css']
})
export class EspaceClientComponent implements OnInit {
  user: LoginResponse | null = null;
  profile: UserModel | null = null;
  errorMessage = '';

  get isAdmin(): boolean {
    return this.reaService.isAdmin();
  }

  constructor(private reaService: ReaExpressService, private router: Router) {}

  ngOnInit(): void {
    this.user = this.reaService.getCurrentUser();
    this.reaService.me().subscribe({
      next: (profile) => {
        this.profile = profile;
      },
      error: () => {
        this.errorMessage = 'Session expirée ou backend indisponible.';
      }
    });
  }

  logout(): void {
    this.reaService.logout();
    this.router.navigate(['/login']);
  }
}
