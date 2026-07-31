import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AuthInterceptor } from './shared/auth.interceptor';
import { AuthGuard } from './shared/auth.guard';
import { AdminGuard } from './shared/admin.guard';
import { AppConfigModule } from './app-config/app-config.module';
import { ReaExpressService } from './shared/rea-express.service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { CatalogueComponent } from './catalogue/catalogue.component';
import { PromotionComponent } from './promotion/promotion.component';
import { FaqComponent } from './faq/faq.component';
import { ContactComponent } from './contact/contact.component';
import { TemoignagesComponent } from './temoignages/temoignages.component';
import { EspaceClientComponent } from './espace-client/espace-client.component';
import { ConnecterComponent } from './connecter/connecter.component';
import { VoirPanierComponent } from './voir-panier/voir-panier.component';
import { ConditionVenteComponent } from './condition-vente/condition-vente.component';
import { CommentComanderComponent } from './comment-comander/comment-comander.component';
import { PlanSiteComponent } from './plan-site/plan-site.component';
import { HomeComponent } from './home/home.component';
import { PlanAccesComponent } from './plan-acces/plan-acces.component';
import { ReadMoreComponent } from './read-more/read-more.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { MailSendComponent } from './mail-send/mail-send.component';
import { DashbordComponent } from './dashbord/dashbord.component';
import { ProduitDetailComponent } from './produit-detail/produit-detail.component';
import { SocialAuthComponent } from './shared/social-auth/social-auth.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { OauthCallbackComponent } from './oauth-callback/oauth-callback.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    CatalogueComponent,
    PromotionComponent,
    FaqComponent,
    ContactComponent,
    TemoignagesComponent,
    EspaceClientComponent,
    ConnecterComponent,
    VoirPanierComponent,
    ConditionVenteComponent,
    CommentComanderComponent,
    PlanSiteComponent,
    HomeComponent,
    PlanAccesComponent,
    ReadMoreComponent,
    LoginComponent,
    SignupComponent,
    MailSendComponent,
    DashbordComponent,
    ProduitDetailComponent,
    SocialAuthComponent,
    ForgotPasswordComponent,
    OauthCallbackComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppConfigModule,
    CommonModule,
    BrowserAnimationsModule
  ],
  providers: [
    ReaExpressService,
    AuthGuard,
    AdminGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
