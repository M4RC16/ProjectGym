import { Component, EventEmitter, Input, Output } from '@angular/core';
import { type Trainer } from '../../models/user.model';


@Component({
  selector: 'app-trainer-card',
  imports: [],
  templateUrl: './trainer-card.html',
  styleUrl: './trainer-card.css',
})
export class TrainerCard {

  @Input({required:true}) trainer!: Trainer;
  @Output() viewProfile = new EventEmitter<Trainer>();

  readonly fallbackImage = '/assets/trainers/NAI.jpg';

  onOpenProfile() {
    this.viewProfile.emit(this.trainer);
  }

  onImageError(event: Event) {
    const image = event.target as HTMLImageElement;
    image.src = this.fallbackImage;
  }

}
