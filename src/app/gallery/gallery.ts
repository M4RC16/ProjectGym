import { Component, Inject, Renderer2, inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { type Img } from '../models/img.model';
import { AuthService } from '../services/auth.service';
import { AlertService } from '../services/alert.service';
import { GalleryService } from '../services/gallery.service';

@Component({
  selector: 'app-gallery',
  imports: [],
  templateUrl: './gallery.html',
  styleUrl: './gallery.css',
})
export class Gallery {
  /*   @Input({ required: true }) ImgData!: Img[]; */
  private auth = inject(AuthService);
  private alertService = inject(AlertService);
  private galleryService = inject(GalleryService);
  currentUser = toSignal(this.auth.currentUser$);

  constructor(
    private renderer: Renderer2,
    @Inject(DOCUMENT) private document: Document,
  ) {}

  ImgData: Img[] = [];

  previewImage = false;
  currentLightboxImg!: Img;
  currentIndex = 0;
  totalImageCount = 0;

  ngOnInit(): void {
    this.loadImages();
  }

  loadImages() {
    this.galleryService.getAllGalleryImg().subscribe({
      next: (imageUrls) => {
        this.ImgData = this.mapAndSortImages(imageUrls ?? []);
        this.totalImageCount = this.ImgData.length;
        this.currentIndex = 0;

        if (this.totalImageCount > 0) {
          this.currentLightboxImg = this.ImgData[0];
        }
      },
      error: (err) => {
        console.error('Galéria képek betöltése sikertelen:', err);
        this.alertService.error('A galéria képek betöltése sikertelen!');
      },
    });
  }

  private mapAndSortImages(imageUrls: string[]): Img[] {
    return [...imageUrls]
      .sort((a, b) => a.localeCompare(b))
      .map((imgUrl, index) => ({
        imgId: index + 1,
        imgUrl,
        imgAlt: `Galéria kép ${index + 1}`,
      }));
  }

  // Kép megjelenítése nagyban
  onPreview(index: number) {
    if (!this.totalImageCount) return;

    this.previewImage = true;
    this.currentIndex = index;
    this.currentLightboxImg = this.ImgData[index];
    this.renderer.addClass(this.document.body, 'no-scroll');
  }

  onClose() {
    this.previewImage = false;
    this.renderer.removeClass(this.document.body, 'no-scroll');
  }

  onNext() {
    if (!this.totalImageCount) return;

    this.currentIndex += 1;
    if (this.currentIndex + 1 > this.totalImageCount) {
      this.currentIndex = 0;
    }
    this.currentLightboxImg = this.ImgData[this.currentIndex];
  }

  onPrevious() {
    if (!this.totalImageCount) return;

    this.currentIndex -= 1;
    if (this.currentIndex < 0) {
      this.currentIndex = this.totalImageCount - 1;
    }
    this.currentLightboxImg = this.ImgData[this.currentIndex];
  }

  isAdmin(): boolean {
    return this.currentUser()?.role?.some((role) => role.id === 1) ?? false;
  }

  async onDeleteImage(image: Img, event: Event) {
    event.stopPropagation();

    const confirmed = await this.alertService.confirm('Biztosan törölni szeretnéd ezt a képet?');
    if (!confirmed) return;

    const imageIndex = this.ImgData.findIndex((img) => img.imgId === image.imgId);
    if (imageIndex === -1) return;

    const remainingImages = this.ImgData.filter((img) => img.imgId !== image.imgId);
    this.ImgData = remainingImages.map((img, index) => ({ ...img, imgId: index + 1 }));
    this.totalImageCount = this.ImgData.length;

    this.galleryService.deleteGalleryImg(image.imgUrl).subscribe({
      next: () => {
        this.alertService.success('Kép sikeresen törölve!');
      },
      error: (err) => {
        console.error('Kép törlése sikertelen:', err);
        this.alertService.error('Kép törlése sikertelen!');
      },
    });

    if (this.totalImageCount === 0) {
      this.previewImage = false;
      this.renderer.removeClass(this.document.body, 'no-scroll');
      return;
    }

    if (this.previewImage) {
      if (imageIndex < this.currentIndex) {
        this.currentIndex -= 1;
      } else if (imageIndex === this.currentIndex) {
        this.currentIndex = this.currentIndex % this.totalImageCount;
      }

      this.currentLightboxImg = this.ImgData[this.currentIndex];
    }
  }
}
