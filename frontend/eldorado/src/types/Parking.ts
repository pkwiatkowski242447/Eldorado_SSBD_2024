export enum SectorType {
    UNCOVERED = "UNCOVERED",
    COVERED = "COVERED",
    UNDERGROUND = "UNDERGROUND"
}

export enum SectorStrategy {
    LEAST_OCCUPIED = "LEAST_OCCUPIED",
    MOST_OCCUPIED = "MOST_OCCUPIED",
    LEAST_OCCUPIED_WEIGHTED = "LEAST_OCCUPIED_WEIGHTED"
}
export interface ParkingListType {
    id: string;
    city: string;
    zipCode: string;
    street: string;
    strategy: SectorStrategy;
    sectorTypes: SectorType[]
}

export interface ParkingType {
    parkingId: string;
    version: string;
    city: string;
    zipCode: string;
    street: string;
    strategy: SectorStrategy;
    signature: string;
}

export interface CreateParkingType {
    city: string;
    zipCode: string;
    street: string;
    strategy: SectorStrategy;
}

export interface SectorListType {
    id: string;
    name:string;
    active:boolean;
    type: SectorType;
    maxPlaces: number;
    availablePlaces: number;    //todo change to occupiedSpaces
    weight: number;
}

export interface CreateSectorType {
    name:string;
    active:boolean;
    type: SectorType;
    maxPlaces: number;
    weight: number;
}