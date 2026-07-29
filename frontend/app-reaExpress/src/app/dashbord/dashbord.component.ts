import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReaExpressService } from '../shared/rea-express.service';
import { LoginResponse, UserModel } from '../shared/models';

@Component({
  selector: 'app-dashbord',
  templateUrl: './dashbord.component.html',
  styleUrls: ['./dashbord.component.css']
})
export class DashbordComponent implements OnInit {
  currentUser: LoginResponse | null = null;
  users: UserModel[] = [];
  loading = false;
  errorMessage = '';
  successMessage = '';

  get adminCount(): number {
    return this.users.filter((u) => u.roles?.includes('ROLE_ADMIN')).length;
  }

  get activeCount(): number {
    return this.users.filter((u) => u.status === 'true').length;
  }

  constructor(private reaService: ReaExpressService, private router: Router) {}

  ngOnInit(): void {
    this.currentUser = this.reaService.getCurrentUser();
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.errorMessage = '';
    this.reaService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Impossible de charger les utilisateurs (droits admin / backend).';
      }
    });
  }

  toggleStatus(user: UserModel): void {
    const nextStatus = user.status === 'true' ? 'false' : 'true';
    this.reaService.updateUser(user.id, { status: nextStatus }).subscribe({
      next: (res) => {
        this.successMessage = res.message;
        this.loadUsers();
      },
      error: () => {
        this.errorMessage = 'Mise à jour du statut impossible.';
      }
    });
  }

  deleteUser(user: UserModel): void {
    if (!confirm(`Supprimer ${user.name} ?`)) {
      return;
    }
    this.reaService.deleteUser(user.id).subscribe({
      next: (res) => {
        this.successMessage = res.message;
        this.loadUsers();
      },
      error: () => {
        this.errorMessage = 'Suppression impossible.';
      }
    });
  }

  logout(): void {
    this.reaService.logout();
    this.router.navigate(['/login']);
  }
}
