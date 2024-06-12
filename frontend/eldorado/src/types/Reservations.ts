export interface ReservationType {
    beginTime: string,
    city: string,
    endingTime: string | null,
    id: string,
    sectorName: string,
    street: string,
    zipCode: string,
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

    return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
}