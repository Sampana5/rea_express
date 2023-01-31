import { NgModule, NO_ERRORS_SCHEMA,CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import { AppConfigModule } from './app-config/app-config.module';
import { CommonModule } from '@angular/common';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations'
import {MatIconModule} from '@angular/material/icon';
import { FlexLayoutModule } from '@angular/flex-layout';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';


//angularFire imports
import {AngularFireModule} from '@angular/fire/compat';
import { AngularFirestoreModule } from '@angular/fire/compat/firestore/';

//environnet import
import { environment } from 'src/environments/environment';

//service import
import { ItuneService } from './test-itune/itune-service';
import { ReaExpressService } from './shared/rea-express.service';


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
import { ReadMoreComponent } from './read-more/read-more.component';
import { LoginComponent } from './login/login.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import { SignupComponent } from './signup/signup.component';
import { MailSendComponent } from './mail-send/mail-send.component';
import { DashbordComponent } from './dashbord/dashbord.component';






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
    PlanAccesComponent,
    ReadMoreComponent,
    LoginComponent,
    SignupComponent,
    MailSendComponent,
    DashbordComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    AppConfigModule,
    CommonModule,
    BrowserAnimationsModule,
    MatIconModule,
    FlexLayoutModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatToolbarModule,
    ReactiveFormsModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFirestoreModule


  ],
  providers: [
    ItuneService,
    ReaExpressService
  ],
  bootstrap: [AppComponent],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA,
    NO_ERRORS_SCHEMA
  ]
})
export class AppModule { }
