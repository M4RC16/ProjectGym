import { Component, inject, OnInit } from '@angular/core';
import { CalendarEvent, EventDay, TimeSlot, Trainer } from '../../models/models.model';
import { FormsModule } from '@angular/forms';
import { RequestsService } from '../../services/requests.service';
import { AuthService } from '../../services/auth.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-calendar',
  imports: [FormsModule],
  templateUrl: './calendar.html',
  styleUrl: './calendar.css',
})
export class Calendar implements OnInit {
  private requestsService = inject(RequestsService);
  private auth = inject(AuthService);
  private alertService = inject(AlertService);
  currentUser = toSignal(this.auth.currentUser$);

  today = new Date();
  activeDay: number = 0;
  month: number = this.today.getMonth();
  year: number = this.today.getFullYear();

  months = [
    'Január',
    'Február',
    'Március',
    'Április',
    'Május',
    'Június',
    'Július',
    'Augusztus',
    'Szeptember',
    'Október',
    'November',
    'December',
  ];

  weekdays = ['Vasárnap', 'Hétfő', 'Kedd', 'Szerda', 'Csütörtök', 'Péntek', 'Szombat'];

  weekdaysShort = ['Hét', 'Ked', 'Sze', 'Csü', 'Pén', 'Szo', 'Vas'];

  days: any[] = [];
  eventsArr: EventDay[] = [];
  currentMonthYear: string = '';
  eventDayName: string = '';
  eventDateString: string = '';
  displayedEvents: CalendarEvent[] = [];
  showNoEvent: boolean = false;

  dateInput: string = '';
  addEventActive: boolean = false;

  // Booking
  trainers: Trainer[] = [];
  selectedTrainerId: string = '';
  timeSlots: TimeSlot[] = [];
  selectedTimeSlotId: number | null = null;
  loadingSlots: boolean = false;

  ngOnInit() {
    this.getEvents();
    this.initCalendar();
    this.loadTrainers();
  }

  initCalendar() {
    const firstDay = new Date(this.year, this.month, 1);
    const lastDay = new Date(this.year, this.month + 1, 0);
    const prevLastDay = new Date(this.year, this.month, 0);
    const prevDays = prevLastDay.getDate();
    const lastDate = lastDay.getDate();

    // shift to Monday-start: JS getDay() -> 0=Sun..6=Sat ; convert so 0=Mon..6=Sun
    const startDay = (firstDay.getDay() + 6) % 7; // number of prev-month days to show
    const endDay = (lastDay.getDay() + 6) % 7;
    const nextDays = 6 - endDay;

    this.currentMonthYear = this.months[this.month] + ' ' + this.year;
    this.days = [];

    for (let x = startDay; x > 0; x--) {
      this.days.push({
        day: prevDays - x + 1,
        type: 'prev-date',
        isToday: false,
        isActive: false,
        hasEvent: false,
      });
    }

    for (let i = 1; i <= lastDate; i++) {
      const hasEvent = this.eventsArr.some(
        (e) => e.day === i && e.month === this.month + 1 && e.year === this.year,
      );

      const isToday =
        i === new Date().getDate() &&
        this.year === new Date().getFullYear() &&
        this.month === new Date().getMonth();

      if (isToday) {
        this.activeDay = i;
        this.getActiveDay(i);
        this.updateEvents(i);
      }

      this.days.push({
        day: i,
        type: 'current',
        isToday: isToday,
        isActive: isToday,
        hasEvent: hasEvent,
      });
    }

    for (let j = 1; j <= nextDays; j++) {
      this.days.push({
        day: j,
        type: 'next-date',
        isToday: false,
        isActive: false,
        hasEvent: false,
      });
    }
  }

  prevMonth() {
    this.month--;
    if (this.month < 0) {
      this.month = 11;
      this.year--;
    }
    this.initCalendar();
  }

  nextMonth() {
    this.month++;
    if (this.month > 11) {
      this.month = 0;
      this.year++;
    }
    this.initCalendar();
  }

  onDayClick(dayObj: any) {
    if (dayObj.type === 'prev-date') {
      this.prevMonth();
      setTimeout(() => {
        this.activeDay = dayObj.day;
        this.setActiveDayByNumber(dayObj.day);
      }, 0);
    } else if (dayObj.type === 'next-date') {
      this.nextMonth();
      setTimeout(() => {
        this.activeDay = dayObj.day;
        this.setActiveDayByNumber(dayObj.day);
      }, 0);
    } else {
      this.days.forEach((d) => (d.isActive = false));
      dayObj.isActive = true;
      this.activeDay = dayObj.day;
      this.getActiveDay(dayObj.day);
      this.updateEvents(dayObj.day);
    }
  }

  setActiveDayByNumber(dayNumber: number) {
    this.days.forEach((d) => {
      if (d.day === dayNumber && d.type === 'current') {
        d.isActive = true;
      }
    });
    this.getActiveDay(dayNumber);
    this.updateEvents(dayNumber);
  }

