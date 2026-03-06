import { Injectable, signal } from '@angular/core';

export type AlertType = 'success' | 'error';

export interface AlertData {
  message: string;
  type: AlertType;
}

export interface ConfirmData {
  message: string;
  resolve: (value: boolean) => void;
}

@Injectable({ providedIn: 'root' })
export class AlertService {
  alert = signal<AlertData | null>(null);
  confirmData = signal<ConfirmData | null>(null);
  private timeout: any;

  show(message: string, type: AlertType = 'success', duration = 4000) {
    clearTimeout(this.timeout);
    this.alert.set({ message, type });
    this.timeout = setTimeout(() => this.alert.set(null), duration);
  }

  success(message: string, duration = 4000) {
    this.show(message, 'success', duration);
  }

  error(message: string, duration = 4000) {
    this.show(message, 'error', duration);
  }

  dismiss() {
    clearTimeout(this.timeout);
    this.alert.set(null);
  }

  confirm(message: string): Promise<boolean> {
    return new Promise((resolve) => {
      this.confirmData.set({ message, resolve });
    });
  }

  confirmResolve(result: boolean) {
    const data = this.confirmData();
    if (data) {
      data.resolve(result);
      this.confirmData.set(null);
    }
  }
}
