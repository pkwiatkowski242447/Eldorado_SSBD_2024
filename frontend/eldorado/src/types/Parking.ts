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
    id: string;
    city: string;
    zipCode: string;
    street: string;
    strategy: sectorStrategy;
}