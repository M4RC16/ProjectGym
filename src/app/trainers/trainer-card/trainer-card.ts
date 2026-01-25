import { Component, Input } from '@angular/core';
import { Trainer } from '../../models/trainer.model';

@Component({
  selector: 'app-trainer-card',
  imports: [],
  templateUrl: './trainer-card.html',
  styleUrl: './trainer-card.css',
})
export class TrainerCard {

  @Input({required:true}) trainer!: Trainer;

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

}
