import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class GalleryService {
  private readonly baseUrl = environment.apiUrl;

  private httpClient = inject(HttpClient);


  getAllGalleryImg() {
    return this.httpClient.get<string[]>(`${this.baseUrl}/api/gallery/getAll/image`, {
      withCredentials: true,
    });
  }

  addGalleryImg(payLoad: File, altText: string) {
    const formData = new FormData();
    formData.append('file', payLoad);
    formData.append('text', altText);
    return this.httpClient.post(`${this.baseUrl}/api/gallery/add`, formData, {
      withCredentials: true,
    });
  }

  deleteGalleryImg(imageUrl: string) {
    return this.httpClient.delete(`${this.baseUrl}/api/gallery/delete`, {
      withCredentials: true,
      params: { imageUrl },
    });
  }
}
