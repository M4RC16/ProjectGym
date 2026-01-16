import { Component, Input, OnInit} from '@angular/core';
import { type Img } from '../../models/Img.model';

@Component({
  selector: 'app-img-slider',
  imports: [],
  templateUrl: './img-slider.html',
  styleUrl: './img-slider.css',
})

export class ImgSlider {

  @Input({required: true}) ImgData !:Img[];

  previewImage = false;
  currentLightboxImg !: Img;
  currentIndex = 0;
  totalImageCount !: number;

  ngOnInit(): void {
    this.currentLightboxImg = this.ImgData[0];
    this.totalImageCount = this.ImgData.length;
  }


  // Kép megjelenítése nagyban
  onPreview(index: number){
    this.previewImage = true;
    this.currentIndex = index;
    this.currentLightboxImg = this.ImgData[index];
  };

  onClose(){
    this.previewImage = false;
  }

  onNext(){
    this.currentIndex += 1;
    if (this.currentIndex+1 > this.totalImageCount) {
      this.currentIndex = 0;
    }
    this.currentLightboxImg = this.ImgData[this.currentIndex];
  };

  onPrevious(){
    this.currentIndex -= 1;
    if (this.currentIndex < 0) {
      this.currentIndex = this.totalImageCount-1;
    }
    this.currentLightboxImg = this.ImgData[this.currentIndex];
  } 


}
