import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

/**
 * Page de retour du popup GitHub OAuth : transmet le code d'autorisation
 * à la fenêtre principale puis se ferme.
 */
@Component({
  selector: 'app-oauth-callback',
  template: `
    <div class="oauth-callback">
      <p>{{ message }}</p>
    </div>
  `,
  styles: [`
    .oauth-callback {
      display: flex;
      align-items: center;
      justify-content: center;
      min-height: 60vh;
      color: #475569;
      font-size: 1rem;
    }
  `]
})
export class OauthCallbackComponent implements OnInit {
  message = 'Connexion en cours...';

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const code = this.route.snapshot.queryParamMap.get('code');
    const error = this.route.snapshot.queryParamMap.get('error');

    if (window.opener && code) {
      window.opener.postMessage({ type: 'github-oauth', code }, window.location.origin);
      this.message = 'Connexion réussie, vous pouvez fermer cette fenêtre.';
      window.close();
      return;
    }

    if (window.opener && error) {
      this.message = 'Connexion annulée, vous pouvez fermer cette fenêtre.';
      window.close();
      return;
    }

    // Ouverture directe (hors popup) : retour à la page de connexion.
    this.router.navigate(['/login']);
  }
}
