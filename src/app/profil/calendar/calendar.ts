import { Component, OnInit } from '@angular/core';
import { CalendarEvent, EventDay } from '../../models/models.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-calendar',
  imports: [CommonModule, FormsModule],
  templateUrl: './calendar.html',
  styleUrl: './calendar.css',
})
export class Calendar implements OnInit {
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
  eventName: string = '';
  eventTimeFrom: string = '';
  eventTimeTo: string = '';

  ngOnInit() {
    this.getEvents();
    this.initCalendar();
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

  onDateInputChange() {
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
  }

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
  }

  onEventNameInput() {
    if (this.eventName.length > 60) {
      this.eventName = this.eventName.slice(0, 60);
    }
  }

  onEventTimeFromInput() {
    this.eventTimeFrom = this.eventTimeFrom.replace(/[^0-9:]/g, '');
    if (this.eventTimeFrom.length === 2 && !this.eventTimeFrom.includes(':')) {
      this.eventTimeFrom += ':';
    }
    if (this.eventTimeFrom.length > 5) {
      this.eventTimeFrom = this.eventTimeFrom.slice(0, 5);
    }
  }

  onEventTimeToInput() {
    this.eventTimeTo = this.eventTimeTo.replace(/[^0-9:]/g, '');
    if (this.eventTimeTo.length === 2 && !this.eventTimeTo.includes(':')) {
      this.eventTimeTo += ':';
    }
    if (this.eventTimeTo.length > 5) {
      this.eventTimeTo = this.eventTimeTo.slice(0, 5);
    }
  }

  addEvent() {
    if (this.eventName === '' || this.eventTimeFrom === '' || this.eventTimeTo === '') {
      alert('Please fill all the fields');
      return;
    }

    const timeFromArr = this.eventTimeFrom.split(':');
    const timeToArr = this.eventTimeTo.split(':');
    if (
      timeFromArr.length !== 2 ||
      timeToArr.length !== 2 ||
      parseInt(timeFromArr[0]) > 23 ||
      parseInt(timeFromArr[1]) > 59 ||
      parseInt(timeToArr[0]) > 23 ||
      parseInt(timeToArr[1]) > 59
    ) {
      alert('Invalid Time Format');
      return;
    }

    const timeFrom = this.convertTime(this.eventTimeFrom);
    const timeTo = this.convertTime(this.eventTimeTo);

    let eventExist = false;
    this.eventsArr.forEach((event) => {
      if (
        event.day === this.activeDay &&
        event.month === this.month + 1 &&
        event.year === this.year
      ) {
        event.events.forEach((e) => {
          if (e.title === this.eventName) {
            eventExist = true;
          }
        });
      }
    });

    if (eventExist) {
      alert('Event already added');
      return;
    }

    const newEvent: CalendarEvent = {
      title: this.eventName,
      time: timeFrom + ' - ' + timeTo,
    };

    let eventAdded = false;
    if (this.eventsArr.length > 0) {
      this.eventsArr.forEach((item) => {
        if (
          item.day === this.activeDay &&
          item.month === this.month + 1 &&
          item.year === this.year
        ) {
          item.events.push(newEvent);
          eventAdded = true;
        }
      });
    }

    if (!eventAdded) {
      this.eventsArr.push({
        day: this.activeDay,
        month: this.month + 1,
        year: this.year,
        events: [newEvent],
      });
    }

    this.addEventActive = false;
    this.eventName = '';
    this.eventTimeFrom = '';
    this.eventTimeTo = '';
    this.updateEvents(this.activeDay);

    const activeDayObj = this.days.find((d) => d.isActive);
    if (activeDayObj && !activeDayObj.hasEvent) {
      activeDayObj.hasEvent = true;
    }
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
    let timeHour = parseInt(timeArr[0]);
    const timeMin = timeArr[1];
    const timeFormat = timeHour >= 12 ? 'PM' : 'AM';
    timeHour = timeHour % 12 || 12;
    return timeHour + ':' + timeMin + ' ' + timeFormat;
  }
}
