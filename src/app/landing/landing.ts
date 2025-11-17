import { Component } from '@angular/core';
import { Carousel } from "./carousel/carousel";

@Component({
  selector: 'app-landing',
  imports: [Carousel],
  templateUrl: './landing.html',
  styleUrl: './landing.css',
})
export class Landing {

}
