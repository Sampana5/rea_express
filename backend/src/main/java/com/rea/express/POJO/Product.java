package com.rea.express.POJO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "products")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique = true)
    private String reference;

    /**
     * Image principale (chemin relatif servi par le frontend, ex:
     * /assets/images/products/laboratoire/biochimie/reactifs.jpg).
     * On ne stocke jamais le binaire en base — uniquement le chemin.
     * Compatible avec une future migration vers une URL absolue (S3, CDN...).
     */
    @Column(name = "image_url")
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String technicalInfo;

    /** Marque / fabricant (Roche, Siemens, Abbott...). */
    @Column
    private String brand;

    /** Référence catalogue du fabricant (différente de la référence interne REA). */
    @Column(name = "reference_manufacturer")
    private String referenceManufacturer;

    /** Unité de vente : boîte, kit, flacon, paquet... */
    @Column(name = "unit_of_sale")
    private String unitOfSale;

    /** Disponibilité : En stock, Sur commande, Sur devis... */
    @Column
    private String availability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDocument> documents = new ArrayList<>();
}
