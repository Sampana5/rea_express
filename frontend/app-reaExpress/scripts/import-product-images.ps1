<#
    Script d'import initial des images produits - REA Express
    ----------------------------------------------------------
    Genere l'arborescence src/assets/images/products/{categorie}/{sous-categorie}/{produit}.jpg
    a partir des images existantes du projet (laboratoire/, Sang/, Pcr/).

    Bonne pratique respectee : les fichiers restent dans les assets (ou un stockage
    cloud en prod), la base MySQL ne stocke que le CHEMIN (image_url). Ce script se
    contente de placer un visuel par produit ; remplacez ensuite chaque fichier par la
    vraie photo professionnelle (meme nom) sans rien changer d'autre.

    Les regles de slug sont IDENTIQUES a com.rea.express.utils.SlugUtils cote backend,
    afin que les chemins generes correspondent exactement a ceux enregistres en base.

    Usage (depuis frontend/app-reaExpress) :
        powershell -ExecutionPolicy Bypass -File .\scripts\import-product-images.ps1
    Option -Force pour ecraser les images deja presentes.
#>

param(
    [switch]$Force
)

$ErrorActionPreference = "Stop"

# Racine assets (le script vit dans frontend/app-reaExpress/scripts)
$assetsRoot = Join-Path $PSScriptRoot "..\src\assets\images"
$assetsRoot = [System.IO.Path]::GetFullPath($assetsRoot)
$productsRoot = Join-Path $assetsRoot "products"

function ConvertTo-Slug([string]$text) {
    if ([string]::IsNullOrWhiteSpace($text)) { return "" }
    $normalized = $text.Normalize([Text.NormalizationForm]::FormD)
    $sb = New-Object System.Text.StringBuilder
    foreach ($ch in $normalized.ToCharArray()) {
        $cat = [Globalization.CharUnicodeInfo]::GetUnicodeCategory($ch)
        if ($cat -ne [Globalization.UnicodeCategory]::NonSpacingMark) {
            [void]$sb.Append($ch)
        }
    }
    $s = $sb.ToString()
    $apostrophe = [char]0x2019
    $s = $s -replace "'", " "
    $s = $s -replace $apostrophe, " "
    $s = $s -replace "&", " et "
    $s = $s -replace "\s+", "-"
    $s = $s -replace "[^\w-]", ""
    $s = $s -replace "-{2,}", "-"
    $s = $s.Trim('-')
    return $s.ToLowerInvariant()
}

function Get-SourcePool([string]$categorySlug) {
    switch ($categorySlug) {
        "banque-de-sang" { return (Get-ChildItem (Join-Path $assetsRoot "Sang") -Filter *.PNG -ErrorAction SilentlyContinue) }
        "pcr"            { return (Get-ChildItem (Join-Path $assetsRoot "Pcr")  -Filter *.PNG -ErrorAction SilentlyContinue) }
        default          { return (Get-ChildItem (Join-Path $assetsRoot "laboratoire") -Filter *.PNG -ErrorAction SilentlyContinue) }
    }
}

# Catalogue - DOIT rester synchronise avec CatalogDataLoader.java
$catalog = [ordered]@{
    "Laboratoire" = [ordered]@{
        "Accessoires"        = @("Pipettes", "Tubes", "Portoirs", "Consommable")
        "Bacteriologie"      = @("Milieu de culture", "Colorant", "Disque d'antibiogramme", "Identification")
        "Biochimie"          = @("Reactifs", "Controle", "Electrophorese", "Consommable biochimie")
        "Consommable"        = @("Gants", "Masques", "Seringues", "Divers")
        "Electrophorese"     = @("Kits electrophorese", "Tampons", "Accessoires electrophorese")
        "Equipement"         = @("Analyseurs", "Centrifugeuses", "Microscopes")
        "Hematologie"        = @("Reactifs hematologie", "Coagulation", "Transfusion sanguine", "Groupage")
        "Immunologie"        = @("Reactifs immunologie", "Controles immunologie", "Kits rapides")
        "Ionogramme"         = @("Reactifs ionogramme", "Electrodes", "Controles ionogramme")
        "Parasitologie"      = @("Colorants parasitologie", "Kits parasitologie", "Accessoires parasitologie")
        "PCR laboratoire"    = @("Kits PCR", "Reactifs PCR", "Consommables PCR")
        "Produits chimiques" = @("Produits chimiques", "Colorant chimique", "Accessoire chimie", "Microscopie")
        "Serologie"          = @("Kits serologie", "Reactifs serologie", "Controles serologie")
        "Urinaire"           = @("Bandelettes", "Reactifs urinaire", "Controles urinaire")
    }
    "Medecine" = [ordered]@{
        "Diagnostic"            = @("Tensiometres", "Stethoscopes", "Thermometres")
        "Consommables medicaux" = @("Compresses", "Pansements", "Gants d'examen")
    }
    "Banque de sang" = [ordered]@{
        "Poches de sang"       = @("Poches simples", "Poches doubles", "Poches triples")
        "Reactifs de groupage" = @("Serums tests", "Cartes de groupage", "Reactifs Coombs")
    }
    "PCR" = [ordered]@{
        "Extraction"    = @("Kits d'extraction ADN", "Kits d'extraction ARN")
        "Amplification" = @("Master Mix", "Enzymes", "Consommables amplification")
    }
}

$created = 0
$skipped = 0

foreach ($categoryName in $catalog.Keys) {
    $categorySlug = ConvertTo-Slug $categoryName
    $pool = @(Get-SourcePool $categorySlug)
    if ($pool.Count -eq 0) {
        Write-Warning "Aucune image source pour la categorie '$categoryName' - ignoree."
        continue
    }
    $poolIndex = 0
    $subMap = $catalog[$categoryName]
    foreach ($subName in $subMap.Keys) {
        $subSlug = ConvertTo-Slug $subName
        $targetDir = Join-Path $productsRoot (Join-Path $categorySlug $subSlug)
        if (-not (Test-Path $targetDir)) {
            New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
        }
        foreach ($productName in $subMap[$subName]) {
            $productSlug = ConvertTo-Slug $productName
            $target = Join-Path $targetDir "$productSlug.jpg"
            if ((Test-Path $target) -and (-not $Force)) {
                $skipped++
            } else {
                $source = $pool[$poolIndex % $pool.Count]
                Copy-Item -Path $source.FullName -Destination $target -Force
                $created++
            }
            $poolIndex++
        }
    }
}

Write-Host "Images produits generees : $created cree(s), $skipped ignore(s)."
Write-Host "Dossier : $productsRoot"
