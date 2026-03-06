import { Component, inject } from '@angular/core';
import { TrainerService } from '../services/trainer.service';
import { TrainerCard } from "./trainer-card/trainer-card";
import { Trainer } from '../models/models.model';

@Component({
  selector: 'app-trainers',
  imports: [TrainerCard],
  templateUrl: './trainers.html',
  styleUrl: './trainers.css',
})
export class Trainers {
  private TrainerService = inject(TrainerService);

  trainers: Trainer[] = [];

  ngOnInit() {
    this.TrainerService.getAllTrainer().subscribe(
      (response) => {
        this.trainers = response;
      },
      (error) => {
        console.error('Error fetching trainers:', error);
      }
    );}

}
