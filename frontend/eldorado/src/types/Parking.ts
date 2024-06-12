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
    availablePlaces: number;    //todo change to occupiedSpaces
    weight: number;
}

export interface CreateSectorType {
    name:string;
    type: sectorType;
    maxPlaces: number;
    weight: number;
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
    availablePlaces: number;
}

export interface SectorInfoType {
    id: string;
    parkingId: string;
    version: string;
    name: string;
    type: sectorType;
    maxPlaces: number;
    weight: number;
}