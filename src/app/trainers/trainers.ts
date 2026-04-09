import { Component, HostListener, inject } from '@angular/core';
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
  readonly fallbackImage = '/assets/trainers/NAI.jpg';

  trainers: Trainer[] = [];
  selectedTrainer: Trainer | null = null;
  isProfileModalOpen = false;

  ngOnInit() {
    this.TrainerService.getAllTrainer().subscribe(
      (response) => {
        this.trainers = response;
      },
      (error) => {
        console.error('Error fetching trainers:', error);
      }
    );
  }

  openProfile(trainer: Trainer) {
    this.selectedTrainer = trainer;
    this.isProfileModalOpen = true;
    document.body.style.overflow = 'hidden';
  }

  closeProfile() {
    this.isProfileModalOpen = false;
    this.selectedTrainer = null;
    document.body.style.overflow = '';
  }

  onModalImageError(event: Event) {
    const image = event.target as HTMLImageElement;
    image.src = this.fallbackImage;
  }

  @HostListener('document:keydown.escape')
  onEscapePress() {
    if (this.isProfileModalOpen) {
      this.closeProfile();
    }
  }

}
