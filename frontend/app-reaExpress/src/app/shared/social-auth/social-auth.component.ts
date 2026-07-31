import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  NgZone,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { ReaExpressService } from '../rea-express.service';

declare const google: any;

@Component({
  selector: 'app-social-auth',
  templateUrl: './social-auth.component.html',
  styleUrls: ['./social-auth.component.css']
})
export class SocialAuthComponent implements OnInit, AfterViewInit, OnDestroy {
  /** 'login' ou 'signup' — adapte uniquement le libellé des boutons. */
  @Input() mode: 'login' | 'signup' = 'login';

  @ViewChild('googleBtn') googleBtn?: ElementRef<HTMLDivElement>;

  loading = false;
  errorMessage = '';
  googleReady = false;

  private githubPopup: Window | null = null;
  private readonly onMessage = (event: MessageEvent) => this.handleGithubMessage(event);

  constructor(
    private reaService: ReaExpressService,
    private router: Router,
    private zone: NgZone
  ) {}

  get googleLabel(): string {
    return this.mode === 'signup' ? "S'inscrire avec Google" : 'Continuer avec Google';
  }

  get githubLabel(): string {
    return this.mode === 'signup' ? "S'inscrire avec GitHub" : 'Continuer avec GitHub';
  }

  ngOnInit(): void {
    window.addEventListener('message', this.onMessage);
  }

  ngAfterViewInit(): void {
    this.initGoogle();
  }

  ngOnDestroy(): void {
    window.removeEventListener('message', this.onMessage);
    if (this.githubPopup && !this.githubPopup.closed) {
      this.githubPopup.close();
    }
  }

  // ------------------------------------------------------------- Google

  private initGoogle(): void {
    const clientId = environment.oauth?.googleClientId;
    if (!clientId || typeof google === 'undefined' || !google?.accounts?.id) {
      return;
    }

    google.accounts.id.initialize({
      client_id: clientId,
      callback: (response: { credential: string }) => {
        this.zone.run(() => this.onGoogleCredential(response.credential));
      }
    });

    if (this.googleBtn?.nativeElement) {
      google.accounts.id.renderButton(this.googleBtn.nativeElement, {
        theme: 'outline',
        size: 'large',
        width: 320,
        text: this.mode === 'signup' ? 'signup_with' : 'continue_with',
        locale: 'fr'
      });
      this.googleReady = true;
    }
  }

  private onGoogleCredential(idToken: string): void {
    this.loading = true;
    this.errorMessage = '';
    this.reaService.loginWithGoogle(idToken).subscribe({
      next: () => this.onAuthenticated(),
      error: (err) => this.onAuthError(err, 'Connexion Google impossible pour le moment.')
    });
  }

  // ------------------------------------------------------------- GitHub

  openGithubPopup(): void {
    const clientId = environment.oauth?.githubClientId;
    if (!clientId) {
      this.errorMessage = "La connexion GitHub n'est pas configurée.";
      return;
    }

    const redirectUri = `${window.location.origin}/oauth/callback/github`;
    const url = 'https://github.com/login/oauth/authorize'
      + `?client_id=${encodeURIComponent(clientId)}`
      + `&redirect_uri=${encodeURIComponent(redirectUri)}`
      + '&scope=read:user%20user:email';

    const width = 600;
    const height = 700;
    const left = window.screenX + (window.outerWidth - width) / 2;
    const top = window.screenY + (window.outerHeight - height) / 2;

    this.errorMessage = '';
    this.githubPopup = window.open(
      url,
      'github-oauth',
      `width=${width},height=${height},left=${left},top=${top},noopener=no`
    );
    if (!this.githubPopup) {
      this.errorMessage = 'Autorisez les fenêtres pop-up pour continuer avec GitHub.';
    }
  }

  private handleGithubMessage(event: MessageEvent): void {
    if (event.origin !== window.location.origin) {
      return;
    }
    const data = event.data;
    if (!data || data.type !== 'github-oauth' || !data.code) {
      return;
    }

    this.zone.run(() => {
      this.loading = true;
      this.errorMessage = '';
      this.reaService.loginWithGithub(data.code).subscribe({
        next: () => this.onAuthenticated(),
        error: (err) => this.onAuthError(err, 'Connexion GitHub impossible pour le moment.')
      });
    });
  }

  // ------------------------------------------------------------- Commun

  private onAuthenticated(): void {
    this.loading = false;
    this.router.navigate(['/home']);
  }

  private onAuthError(err: any, fallback: string): void {
    this.loading = false;
    this.errorMessage = err?.error?.message || fallback;
  }
}
