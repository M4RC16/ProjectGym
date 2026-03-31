import { Component, Input } from '@angular/core';
import { type Trainer } from '../../models/models.model';


@Component({
  selector: 'app-trainer-card',
  imports: [],
  templateUrl: './trainer-card.html',
  styleUrl: './trainer-card.css',
})
export class TrainerCard {

  @Input({required:true}) trainer!: Trainer;

  readonly fallbackImage = '/assets/trainers/NAI.jpg';

  isOpen = false;

  open() {
    this.isOpen = true;
  }

  close() {
    this.isOpen = false;
  }

  toggle() {
    this.isOpen = !this.isOpen;
  }

  onImageError(event: Event) {
    const image = event.target as HTMLImageElement;
    image.src = this.fallbackImage;
  }

}
