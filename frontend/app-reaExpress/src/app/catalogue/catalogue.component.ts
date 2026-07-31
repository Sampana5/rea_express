import { Component } from '@angular/core';

interface ProductItem {
  name: string;
  image: string;
}

interface Subcategory {
  title: string;
  items: ProductItem[];
}

const IMAGE_BASE = 'assets/images/produits';

const item = (name: string, slug: string): ProductItem => ({
  name,
  image: `${IMAGE_BASE}/${slug}.png`
});

@Component({
  selector: 'app-catalogue',
  templateUrl: './catalogue.component.html',
  styleUrls: ['./catalogue.component.css']
})
export class CatalogueComponent {
  subcategories: Subcategory[] = [
    {
      title: 'Accessoires',
      items: [
        item('Pipettes', 'accessoires-pipettes'),
        item('Tubes', 'accessoires-tubes'),
        item('Portoirs', 'accessoires-portoirs'),
        item('Consommable', 'accessoires-consommable')
      ]
    },
    {
      title: 'Bactériologie',
      items: [
        item('Milieu de culture', 'bacteriologie-milieu-de-culture'),
        item('Colorant', 'bacteriologie-colorant'),
        item("Disque d'antibiogramme", 'bacteriologie-disque-antibiogramme'),
        item('Identification', 'bacteriologie-identification')
      ]
    },
    {
      title: 'Biochimie',
      items: [
        item('Réactifs', 'biochimie-reactifs'),
        item('Contrôle', 'biochimie-controle'),
        item('Électrophorèse', 'biochimie-electrophorese'),
        item('Consommable', 'biochimie-consommable')
      ]
    },
    {
      title: 'Consommable',
      items: [
        item('Gants', 'consommable-gants'),
        item('Masques', 'consommable-masques'),
        item('Seringues', 'consommable-seringues'),
        item('Divers', 'consommable-divers')
      ]
    },
    {
      title: 'Électrophorèse',
      items: [
        item('Kits', 'electrophorese-kits'),
        item('Tampons', 'electrophorese-tampons'),
        item('Accessoires', 'electrophorese-accessoires')
      ]
    },
    {
      title: 'Équipement',
      items: [
        item('Analyseurs', 'equipement-analyseurs'),
        item('Centrifugeuses', 'equipement-centrifugeuses'),
        item('Microscopes', 'equipement-microscopes')
      ]
    },
    {
      title: 'Hématologie',
      items: [
        item('Réactifs', 'hematologie-reactifs'),
        item('Coagulation', 'hematologie-coagulation'),
        item('Transfusion sanguine', 'hematologie-transfusion-sanguine'),
        item('Groupage', 'hematologie-groupage')
      ]
    },
    {
      title: 'Immunologie',
      items: [
        item('Réactifs', 'immunologie-reactifs'),
        item('Contrôles', 'immunologie-controles'),
        item('Kits rapides', 'immunologie-kits-rapides')
      ]
    },
    {
      title: 'Ionogramme',
      items: [
        item('Réactifs', 'ionogramme-reactifs'),
        item('Électrodes', 'ionogramme-electrodes'),
        item('Contrôles', 'ionogramme-controles')
      ]
    },
    {
      title: 'Parasitologie',
      items: [
        item('Colorants', 'parasitologie-colorants'),
        item('Kits', 'parasitologie-kits'),
        item('Accessoires', 'parasitologie-accessoires')
      ]
    },
    {
      title: 'PCR',
      items: [
        item('Kits PCR', 'pcr-kits-pcr'),
        item('Réactifs', 'pcr-reactifs'),
        item('Consommables', 'pcr-consommables')
      ]
    },
    {
      title: 'Produits chimiques',
      items: [
        item('Produits chimiques', 'produits-chimiques-produits-chimiques'),
        item('Colorant', 'produits-chimiques-colorant'),
        item('Accessoire', 'produits-chimiques-accessoire'),
        item('Microscopie', 'produits-chimiques-microscopie')
      ]
    },
    {
      title: 'Sérologie',
      items: [
        item('Kits', 'serologie-kits'),
        item('Réactifs', 'serologie-reactifs'),
        item('Contrôles', 'serologie-controles')
      ]
    },
    {
      title: 'Urinaire',
      items: [
        item('Bandelettes', 'urinaire-bandelettes'),
        item('Réactifs', 'urinaire-reactifs'),
        item('Contrôles', 'urinaire-controles')
      ]
    }
  ];
}
