import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Loader2, Slash} from "lucide-react";
import {useTranslation} from "react-i18next";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";
import {useEffect, useState} from "react";
import {ParkingListType, SectorType} from "@/types/Parking.ts";
import {useParams} from "react-router-dom";
import {Button} from "@/components/ui/button.tsx";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink, PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";

function ActiveParkingInfoPage() {
    const [currentPage, setCurrentPage] = useState(() => parseInt("0"));
    const [parking, setParking] = useState<ParkingListType>();
    const [sector, setSector] = useState<SectorType[]>();
    const [isRefreshing, setIsRefreshing] = useState(false);
    const {t} = useTranslation();
    const {id} = useParams<{ id: string }>();
    const pageSize = 5;

    const fetchParkingInfo = () => {
        if (id) {
            api.getParkingInfo(id)
                .then(response => {
                    setParking(response.data);
                })
                .catch(error => {
                    handleApiError(error);
                });
        }
    };

    const fetchParkingSectors = (page?: number) => {
        if (id) {
            const actualPage = page != undefined ? page : currentPage;
            const details = `?pageNumber=${actualPage}&pageSize=${pageSize}`;
            api.getParkingSectors(id, details)
                .then(response => {
                    if (response.status === 200) {
                        setCurrentPage(actualPage);
                        setSector(response.data);
                    }
                })
                .catch(error => {
                    handleApiError(error);
                });
        }
    };

    useEffect(() => {
        fetchParkingInfo();
        fetchParkingSectors();
    }, []);

    function refresh() {
        fetchParkingInfo();
        setTimeout(() => setIsRefreshing(false), 1000);
    }

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/home">{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/parking-list">{"Active Parking List"}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{"Parking Info"}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button onClick={refresh} variant={"ghost"} className="w-auto" disabled={isRefreshing}>
                    {isRefreshing ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) : (
                        t("general.refresh")
                    )}
                </Button>
            </div>
            <div className="mx-auto grid w-full max-w-6xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">Parking Info - {parking?.city}, {parking?.street} Street</h1>
            </div>
            <div>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead
                                className="text-center">{t("City")}</TableHead>
                            <TableHead
                                className="text-center">{t("Street")}</TableHead>
                            <TableHead
                                className="text-center">{t("Zip code")}</TableHead>
                            <TableHead
                                className="text-center">{t("Strategy")}</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        <TableRow key={parking?.id} className="flex-auto">
                            <TableCell>{parking?.city}</TableCell>
                            <TableCell>{parking?.street}</TableCell>
                            <TableCell>{parking?.zipCode}</TableCell>
                            <TableCell><Badge variant={"default"}>{parking?.strategy} </Badge></TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </div>
            <div className="mx-auto grid w-full max-w-6xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">Sectors</h1>
            </div>
            <div>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead
                                className="text-center">{t("Name")}</TableHead>
                            <TableHead
                                className="text-center">{t("Type")}</TableHead>
                            <TableHead
                                className="text-center">{t("Total parking spaces")}</TableHead>
                            <TableHead
                                className="text-center">{t("Free parking spaces")}</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        {sector?.map(sector => (
                            <TableRow key={sector?.id} className="flex-auto">
                                <TableCell>{sector?.name}</TableCell>
                                <TableCell><Badge variant={"default"}>{sector?.type} </Badge></TableCell>
                                <TableCell>{sector?.maxPlaces}</TableCell>
                                <TableCell>{sector?.availablePlaces}</TableCell>

                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <div className={"pt-5"}>
                    <Pagination>
                        <PaginationContent>
                            <PaginationItem>
                                <PaginationPrevious
                                    onClick={() => {
                                        if (currentPage > 0) fetchParkingSectors(currentPage - 1)
                                    }}
                                />
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationLink>{currentPage + 1}</PaginationLink>
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationNext
                                    onClick={() => {
                                        if (sector?.length === pageSize) fetchParkingSectors(currentPage + 1)
                                    }}
                                />
                            </PaginationItem>
                        </PaginationContent>
                    </Pagination>
                </div>
            </div>
        </div>
    )
}

export default ActiveParkingInfoPage;