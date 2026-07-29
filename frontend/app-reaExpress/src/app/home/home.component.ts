import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  @ViewChild('galleryTrack') galleryTrack?: ElementRef<HTMLDivElement>;

  galleryImages = [
    { src: '/assets/images/laboratoire/lab8.PNG', alt: 'Laboratoire' },
    { src: '/assets/images/Imagerie/imm1.PNG', alt: 'Imagerie' },
    { src: '/assets/images/laboratoire/lab13.PNG', alt: 'Matériel labo' },
    { src: '/assets/images/Sang/sang8.PNG', alt: 'Banque de sang' },
    { src: '/assets/images/laboratoire/lab6.PNG', alt: 'Analyseur' },
    { src: '/assets/images/Sang/sang14.PNG', alt: 'Transfusion' },
    { src: '/assets/images/Pcr/pcrD1.PNG', alt: 'PCR' },
    { src: '/assets/images/Pcr/pcrE3.PNG', alt: 'Kit PCR' },
    { src: '/assets/images/Pcr/pcrR1.PNG', alt: 'Réactif PCR' }
  ];

  private autoScrollId?: ReturnType<typeof setInterval>;

  ngOnInit(): void {
    this.autoScrollId = setInterval(() => this.scrollGallery(1), 3500);
  }

  ngOnDestroy(): void {
    if (this.autoScrollId) {
      clearInterval(this.autoScrollId);
    }
  }

  scrollGallery(direction: number): void {
    const el = this.galleryTrack?.nativeElement;
    if (!el) {
      return;
    }
    const step = Math.min(300, el.clientWidth * 0.75);
    const max = el.scrollWidth - el.clientWidth;
    let next = el.scrollLeft + direction * step;
    if (next > max - 8) {
      next = 0;
    }
    if (next < 0) {
      next = max;
    }
    el.scrollTo({ left: next, behavior: 'smooth' });
  }
}
