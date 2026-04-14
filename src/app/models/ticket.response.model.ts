export interface Ticket {
  id: number;
  price: number;
  ticketName: string;
  unit: string;
  validityLength: number;
}

export interface addTicket {
  price: number;
  ticketName: string;
  unit: string;
  validityLength: number;
}