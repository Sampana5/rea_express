import { Component } from '@angular/core';

@Component({
  selector: 'app-catalogue',
  templateUrl: './catalogue.component.html',
  styleUrls: ['./catalogue.component.css']
})
export class CatalogueComponent {
  subcategories = [
    {
      title: 'Accessoires',
      items: ['Pipettes', 'Tubes', 'Portoirs', 'Consommable']
    },
    {
      title: 'Bactériologie',
      items: ['Milieu de culture', 'Colorant', "Disque d'antibiogramme", 'Identification']
    },
    {
      title: 'Biochimie',
      items: ['Réactifs', 'Contrôle', 'Électrophorèse', 'Consommable']
    },
    {
      title: 'Consommable',
      items: ['Gants', 'Masques', 'Seringues', 'Divers']
    },
    {
      title: 'Électrophorèse',
      items: ['Kits', 'Tampons', 'Accessoires']
    },
    {
      title: 'Équipement',
      items: ['Analyseurs', 'Centrifugeuses', 'Microscopes']
    },
    {
      title: 'Hématologie',
      items: ['Réactifs', 'Coagulation', 'Transfusion sanguine', 'Groupage']
    },
    {
      title: 'Immunologie',
      items: ['Réactifs', 'Contrôles', 'Kits rapides']
    },
    {
      title: 'Ionogramme',
      items: ['Réactifs', 'Électrodes', 'Contrôles']
    },
    {
      title: 'Parasitologie',
      items: ['Colorants', 'Kits', 'Accessoires']
    },
    {
      title: 'PCR',
      items: ['Kits PCR', 'Réactifs', 'Consommables']
    },
    {
      title: 'Produits chimiques',
      items: ['Produits chimiques', 'Colorant', 'Accessoire', 'Microscopie']
    },
    {
      title: 'Sérologie',
      items: ['Kits', 'Réactifs', 'Contrôles']
    },
    {
      title: 'Urinaire',
      items: ['Bandelettes', 'Réactifs', 'Contrôles']
    }
  ];
}
