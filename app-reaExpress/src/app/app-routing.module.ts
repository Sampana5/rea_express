import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CatalogueComponent } from './catalogue/catalogue.component';
import { CommentComanderComponent } from './comment-comander/comment-comander.component';
import { ConditionVenteComponent } from './condition-vente/condition-vente.component';
import { ConnecterComponent } from './connecter/connecter.component';
import { ContactComponent } from './contact/contact.component';
import { EspaceClientComponent } from './espace-client/espace-client.component';
import { FaqComponent } from './faq/faq.component';
import { HomeComponent } from './home/home.component';
import { MusicListComponent } from './music-list/music-list.component';
import { MusicComponent } from './music/music.component';
import { PlanSiteComponent } from './plan-site/plan-site.component';
import { ProduitsComponent } from './produits/produits.component';
import { PromotionComponent } from './promotion/promotion.component';
import { TemoignagesComponent } from './temoignages/temoignages.component';
import { TestItuneComponent } from './test-itune/test-itune.component';
import { VoirPanierComponent } from './voir-panier/voir-panier.component';

const routes: Routes = [
  {path:'home', component: HomeComponent},
  {path:'catalogue', component: CatalogueComponent},
  {path:'comment-commander', component: CommentComanderComponent},
  {path:'condition-vente', component: ConditionVenteComponent},
  {path:'connecter', component: ConnecterComponent},
  {path:'contact', component: ContactComponent},
  {path:'espace-client', component: EspaceClientComponent},
  {path:'faq', component: FaqComponent},
  {path:'plan-site', component: PlanSiteComponent},
  {path:'produits', component: ProduitsComponent},
  {path:'promotion', component: PromotionComponent},
  {path:'temoignages', component: TemoignagesComponent},
  {path:'voir-panier', component: VoirPanierComponent},
  {path:'itune', component: TestItuneComponent},
  {path: 'music/:musicId', component: MusicComponent},
  {path: '', redirectTo: 'home', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
