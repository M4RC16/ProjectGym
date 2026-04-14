import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { ChangePasswordRequest } from '../models/auth.model';
import { message, UpdateUserNameRequest, User, UserFormData } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private baseUrl = environment.apiUrl;
  private httpClient = inject(HttpClient);

  getAllUsers() {
    return this.httpClient.get<User[]>(`${this.baseUrl}/api/user/requests/getAllUser`, {
      withCredentials: true,
    });
  }

  deleteUser(email: string) {
    return this.httpClient.delete(`${this.baseUrl}/api/user/delete/${email}`, {
      withCredentials: true,
    });
  }

  updateUserRole(id: number, roleId: number) {
    return this.httpClient.post(
      this.baseUrl + `/api/user/change/role`,
      { id, roleId },
      { withCredentials: true },
    );
  }

  getUserById(id: number) {
    return this.httpClient.get<User>(`${this.baseUrl}/api/user/requests/userById/${id}`, {
      withCredentials: true,
    });
  }

  getProfilPicture(){

  }

  updateUserName(data: UpdateUserNameRequest) {
    return this.httpClient.post(`${this.baseUrl}/api/user/change/name`, data, {
      withCredentials: true,
    });
  }

  updatePhoneNumber(payLoad: {number: string} ) {
    return this.httpClient.post(`${this.baseUrl}/api/user/change/number`, payLoad, {
      withCredentials: true,
    });
  }

  changePassword(data: ChangePasswordRequest) {
    return this.httpClient.post(`${this.baseUrl}/api/user/resetPassword`, data, {
      withCredentials: true,
    });
  }

  changeProfilPicture(payLoad: File){
    const formData = new FormData();
    formData.append('file', payLoad);
    return this.httpClient.post(`${this.baseUrl}/api/user/change/pfp`, formData, {
      withCredentials: true,
    });
  }

  changeDescription(payLoad: string){
    return this.httpClient.put(`${this.baseUrl}/api/user/change/description`,{"description":payLoad}, {
      withCredentials: true,
    });
  }

  changeWage(payLoad: number){
    return this.httpClient.put(`${this.baseUrl}/api/user/change/wage`,payLoad, {
      withCredentials: true,
    });
  }

  sendUserForm(data: UserFormData){
    return this.httpClient.post(`${this.baseUrl}/api/form/ContactForm`, data, {
      withCredentials: true,
      responseType: 'text',
    });
  }

  getAllMessages() {
    return this.httpClient.get<message[]>(`${this.baseUrl}/api/form/get`);
  }

  deleteMessage(id: number){
    return this.httpClient.delete(`${this.baseUrl}/api/form/delete/${id}`, {
      withCredentials: true,
    });
  }

}


