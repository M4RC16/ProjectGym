import { Component } from '@angular/core';
import { Carousel } from './carousel/carousel';
import { Trainers } from './trainers/trainers';
import { ImgSlider } from './img-slider/img-slider';
import { type Img } from '../models/Img.model';

@Component({
  selector: 'app-landing',
  imports: [Carousel, /* Trainers, ImgSlider */],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing {
  imgData: Img[] = [
    {
      imgId: 0,
      imgUrl: 'assets/trainers/NAI.jpg',
      imgAlt: '1',
    },
    {
      imgId: 1,
      imgUrl: 'assets/kep3.jpg',
      imgAlt: '2',
    },
    {
      imgId: 2,
      imgUrl: 'assets/kep1.jpg',
      imgAlt: '3',
    },
    {
      imgId: 3,
      imgUrl: 'assets/kep2.jpg',
      imgAlt: '4',
    }, 
    {
      imgId: 4,
      imgUrl: 'assets/trainers/trainer1.jpg',
      imgAlt: '5',
    }, 
  ];
}
