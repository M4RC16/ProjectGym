import { Component } from '@angular/core';
import { NgbCarouselModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-carousel',
  imports: [NgbCarouselModule, CommonModule],
  templateUrl: './carousel.html',
  styleUrl: './carousel.css',
})

export class Carousel {

  images = [
    'assets/kep2.jpg',
    'assets/kep3.jpg',
    'assets/kep1.jpg'
  ];
  currentIndex = 0;

  prev() {
    this.currentIndex = (this.currentIndex === 0) ? this.images.length - 1 : this.currentIndex - 1;
  }

  next() {
    this.currentIndex = (this.currentIndex === this.images.length - 1) ? 0 : this.currentIndex + 1;
  }

}
