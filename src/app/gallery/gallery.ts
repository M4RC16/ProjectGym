import { Component, Input } from '@angular/core';
import { Img } from '../models/Img.model';

@Component({
  selector: 'app-gallery',
  imports: [],
  templateUrl: './gallery.html',
  styleUrl: './gallery.css',
})
export class Gallery {
/*   @Input({ required: true }) ImgData!: Img[]; */

ImgData = [
  { imgId: 1, imgUrl: "https://picsum.photos/300/400", imgAlt: "Álló kép 1" },
  { imgId: 2, imgUrl: "https://picsum.photos/400/300", imgAlt: "Fekvő kép 2" },
  { imgId: 3, imgUrl: "https://picsum.photos/400/400", imgAlt: "Négyzet kép 3" },
  { imgId: 4, imgUrl: "https://picsum.photos/350/500", imgAlt: "Álló kép 4" },
  { imgId: 5, imgUrl: "https://picsum.photos/500/350", imgAlt: "Fekvő kép 5" },
  { imgId: 6, imgUrl: "https://placeholdpicsum.dev/photo/450/450", imgAlt: "Négyzet kép 6" },
  { imgId: 7, imgUrl: "https://placeholdpicsum.dev/photo/300/500", imgAlt: "Álló kép 7" },
  { imgId: 8, imgUrl: "https://placeholdpicsum.dev/photo/500/300", imgAlt: "Fekvő kép 8" },
  { imgId: 9, imgUrl: "https://picsum.photos/320/480", imgAlt: "Álló kép 9" },
  { imgId: 10, imgUrl: "https://picsum.photos/480/320", imgAlt: "Fekvő kép 10" },
  { imgId: 11, imgUrl: "https://placeholdpicsum.dev/photo/600/600", imgAlt: "Négyzet kép 11" },
  { imgId: 12, imgUrl: "https://picsum.photos/280/420", imgAlt: "Álló kép 12" },
  { imgId: 13, imgUrl: "https://picsum.photos/420/280", imgAlt: "Fekvő kép 13" },
  { imgId: 14, imgUrl: "https://picsum.photos/360/360", imgAlt: "Négyzet kép 14" },
  { imgId: 15, imgUrl: "https://placeholdpicsum.dev/photo/380/570", imgAlt: "Álló kép 15" }

  ]

  previewImage = false;
  currentLightboxImg!: Img;
  currentIndex = 0;
  totalImageCount!: number;

  ngOnInit(): void {
    this.currentLightboxImg = this.ImgData[0];
    this.totalImageCount = this.ImgData.length;
  }

  // Kép megjelenítése nagyban
  onPreview(index: number) {
    this.previewImage = true;
    this.currentIndex = index;
    this.currentLightboxImg = this.ImgData[index];
  }

  onClose() {
    this.previewImage = false;
  }

  onNext() {
    this.currentIndex += 1;
    if (this.currentIndex + 1 > this.totalImageCount) {
      this.currentIndex = 0;
    }
    this.currentLightboxImg = this.ImgData[this.currentIndex];
  }

  onPrevious() {
    this.currentIndex -= 1;
    if (this.currentIndex < 0) {
      this.currentIndex = this.totalImageCount - 1;
    }
    this.currentLightboxImg = this.ImgData[this.currentIndex];
  }
}