  goToToday() {
    this.today = new Date();
    this.month = this.today.getMonth();
    this.year = this.today.getFullYear();
    this.initCalendar();
  }

/*   onDateInputChange() {
    this.dateInput = this.dateInput.replace(/[^0-9/]/g, '');
    if (this.dateInput.length === 2 && !this.dateInput.includes('/')) {
      this.dateInput += '/';
    }
    if (this.dateInput.length > 7) {
      this.dateInput = this.dateInput.slice(0, 7);
    }
  }

  gotoDate() {
    const dateArr = this.dateInput.split('/');
    if (dateArr.length === 2) {
      const inputMonth = parseInt(dateArr[0]);
      const inputYear = dateArr[1];
      if (inputMonth > 0 && inputMonth < 13 && inputYear.length === 4) {
        this.month = inputMonth - 1;
        this.year = parseInt(inputYear);
        this.initCalendar();
        return;
      }
    }
    alert('Invalid Date');
  } */

  getActiveDay(date: number) {
    const day = new Date(this.year, this.month, date);
    /*     const dayName = day.toString().split(' ')[0];
    this.eventDayName = dayName; */
    this.eventDayName = this.weekdays[day.getDay()];
    this.eventDateString = date + ' ' + this.months[this.month] + ' ' + this.year;
  }

  updateEvents(date: number) {
    this.displayedEvents = [];
    this.eventsArr.forEach((event) => {
      if (date === event.day && this.month + 1 === event.month && this.year === event.year) {
        this.displayedEvents.push(...event.events);
      }
    });
    this.showNoEvent = this.displayedEvents.length === 0;
    this.saveEvents();
  }

  toggleAddEvent() {
    this.addEventActive = !this.addEventActive;
  }

  closeAddEvent() {
    this.addEventActive = false;
    this.resetBookingForm();
  }

  loadTrainers() {
    this.requestsService.getTrainers().subscribe({
      next: (trainers) => this.trainers = trainers,
      error: (err) => console.error('Edzők betöltése sikertelen:', err)
    });
  }

  onTrainerSelect() {
    this.selectedTimeSlotId = null;
    this.timeSlots = [];

    if (!this.selectedTrainerId) return;

    const dateStr = `${this.year}-${String(this.month + 1).padStart(2, '0')}-${String(this.activeDay).padStart(2, '0')}`;
    this.loadingSlots = true;

    this.requestsService.getTrainerTimeSlots(this.selectedTrainerId, dateStr).subscribe({
      next: (slots) => {
        this.timeSlots = slots;
        this.loadingSlots = false;
      },
      error: (err) => {
        console.error('Időpontok betöltése sikertelen:', err);
        this.loadingSlots = false;
      }
    });
  }

  addBooking() {
    if (!this.selectedTrainerId || !this.selectedTimeSlotId) return;

    const trainer = this.trainers.find(t => t.id === this.selectedTrainerId);
    if (!trainer) return;

    const dateStr = `${this.year}-${String(this.month + 1).padStart(2, '0')}-${String(this.activeDay).padStart(2, '0')}`;
    const selectedSlot = this.timeSlots.find(s => s.id === this.selectedTimeSlotId);

    this.requestsService.createBooking({
      trainerId: trainer.id,
      trainerName: trainer.name,
      timeSlotId: this.selectedTimeSlotId,
      date: dateStr
    }).subscribe({
      next: () => {
        // Event hozzáadása a naptárhoz
        const newEvent: CalendarEvent = {
          title: trainer.name,
          time: selectedSlot ? selectedSlot.time : ''
        };

        let eventAdded = false;
        this.eventsArr.forEach(item => {
          if (item.day === this.activeDay && item.month === this.month + 1 && item.year === this.year) {
            item.events.push(newEvent);
            eventAdded = true;
          }
        });

        if (!eventAdded) {
          this.eventsArr.push({
            day: this.activeDay,
            month: this.month + 1,
            year: this.year,
            events: [newEvent]
          });
        }

        const activeDayObj = this.days.find(d => d.isActive);
        if (activeDayObj) activeDayObj.hasEvent = true;

        this.updateEvents(this.activeDay);
        this.closeAddEvent();
      },
      error: (err) => {
        console.error('Foglalás sikertelen:', err);
        this.alertService.error('Foglalás sikertelen!');
      }
    });
  }

  resetBookingForm() {
    this.selectedTrainerId = '';
    this.selectedTimeSlotId = null;
    this.timeSlots = [];
  }

  deleteEvent(event: CalendarEvent) {
    if (confirm('Are you sure you want to delete this event?')) {
      this.eventsArr.forEach((eventDay) => {
        if (
          eventDay.day === this.activeDay &&
          eventDay.month === this.month + 1 &&
          eventDay.year === this.year
        ) {
          const index = eventDay.events.findIndex((e) => e.title === event.title);
          if (index > -1) {
            eventDay.events.splice(index, 1);
          }
          if (eventDay.events.length === 0) {
            const dayIndex = this.eventsArr.indexOf(eventDay);
            this.eventsArr.splice(dayIndex, 1);
            const activeDayObj = this.days.find((d) => d.isActive);
            if (activeDayObj) {
              activeDayObj.hasEvent = false;
            }
          }
        }
      });
      this.updateEvents(this.activeDay);
    }
  }

  saveEvents() {
    localStorage.setItem('events', JSON.stringify(this.eventsArr));
  }

  getEvents() {
    const events = localStorage.getItem('events');
    if (events) {
      this.eventsArr = JSON.parse(events);
    }
  }

  convertTime(time: string): string {
    const timeArr = time.split(':');
    const timeHour = timeArr[0].padStart(2, '0');
    const timeMin = timeArr[1];
    return timeHour + ':' + timeMin;
  }
}
