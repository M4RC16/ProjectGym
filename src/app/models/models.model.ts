export interface Img {
  imgId: number;
  imgUrl: string;
  imgAlt: string;
}

export interface Trainer {
  id: string;
  name: string;
  shortDescription: string;
  description: string;
  imageUrl: string;
}

export interface User {
  id: number;
  name: string;
  role: {
    id: number;
    name: string;
  };
}

export interface loginData {
  email: string;
  password: string;
  browserOSType: string;
}

export interface loginResponse {
  JWTtoken: string;
  email: string;
  message: string;
}

export interface registerData{
  email: string;
  password: string;
/*   passwordConfirmation: string; */
}

export interface CalendarEvent {
  title: string;
  time: string;
}

export interface EventDay {
  day: number;
  month: number;
  year: number;
  events: CalendarEvent[];
}

