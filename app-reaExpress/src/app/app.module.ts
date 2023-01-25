import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import { ItuneService } from './test-itune/itune-service';
import { AppConfigModule } from './app-config/app-config.module';
import { CommonModule } from '@angular/common';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations'
import {MatIconModule} from '@angular/material/icon';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { BodyComponent } from './body/body.component';
import { ProduitsComponent } from './produits/produits.component';
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
import { TestItuneComponent } from './test-itune/test-itune.component';
import { HomeComponent } from './home/home.component';
import { MusicListComponent } from './music-list/music-list.component';
import { MusicComponent } from './music/music.component';
import { PlanAccesComponent } from './plan-acces/plan-acces.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    BodyComponent,
    ProduitsComponent,
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
    TestItuneComponent,
    HomeComponent,
    MusicListComponent,
    MusicComponent,
    PlanAccesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    AppConfigModule,
    CommonModule,
    BrowserAnimationsModule,
    MatIconModule
  ],
  providers: [
    ItuneService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
