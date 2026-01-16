import { Component, inject } from '@angular/core';
import { TrainerCard } from './trainer-card/trainer-card';
import { TrainerService } from '../../services/trainer.service';

@Component({
  selector: 'app-trainers',
  imports: [TrainerCard],
  templateUrl: './trainers.html',
  styleUrl: './trainers.css',
})
export class Trainers {

  private TrainerService = inject(TrainerService);

  Trainers = this.TrainerService.getFirstThreeTrainers();



}
