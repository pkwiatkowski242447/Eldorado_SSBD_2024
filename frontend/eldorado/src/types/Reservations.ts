import { format } from 'date-fns';

export enum ReservationStatus{
    AWAITING = "AWAITING",
    IN_PROGRESS = "IN_PROGRESS",
    COMPLETED_MANUALLY = "COMPLETED_MANUALLY",
    COMPLETED_AUTOMATICALLY = "COMPLETED_AUTOMATICALLY",
    CANCELLED = "CANCELLED",
    TERMINATED = "TERMINATED"
}

export interface ReservationType {
    beginTime: string,
    city: string,
    endingTime: string | null,
    id: string,
    sectorName: string,
    street: string,
    zipCode: string,
    status: ReservationStatus
}

export interface ClientReservationType {
    beginTime: string,
    city: string,
    endingTime: string | null,
    id: string,
    sectorName: string,
    street: string,
    zipCode: string,
    clientId: string | null,
    status: ReservationStatus
}

export enum ParkingEventTypeEnum{
    ENTRY = 'ENTRY',
    EXIT = 'EXIT'
}

export interface ParkingEventType {
    id: string,
    type: ParkingEventTypeEnum,
    date: string
}

export interface ReservationDetailsType {
    id: string,
    beginTime: string,
    endingTime: string,
    city: string,
    street: string,
    zipCode: string,
    sectorName: string,
    parkingEvents: ParkingEventType[] | null,
    status: ReservationStatus
}

export function arrayToDate(arr: number[]): string {
    const [year, month, day, hour, minute] = arr;
    let date = new Date(year, month - 1, day, hour, minute);

    const userTimezone = localStorage.getItem('timezone');
    const gmtOffset = 0;

    if (userTimezone !== 'GMT+0') {
        const userTimezoneOffset = date.getTimezoneOffset();
        const timezoneDifference = gmtOffset - userTimezoneOffset;
        date = new Date(date.getTime() + timezoneDifference * 60 * 1000);
    }

    return format(date, "dd.MM.yyyy HH:mm");
}