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
    id: string;
    city: string;
    zipCode: string;
    street: string;
    strategy: SectorStrategy;
}

export interface CreateParkingType {
    city: string;
    zipCode: string;
    street: string;
    strategy: SectorStrategy;
}