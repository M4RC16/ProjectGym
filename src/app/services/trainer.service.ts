import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class TrainerService {

    private trainers = [
        {
            id: '1',
            name: 'John Doe',
            shortDescription: 'Certified Personal Trainer',
            description: 'Expert in strength training and nutrition.',
            imageUrl: 'assets/trainers/NAI.jpg'
        },
        {
            id: '2',
            name: 'Jane Smith',
            shortDescription: 'Yoga Instructor',
            description: 'Specializes in Vinyasa and Hatha yoga styles.',
            imageUrl: 'assets/trainers/NAI.jpg'
        },
        {
            id: '3',
            name: 'Mike Johnson',
            shortDescription: 'Cardio Specialist',
            description: 'Focuses on high-intensity interval training.',
            imageUrl: 'assets/trainers/NAI.jpg'
        },
        {
            id: '4',
            name: 'Mike Johnson',
            shortDescription: 'Cardio Specialist',
            description: 'Focuses on high-intensity interval training.',
            imageUrl: 'assets/trainers/NAI.jpg'
        }
    ];

    getFirstThreeTrainers() {
        return [...this.trainers].slice(0, 3);
    }


}