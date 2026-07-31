package com.rea.express.config;

import com.rea.express.POJO.Category;
import com.rea.express.POJO.Product;
import com.rea.express.POJO.ProductImage;
import com.rea.express.POJO.SubCategory;
import com.rea.express.dao.CategoryDao;
import com.rea.express.utils.SlugUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Remplit la base avec le catalogue initial (catégories → sous-catégories → produits)
 * uniquement si la table des catégories est vide.
 *
 * Les chemins d'images sont déterministes :
 *   /assets/images/products/{categorie}/{sous-categorie}/{produit}.jpg
 * et correspondent aux fichiers générés par scripts/import-product-images.ps1.
 * La base ne stocke que le chemin, jamais le binaire (compatible S3/CDN plus tard).
 */
@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class CatalogDataLoader implements CommandLineRunner {

    private static final String IMAGE_BASE = "/assets/images/products";

    private final CategoryDao categoryDao;

    private int referenceCounter = 1;

    @Override
    @Transactional
    public void run(String... args) {
        if (categoryDao.count() > 0) {
            log.info("Catalogue déjà présent, seeding ignoré.");
            return;
        }
        log.info("Seeding du catalogue REA Express...");
        seedLaboratoire();
        seedMedecine();
        seedBanqueDeSang();
        seedPcr();
        log.info("Catalogue seedé avec succès.");
    }

    private void seedLaboratoire() {
        Category cat = category("Laboratoire",
                "Réactifs, consommables et équipements pour laboratoires d'analyses médicales.");
        sub(cat, "Accessoires", "Pipettes", "Tubes", "Portoirs", "Consommable");
        sub(cat, "Bactériologie", "Milieu de culture", "Colorant", "Disque d'antibiogramme", "Identification");
        sub(cat, "Biochimie", "Réactifs", "Contrôle", "Électrophorèse", "Consommable biochimie");
        sub(cat, "Consommable", "Gants", "Masques", "Seringues", "Divers");
        sub(cat, "Électrophorèse", "Kits électrophorèse", "Tampons", "Accessoires électrophorèse");
        sub(cat, "Équipement", "Analyseurs", "Centrifugeuses", "Microscopes");
        sub(cat, "Hématologie", "Réactifs hématologie", "Coagulation", "Transfusion sanguine", "Groupage");
        sub(cat, "Immunologie", "Réactifs immunologie", "Contrôles immunologie", "Kits rapides");
        sub(cat, "Ionogramme", "Réactifs ionogramme", "Électrodes", "Contrôles ionogramme");
        sub(cat, "Parasitologie", "Colorants parasitologie", "Kits parasitologie", "Accessoires parasitologie");
        sub(cat, "PCR laboratoire", "Kits PCR", "Réactifs PCR", "Consommables PCR");
        sub(cat, "Produits chimiques", "Produits chimiques", "Colorant chimique", "Accessoire chimie", "Microscopie");
        sub(cat, "Sérologie", "Kits sérologie", "Réactifs sérologie", "Contrôles sérologie");
        sub(cat, "Urinaire", "Bandelettes", "Réactifs urinaire", "Contrôles urinaire");
    }

    private void seedMedecine() {
        Category cat = category("Médecine",
                "Matériel médical et consommables pour cliniques, cabinets et centres de santé.");
        sub(cat, "Diagnostic", "Tensiomètres", "Stéthoscopes", "Thermomètres");
        sub(cat, "Consommables médicaux", "Compresses", "Pansements", "Gants d'examen");
    }

    private void seedBanqueDeSang() {
        Category cat = category("Banque de sang",
                "Poches, réactifs et consommables pour la collecte et la transfusion sanguine.");
        sub(cat, "Poches de sang", "Poches simples", "Poches doubles", "Poches triples");
        sub(cat, "Réactifs de groupage", "Sérums tests", "Cartes de groupage", "Réactifs Coombs");
    }

    private void seedPcr() {
        Category cat = category("PCR",
                "Extraction, amplification et consommables pour la biologie moléculaire.");
        sub(cat, "Extraction", "Kits d'extraction ADN", "Kits d'extraction ARN");
        sub(cat, "Amplification", "Master Mix", "Enzymes", "Consommables amplification");
    }

    // ------------------------------------------------------------- helpers

    private Category category(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setSlug(SlugUtils.slugify(name));
        category.setDescription(description);
        return categoryDao.save(category);
    }

    private void sub(Category category, String name, String... productNames) {
        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setSlug(SlugUtils.slugify(name));
        subCategory.setDescription("Gamme " + name.toLowerCase() + " — catalogue " + category.getName() + ".");
        subCategory.setCategory(category);

        List<Product> products = new ArrayList<>();
        for (String productName : productNames) {
            products.add(buildProduct(category, subCategory, productName));
        }
        subCategory.setProducts(products);
        category.getSubCategories().add(subCategory);
        // cascade ALL depuis Category -> SubCategory -> Product/ProductImage
        categoryDao.save(category);
    }

    private Product buildProduct(Category category, SubCategory subCategory, String productName) {
        String productSlug = SlugUtils.slugify(productName);
        String imageUrl = String.format("%s/%s/%s/%s.jpg",
                IMAGE_BASE, category.getSlug(), subCategory.getSlug(), productSlug);

        Product product = new Product();
        product.setName(productName);
        product.setSlug(subCategory.getSlug() + "-" + productSlug);
        product.setReference(String.format("REA-%05d", referenceCounter++));
        product.setDescription(productName + " — gamme " + subCategory.getName().toLowerCase()
                + " proposée par REA Express pour laboratoires, hôpitaux et centres de santé. "
                + "Qualité professionnelle, conseil technique et service après-vente.");
        product.setTechnicalInfo("Spécifications, conditionnement et disponibilité communiqués sur demande de devis.");
        product.setImageUrl(imageUrl);
        // Marque, référence fabricant et unité de vente restent à renseigner par
        // l'admin avec les vraies valeurs — inventer des marques serait trompeur.
        product.setAvailability("Sur devis");
        product.setSubCategory(subCategory);

        ProductImage principal = new ProductImage();
        principal.setUrl(imageUrl);
        principal.setType("principal");
        principal.setProduct(product);
        product.getImages().add(principal);

        return product;
    }
}
