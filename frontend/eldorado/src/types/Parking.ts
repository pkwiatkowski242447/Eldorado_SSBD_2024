export enum sectorType {
    UNCOVERED = "UNCOVERED",
    COVERED = "COVERED",
    UNDERGROUND = "UNDERGROUND"
}

export enum sectorStrategy {
    LEAST_OCCUPIED = "LEAST_OCCUPIED",
    MOST_OCCUPIED = "MOST_OCCUPIED",
    LEAST_OCCUPIED_WEIGHTED = "LEAST_OCCUPIED_WEIGHTED"
}
export interface ParkingListType {
    id: string;
    city: string;
    zipCode: string;
    street: string;
    strategy: sectorStrategy;
    sectorTypes: sectorType[]
}

export interface ParkingType {
    parkingId: string;
    version: string;
    city: string;
    zipCode: string;
    street: string;
    strategy: sectorStrategy;
    signature: string;
}

export interface CreateParkingType {
    city: string;
    zipCode: string;
    street: string;
    strategy: sectorStrategy;
}

export interface SectorListType {
    id: string;
    name:string;
    type: sectorType;
    maxPlaces: number;
    occupiedPlaces: number;    //todo change to occupiedSpaces
    weight: number;
    deactivationTime: string;
}

export interface CreateSectorType {
    name:string;
    type: sectorType;
    maxPlaces: number;
    weight: number;
}

export interface SectorType {
    id: string;
    name: string;
    type: sectorType;
    maxPlaces: number;
    occupiedPlaces: number;
    signature: string;
}

export interface SectorType {
    id: string;
    parkingId: string;
    name:string;
    type: sectorType;
    maxPlaces: number;
    weight: number;
    version: string;
    signature: string;
    occupiedPlaces: number;
    deactivationTime: string;
}

export interface EditSectorType {
    id: string;
    parkingId: string;
    name:string;
    type: sectorType;
    maxPlaces: number;
    weight: number;
    version: string;
    signature: string;
}

export interface SectorInfoType {
    id: string;
    parkingId: string;
    version: string;
    name: string;
    type: sectorType;
    maxPlaces: number;
    weight: number;
    active: boolean;
    deactivationTime: string;
}

//god please forgive me for what must be done
export function dtoTimeToString(arr: number[]): string {
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

export function sectorDTOtoSectorListType(object:any):SectorListType {
    const time = object.deactivationTime !== null ? dtoTimeToString(object.deactivationTime) : "";
    return {
        id: object.id,
        name: object.name,
        type: object.type,
        maxPlaces: object.maxPlaces,
        occupiedPlaces: object.occupiedPlaces,
        weight: object.weight,
        deactivationTime: time
    }
}