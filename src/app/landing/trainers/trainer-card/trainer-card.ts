import { Component, Input } from '@angular/core';
import { Trainer } from '../../../models/trainer.model';

@Component({
  selector: 'app-trainer-card',
  imports: [],
  templateUrl: './trainer-card.html',
  styleUrl: './trainer-card.css',
})
export class TrainerCard {

  @Input({required:true}) trainer!: Trainer;

}
