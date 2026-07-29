import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { LoginResponse } from '../shared/models';
import { ReaExpressService } from '../shared/rea-express.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
  menuOpen = false;
  searchOpen = false;
  scrolled = false;
  searchQuery = '';
  currentUser: LoginResponse | null = null;
  private sub?: Subscription;
  private routeSub?: Subscription;

  constructor(private reaService: ReaExpressService, private router: Router) {}

  get isAdmin(): boolean {
    return this.reaService.isAdmin();
  }

  ngOnInit(): void {
    this.onScroll();
    this.sub = this.reaService.currentUser$.subscribe((user) => {
      this.currentUser = user;
    });
    this.routeSub = this.router.events
      .pipe(filter((e): e is NavigationEnd => e instanceof NavigationEnd))
      .subscribe(() => {
        this.closeMenu();
        this.searchOpen = false;
        window.scrollTo({ top: 0, behavior: 'smooth' });
      });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
    this.routeSub?.unsubscribe();
  }

  @HostListener('window:scroll')
  onScroll(): void {
    this.scrolled = window.scrollY > 8;
  }

  @HostListener('document:keydown.escape')
  onEscape(): void {
    this.closeMenu();
    this.searchOpen = false;
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
    this.searchOpen = false;
  }

  toggleSearch(): void {
    this.searchOpen = !this.searchOpen;
    this.menuOpen = false;
  }

  closeMenu(): void {
    this.menuOpen = false;
  }

  logout(): void {
    this.reaService.logout();
    this.closeMenu();
    this.router.navigate(['/login']);
  }
}
